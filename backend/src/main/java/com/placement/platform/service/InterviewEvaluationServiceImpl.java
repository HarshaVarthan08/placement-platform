package com.placement.platform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.placement.platform.dto.*;
import com.placement.platform.entity.*;
import com.placement.platform.exception.*;
import com.placement.platform.mapper.InterviewEvaluationMapper;
import com.placement.platform.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterviewEvaluationServiceImpl implements InterviewEvaluationService {

    private static final Logger log = LoggerFactory.getLogger(InterviewEvaluationServiceImpl.class);

    private final UserRepository userRepository;
    private final InterviewSessionRepository interviewSessionRepository;
    private final InterviewSessionQuestionRepository interviewSessionQuestionRepository;
    private final InterviewEvaluationRepository interviewEvaluationRepository;
    private final QuestionEvaluationRepository questionEvaluationRepository;
    private final ResumeRepository resumeRepository;
    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final CandidateContextService candidateContextService;
    private final AIService aiService;
    private final InterviewEvaluationMapper interviewEvaluationMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    @Lazy
    private InterviewEvaluationService self;

    public InterviewEvaluationServiceImpl(
            UserRepository userRepository,
            InterviewSessionRepository interviewSessionRepository,
            InterviewSessionQuestionRepository interviewSessionQuestionRepository,
            InterviewEvaluationRepository interviewEvaluationRepository,
            QuestionEvaluationRepository questionEvaluationRepository,
            ResumeRepository resumeRepository,
            ResumeAnalysisRepository resumeAnalysisRepository,
            CandidateContextService candidateContextService,
            AIService aiService,
            InterviewEvaluationMapper interviewEvaluationMapper,
            ObjectMapper objectMapper
    ) {
        this.userRepository = userRepository;
        this.interviewSessionRepository = interviewSessionRepository;
        this.interviewSessionQuestionRepository = interviewSessionQuestionRepository;
        this.interviewEvaluationRepository = interviewEvaluationRepository;
        this.questionEvaluationRepository = questionEvaluationRepository;
        this.resumeRepository = resumeRepository;
        this.resumeAnalysisRepository = resumeAnalysisRepository;
        this.candidateContextService = candidateContextService;
        this.aiService = aiService;
        this.interviewEvaluationMapper = interviewEvaluationMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public InterviewEvaluationResponseDto evaluateInterview(Long sessionId) {
        Long resolvedSessionId = resolveSessionId(sessionId);
        // Prevent concurrent evaluations for the same session
        synchronized (String.valueOf(resolvedSessionId).intern()) {
            InterviewEvaluationService target = (self != null) ? self : this;
            return target.evaluateInterviewTransactional(resolvedSessionId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewEvaluationResponseDto getEvaluation(Long sessionId) {
        Long resolvedSessionId = resolveSessionId(sessionId);
        InterviewSession session = getSessionAndVerifyUser(resolvedSessionId);

        InterviewEvaluation evaluation = interviewEvaluationRepository.findByInterviewSession(session)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found for session ID: " + resolvedSessionId));

        List<InterviewSessionQuestion> sessionQuestions = interviewSessionQuestionRepository
                .findByInterviewSessionOrderByDisplayOrderAsc(session);
        List<QuestionEvaluation> questionEvaluations = questionEvaluationRepository
                .findByInterviewSessionQuestionIn(sessionQuestions);

        return interviewEvaluationMapper.toDto(evaluation, questionEvaluations);
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewEvaluationStatusResponseDto getEvaluationStatus(Long sessionId) {
        Long resolvedSessionId = resolveSessionId(sessionId);
        InterviewSession session = getSessionAndVerifyUser(resolvedSessionId);

        boolean evaluated = interviewEvaluationRepository.existsByInterviewSession(session);
        String status = evaluated ? EvaluationStatus.COMPLETED.name() : EvaluationStatus.NOT_EVALUATED.name();

        return new InterviewEvaluationStatusResponseDto(resolvedSessionId, status);
    }

    @Transactional
    public InterviewEvaluationResponseDto evaluateInterviewTransactional(Long sessionId) {
        InterviewSession session = getSessionAndVerifyUser(sessionId);

        // 1. Cache Check: Return cached result if already evaluated
        Optional<InterviewEvaluation> existingEvaluationOpt = interviewEvaluationRepository.findByInterviewSession(session);
        if (existingEvaluationOpt.isPresent()) {
            log.info("Returning cached evaluation for session: {}", sessionId);
            List<InterviewSessionQuestion> sessionQuestions = interviewSessionQuestionRepository
                    .findByInterviewSessionOrderByDisplayOrderAsc(session);
            List<QuestionEvaluation> questionEvaluations = questionEvaluationRepository
                    .findByInterviewSessionQuestionIn(sessionQuestions);
            return interviewEvaluationMapper.toDto(existingEvaluationOpt.get(), questionEvaluations);
        }

        // 2. Perform Validations
        if (session.getStatus() != InterviewStatus.COMPLETED) {
            throw new IllegalArgumentException("Interview session must be COMPLETED before evaluation can be performed.");
        }

        List<InterviewSessionQuestion> questions = interviewSessionQuestionRepository
                .findByInterviewSessionOrderByDisplayOrderAsc(session);

        long answeredCount = questions.stream()
                .filter(q -> q.getStatus() == SessionQuestionStatus.ANSWERED || q.getTextAnswer() != null)
                .count();

        if (answeredCount == 0) {
            throw new InterviewEvaluationException("No answered questions exist in the interview session.");
        }

        // Audio support check
        for (InterviewSessionQuestion q : questions) {
            if (q.getAnswerType() == AnswerType.AUDIO && (q.getTextAnswer() == null || q.getTextAnswer().trim().isEmpty())) {
                throw new InterviewEvaluationException("Audio evaluation is not yet supported. Audio answers must be transcribed before evaluation.");
            }
        }

        // 3. Build Prompt Context
        User user = session.getUser();
        CandidateContext context = candidateContextService.buildContext(user);
        Resume resume = resumeRepository.findByUser(user)
                .orElseThrow(() -> new ResumeMissingException("No resume found. Please upload your resume first."));
        ResumeAnalysis resumeAnalysis = resumeAnalysisRepository.findByResume(resume)
                .orElseThrow(() -> new ResumeAnalysisMissingException("Resume analysis is missing. Please analyze your resume first."));
        Integer atsScore = resumeAnalysis.getAtsScore();

        String prompt = buildEvaluationPrompt(context, atsScore, questions);

        // 4. Invoke Gemini API
        long startTime = System.currentTimeMillis();
        String rawResponse = aiService.evaluateInterview(prompt);
        long duration = System.currentTimeMillis() - startTime;

        // 5. Parse and Validate Response
        GeminiEvaluationJson parsedJson = parseAndValidateGeminiResponse(rawResponse, questions);

        // 6. Save Entities
        InterviewEvaluation evaluation = new InterviewEvaluation();
        evaluation.setInterviewSession(session);
        evaluation.setOverallScore(parsedJson.overallScore());
        evaluation.setTechnicalScore(parsedJson.technicalScore());
        evaluation.setCommunicationScore(parsedJson.communicationScore());
        evaluation.setProblemSolvingScore(parsedJson.problemSolvingScore());
        evaluation.setConfidenceScore(parsedJson.confidenceScore());
        evaluation.setProfileMatchScore(parsedJson.profileMatchScore());
        evaluation.setVerdict(Verdict.valueOf(parsedJson.verdict()));
        evaluation.setVerdictJustification(parsedJson.verdictJustification());
        evaluation.setSummary(parsedJson.summary());
        evaluation.setOverallFeedback(parsedJson.overallFeedback());
        evaluation.setStrengths(parsedJson.strengths());
        evaluation.setWeaknesses(parsedJson.weaknesses());
        evaluation.setRecommendedTopics(parsedJson.recommendedTopics());
        evaluation.setLearningPlan(parsedJson.learningPlan());
        evaluation.setStatus(EvaluationStatus.COMPLETED);
        evaluation.setPromptVersion(1);
        evaluation.setEvaluationDurationMs(duration);
        evaluation.setRawResponse(rawResponse);
        evaluation.setModelUsed(aiService.getModelUsed());
        evaluation.setEvaluationVersion(1);
        evaluation.setEvaluatedAt(LocalDateTime.now());

        // Calculate performance band based on overall score
        evaluation.setPerformanceBand(calculatePerformanceBand(parsedJson.overallScore()));

        InterviewEvaluation savedEvaluation = interviewEvaluationRepository.save(evaluation);

        List<QuestionEvaluation> savedQuestionEvaluations = new ArrayList<>();
        Map<Long, InterviewSessionQuestion> questionMap = questions.stream()
                .collect(Collectors.toMap(InterviewSessionQuestion::getId, q -> q));

        for (GeminiQuestionEvaluationJson qJson : parsedJson.questionEvaluations()) {
            InterviewSessionQuestion sq = questionMap.get(qJson.questionId());
            QuestionEvaluation qEval = new QuestionEvaluation();
            qEval.setInterviewSessionQuestion(sq);
            qEval.setScore(qJson.score());
            qEval.setStrengths(qJson.strengths());
            qEval.setWeaknesses(qJson.weaknesses());
            qEval.setFeedback(qJson.feedback());
            qEval.setImprovement(qJson.improvement());
            qEval.setIdealAnswer(qJson.idealAnswer());
            savedQuestionEvaluations.add(questionEvaluationRepository.save(qEval));
        }

        return interviewEvaluationMapper.toDto(savedEvaluation, savedQuestionEvaluations);
    }

    private Long resolveSessionId(Long sessionId) {
        if (sessionId != null) {
            return sessionId;
        }
        User user = getAuthenticatedUser();
        InterviewSession session = interviewSessionRepository
                .findFirstByUserAndStatusOrderByCompletedAtDesc(user, InterviewStatus.COMPLETED)
                .orElseThrow(() -> new ResourceNotFoundException("No completed interview sessions found for the current user."));
        return session.getId();
    }

    private InterviewSession getSessionAndVerifyUser(Long sessionId) {
        User user = getAuthenticatedUser();
        InterviewSession session = interviewSessionRepository.findById(sessionId)
                .orElseThrow(() -> new InterviewSessionNotFoundException("Interview session not found with ID: " + sessionId));
        if (!session.getUser().getId().equals(user.getId())) {
            throw new InterviewEvaluationException("You do not have permission to access this interview session.");
        }
        return session;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("User is not authenticated");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    private PerformanceBand calculatePerformanceBand(int overallScore) {
        if (overallScore >= 90 && overallScore <= 100) {
            return PerformanceBand.EXCELLENT;
        } else if (overallScore >= 75 && overallScore < 90) {
            return PerformanceBand.GOOD;
        } else if (overallScore >= 60 && overallScore < 75) {
            return PerformanceBand.AVERAGE;
        } else {
            return PerformanceBand.NEEDS_IMPROVEMENT;
        }
    }

    private String buildEvaluationPrompt(CandidateContext context, Integer atsScore, List<InterviewSessionQuestion> questions) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an expert technical interviewer and executive talent evaluator. ");
        prompt.append("Evaluate the following completed interview session and provide overall scores, verdict, and detailed feedback. ")
              .append("The evaluation must be performed on the entire interview in one response.\n\n");

        prompt.append("### CANDIDATE CONTEXT ###\n");
        prompt.append("Name: ").append(context.name()).append("\n");
        prompt.append("College: ").append(context.college()).append("\n");
        prompt.append("Degree: ").append(context.degree()).append("\n");
        prompt.append("Branch: ").append(context.branch()).append("\n");
        prompt.append("CGPA: ").append(context.cgpa()).append("\n");
        prompt.append("Target Role: ").append(context.targetRole()).append("\n");
        prompt.append("Projects:\n").append(context.projects()).append("\n");
        prompt.append("Internships:\n").append(context.internship()).append("\n");
        prompt.append("Skills: ").append(String.join(", ", context.skills())).append("\n");
        prompt.append("Target Companies: ").append(String.join(", ", context.targetCompanies())).append("\n");
        prompt.append("ATS Score: ").append(atsScore).append("\n");
        prompt.append("Resume Summary: ").append(context.resumeSummary()).append("\n");
        prompt.append("Resume Strengths: ").append(String.join(", ", context.resumeStrengths())).append("\n");
        prompt.append("Resume Weaknesses: ").append(String.join(", ", context.resumeWeaknesses())).append("\n");
        prompt.append("Resume Missing Skills: ").append(String.join(", ", context.resumeMissingSkills())).append("\n\n");

        prompt.append("### INTERVIEW LOGS ###\n");
        for (InterviewSessionQuestion q : questions) {
            prompt.append("Question ID: ").append(q.getId()).append("\n");
            prompt.append("Question: ").append(q.getQuestionText()).append("\n");
            prompt.append("Category: ").append(q.getCategory()).append("\n");
            prompt.append("Difficulty: ").append(q.getDifficulty()).append("\n");
            String ideal = q.getPersonalizedQuestion() != null ? q.getPersonalizedQuestion().getIdealAnswer() : q.getGlobalQuestion().getIdealAnswer();
            prompt.append("Ideal Answer: ").append(ideal).append("\n");
            prompt.append("Candidate Answer: ").append(q.getTextAnswer() != null ? q.getTextAnswer() : "No Answer").append("\n");
            prompt.append("--------------------------------------------------\n");
        }

        prompt.append("\n### EVALUATION INSTRUCTIONS ###\n");
        prompt.append("Analyze the candidate's performance across all questions. Compare candidate answers against the ideal answers. ")
              .append("Evaluate communication, technical competency, problem solving, confidence, and match with the candidate's profile and target role.\n\n");

        prompt.append("Provide scores out of 100 for the following categories:\n")
              .append("- overallScore (Must be calculated as an integer representing general suitability.)\n")
              .append("- technicalScore\n")
              .append("- communicationScore\n")
              .append("- problemSolvingScore\n")
              .append("- confidenceScore\n")
              .append("- profileMatchScore\n\n");

        prompt.append("Also output a Verdict, which must be exactly one of: 'HIRE', 'MAYBE', 'NO_HIRE'. Provide a concise justification for this verdict.\n\n");

        prompt.append("For each individual question, evaluate the response and provide:\n")
              .append("- score (0 to 100)\n")
              .append("- strengths (list of key points the candidate explained well)\n")
              .append("- weaknesses (list of incorrect, missing, or weak points)\n")
              .append("- feedback (constructive critique)\n")
              .append("- improvement (actionable advice to improve)\n")
              .append("- idealAnswer (refined/perfect answer for this question)\n\n");

        prompt.append("You MUST output your evaluation ONLY as a valid JSON object matching the schema below. ")
              .append("Do not include any explanations outside the JSON object. Do not wrap the JSON object in markdown blocks (such as ```json ... ```). ")
              .append("Make sure the JSON response contains every single field listed below and matches the structure exactly:\n\n")
              .append("{\n")
              .append("  \"overallScore\": 85,\n")
              .append("  \"technicalScore\": 80,\n")
              .append("  \"communicationScore\": 90,\n")
              .append("  \"problemSolvingScore\": 85,\n")
              .append("  \"confidenceScore\": 85,\n")
              .append("  \"profileMatchScore\": 82,\n")
              .append("  \"verdict\": \"HIRE\",\n")
              .append("  \"verdictJustification\": \"The candidate demonstrated strong coding logic and clear articulation of system architecture concepts.\",\n")
              .append("  \"summary\": \"The candidate has a solid foundation in the target domain...\",\n")
              .append("  \"overallFeedback\": \"Continue practicing advanced multithreading...\",\n")
              .append("  \"strengths\": [\"Clear communication\", \"Solid OOP fundamentals\"],\n")
              .append("  \"weaknesses\": [\"Gaps in concurrency theory\"],\n")
              .append("  \"recommendedTopics\": [\"Java Concurrency\", \"Thread Pools\"],\n")
              .append("  \"learningPlan\": [\"Read Chapter 3 of Concurrency in Practice\", \"Build a socket server\"],\n")
              .append("  \"questionEvaluations\": [\n")
              .append("    {\n")
              .append("      \"questionId\": 10,\n")
              .append("      \"score\": 90,\n")
              .append("      \"strengths\": [\"...\"],\n")
              .append("      \"weaknesses\": [\"...\"],\n")
              .append("      \"feedback\": \"...\",\n")
              .append("      \"improvement\": \"...\",\n")
              .append("      \"idealAnswer\": \"...\"\n")
              .append("    }\n")
              .append("  ]\n")
              .append("}\n");

        return prompt.toString();
    }

    private GeminiEvaluationJson parseAndValidateGeminiResponse(String rawResponse, List<InterviewSessionQuestion> questions) {
        if (rawResponse == null || rawResponse.trim().isEmpty()) {
            throw new InterviewEvaluationException("Gemini returned an empty response.");
        }

        GeminiEvaluationJson parsed;
        try {
            parsed = objectMapper.readValue(rawResponse, GeminiEvaluationJson.class);
        } catch (Exception e) {
            throw new InterviewEvaluationException("Gemini response was not valid JSON: " + e.getMessage(), e);
        }

        // Validate mandatory fields
        validateMandatoryFields(parsed);

        // Validate score ranges
        validateScore(parsed.overallScore(), "overallScore");
        validateScore(parsed.technicalScore(), "technicalScore");
        validateScore(parsed.communicationScore(), "communicationScore");
        validateScore(parsed.problemSolvingScore(), "problemSolvingScore");
        validateScore(parsed.confidenceScore(), "confidenceScore");
        validateScore(parsed.profileMatchScore(), "profileMatchScore");

        // Validate Verdict
        try {
            Verdict.valueOf(parsed.verdict());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InterviewEvaluationException("Gemini returned an invalid verdict: " + parsed.verdict());
        }

        // Validate questionEvaluations count equals the number of questions in session
        if (parsed.questionEvaluations().size() != questions.size()) {
            throw new InterviewEvaluationException(String.format(
                    "Gemini returned evaluations count mismatch. Expected %d, but got %d.",
                    questions.size(), parsed.questionEvaluations().size()));
        }

        // Validate question ID mapping
        Set<Long> allowedQuestionIds = questions.stream()
                .map(InterviewSessionQuestion::getId)
                .collect(Collectors.toSet());
        Set<Long> processedQuestionIds = new HashSet<>();

        for (GeminiQuestionEvaluationJson qJson : parsed.questionEvaluations()) {
            if (qJson.questionId() == null) {
                throw new InterviewEvaluationException("Gemini questionId cannot be null.");
            }
            if (!allowedQuestionIds.contains(qJson.questionId())) {
                throw new InterviewEvaluationException("Gemini returned an invalid questionId: " + qJson.questionId());
            }
            if (processedQuestionIds.contains(qJson.questionId())) {
                throw new InterviewEvaluationException("Gemini returned duplicate evaluation for questionId: " + qJson.questionId());
            }
            validateScore(qJson.score(), "Question ID " + qJson.questionId() + " score");
            processedQuestionIds.add(qJson.questionId());
        }

        return parsed;
    }

    private void validateScore(Integer score, String fieldName) {
        if (score == null || score < 0 || score > 100) {
            throw new InterviewEvaluationException(fieldName + " must be between 0 and 100.");
        }
    }

    private void validateMandatoryFields(GeminiEvaluationJson parsed) {
        if (parsed == null) {
            throw new InterviewEvaluationException("Evaluation response object cannot be null.");
        }
        if (parsed.overallScore() == null) throw new InterviewEvaluationException("Mandatory field missing: overallScore");
        if (parsed.technicalScore() == null) throw new InterviewEvaluationException("Mandatory field missing: technicalScore");
        if (parsed.communicationScore() == null) throw new InterviewEvaluationException("Mandatory field missing: communicationScore");
        if (parsed.problemSolvingScore() == null) throw new InterviewEvaluationException("Mandatory field missing: problemSolvingScore");
        if (parsed.confidenceScore() == null) throw new InterviewEvaluationException("Mandatory field missing: confidenceScore");
        if (parsed.profileMatchScore() == null) throw new InterviewEvaluationException("Mandatory field missing: profileMatchScore");
        if (parsed.verdict() == null || parsed.verdict().trim().isEmpty()) throw new InterviewEvaluationException("Mandatory field missing: verdict");
        if (parsed.verdictJustification() == null || parsed.verdictJustification().trim().isEmpty()) throw new InterviewEvaluationException("Mandatory field missing: verdictJustification");
        if (parsed.summary() == null || parsed.summary().trim().isEmpty()) throw new InterviewEvaluationException("Mandatory field missing: summary");
        if (parsed.overallFeedback() == null || parsed.overallFeedback().trim().isEmpty()) throw new InterviewEvaluationException("Mandatory field missing: overallFeedback");
        if (parsed.strengths() == null) throw new InterviewEvaluationException("Mandatory field missing: strengths");
        if (parsed.weaknesses() == null) throw new InterviewEvaluationException("Mandatory field missing: weaknesses");
        if (parsed.recommendedTopics() == null) throw new InterviewEvaluationException("Mandatory field missing: recommendedTopics");
        if (parsed.learningPlan() == null) throw new InterviewEvaluationException("Mandatory field missing: learningPlan");
        if (parsed.questionEvaluations() == null) throw new InterviewEvaluationException("Mandatory field missing: questionEvaluations");

        for (GeminiQuestionEvaluationJson qJson : parsed.questionEvaluations()) {
            if (qJson.questionId() == null) throw new InterviewEvaluationException("Mandatory question evaluation field missing: questionId");
            if (qJson.score() == null) throw new InterviewEvaluationException("Mandatory question evaluation field missing: score");
            if (qJson.strengths() == null) throw new InterviewEvaluationException("Mandatory question evaluation field missing: strengths");
            if (qJson.weaknesses() == null) throw new InterviewEvaluationException("Mandatory question evaluation field missing: weaknesses");
            if (qJson.feedback() == null || qJson.feedback().trim().isEmpty()) throw new InterviewEvaluationException("Mandatory question evaluation field missing: feedback");
            if (qJson.improvement() == null || qJson.improvement().trim().isEmpty()) throw new InterviewEvaluationException("Mandatory question evaluation field missing: improvement");
            if (qJson.idealAnswer() == null || qJson.idealAnswer().trim().isEmpty()) throw new InterviewEvaluationException("Mandatory question evaluation field missing: idealAnswer");
        }
    }

    // Inner records for Gemini JSON mapping
    public record GeminiQuestionEvaluationJson(
        Long questionId,
        Integer score,
        List<String> strengths,
        List<String> weaknesses,
        String feedback,
        String improvement,
        String idealAnswer
    ) {}

    public record GeminiEvaluationJson(
        Integer overallScore,
        Integer technicalScore,
        Integer communicationScore,
        Integer problemSolvingScore,
        Integer confidenceScore,
        Integer profileMatchScore,
        String verdict,
        String verdictJustification,
        String summary,
        String overallFeedback,
        List<String> strengths,
        List<String> weaknesses,
        List<String> recommendedTopics,
        List<String> learningPlan,
        List<GeminiQuestionEvaluationJson> questionEvaluations
    ) {}
}
