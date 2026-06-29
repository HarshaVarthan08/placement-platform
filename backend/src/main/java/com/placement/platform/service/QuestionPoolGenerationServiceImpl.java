package com.placement.platform.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.placement.platform.dto.CandidateContext;
import com.placement.platform.entity.*;
import com.placement.platform.exception.GeminiApiException;
import com.placement.platform.exception.QuestionPoolGenerationException;
import com.placement.platform.repository.InterviewProfileRepository;
import com.placement.platform.repository.InterviewQuestionPoolRepository;
import com.placement.platform.repository.InterviewQuestionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionPoolGenerationServiceImpl implements QuestionPoolGenerationService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model:gemini-2.5-flash}")
    private String model;

    private final CandidateContextService candidateContextService;
    private final InterviewProfileService interviewProfileService;
    private final InterviewQuestionPoolRepository interviewQuestionPoolRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public QuestionPoolGenerationServiceImpl(
            CandidateContextService candidateContextService,
            InterviewProfileService interviewProfileService,
            InterviewQuestionPoolRepository interviewQuestionPoolRepository,
            InterviewQuestionRepository interviewQuestionRepository,
            ObjectMapper objectMapper
    ) {
        this.candidateContextService = candidateContextService;
        this.interviewProfileService = interviewProfileService;
        this.interviewQuestionPoolRepository = interviewQuestionPoolRepository;
        this.interviewQuestionRepository = interviewQuestionRepository;
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplate();
    }

    @Override
    @Transactional
    public InterviewQuestionPool generateQuestionPool(User user, InterviewDifficulty difficulty) {
        CandidateContext context = candidateContextService.buildContext(user);

        GeminiQuestionPoolResponse poolResponse = null;
        Exception firstAttemptException = null;

        // Attempt 1
        try {
            poolResponse = callGeminiAndParse(context, difficulty);
            validatePoolResponse(poolResponse);
        } catch (Exception e) {
            firstAttemptException = e;
            // Attempt 2 (Retry exactly once)
            try {
                poolResponse = callGeminiAndParse(context, difficulty);
                validatePoolResponse(poolResponse);
                firstAttemptException = null; // Cleared on success
            } catch (Exception retryEx) {
                throw new QuestionPoolGenerationException(
                        "Question pool generation failed after retry. Primary error: " 
                        + e.getMessage() + ". Retry error: " + retryEx.getMessage(), retryEx);
            }
        }

        if (firstAttemptException != null) {
            throw new QuestionPoolGenerationException("Question pool generation failed: " + firstAttemptException.getMessage(), firstAttemptException);
        }

        // Retrieve current profile version
        InterviewProfile profile = interviewProfileService.getOrCreateProfile(user);
        int currentProfileVersion = profile.getProfileVersion();

        // Create new pool
        InterviewQuestionPool newPool = new InterviewQuestionPool();
        newPool.setUser(user);
        newPool.setProfileVersion(currentProfileVersion);
        newPool.setPromptVersion(1);
        newPool.setModelUsed(model);
        newPool.setStatus(QuestionPoolStatus.ACTIVE);
        newPool.setGeneratedAt(LocalDateTime.now());
        newPool.setUpdatedAt(LocalDateTime.now());

        // Persist the new pool successfully
        InterviewQuestionPool savedPool = interviewQuestionPoolRepository.save(newPool);

        // Map and validate questions
        List<GeminiQuestionDto> gQuestions = poolResponse.questions();
        List<InterviewQuestion> questionsToSave = new ArrayList<>();
        for (int i = 0; i < gQuestions.size(); i++) {
            GeminiQuestionDto gq = gQuestions.get(i);
            InterviewQuestion question = new InterviewQuestion();
            question.setQuestionPool(savedPool);
            question.setQuestion(gq.question());
            question.setIdealAnswer(gq.idealAnswer());
            question.setTopic(gq.topic());
            question.setCategory(parseCategory(gq.category()));
            question.setDifficulty(parseDifficulty(gq.difficulty()));
            question.setKeyPoints(gq.keyPoints());
            question.setDisplayOrder(i + 1);
            questionsToSave.add(question);
        }

        // Validate display order uniqueness and range [1, 50]
        validateDisplayOrders(questionsToSave);

        // Persist questions successfully
        for (InterviewQuestion question : questionsToSave) {
            interviewQuestionRepository.save(question);
        }

        // Archive previous pools (both ACTIVE and OUTDATED) only after successful persistence of the new pool
        List<InterviewQuestionPool> existingPools = interviewQuestionPoolRepository.findByUser(user);
        for (InterviewQuestionPool oldPool : existingPools) {
            // Keep the new pool untouched
            if (oldPool.getId().equals(savedPool.getId())) {
                continue;
            }
            if (oldPool.getStatus() == QuestionPoolStatus.ACTIVE || oldPool.getStatus() == QuestionPoolStatus.OUTDATED) {
                oldPool.setStatus(QuestionPoolStatus.ARCHIVED);
                interviewQuestionPoolRepository.save(oldPool);
            }
        }

        return savedPool;
    }

    private void validateDisplayOrders(List<InterviewQuestion> questions) {
        Set<Integer> displayOrders = new HashSet<>();
        for (InterviewQuestion q : questions) {
            Integer order = q.getDisplayOrder();
            if (order == null || order < 1 || order > 50) {
                throw new QuestionPoolGenerationException("displayOrder is missing or out of range (1-50): " + order);
            }
            if (!displayOrders.add(order)) {
                throw new QuestionPoolGenerationException("Duplicate displayOrder detected: " + order);
            }
        }
        if (displayOrders.size() != 50) {
            throw new QuestionPoolGenerationException("Missing displayOrders, expected exactly 50 unique values (1-50)");
        }
    }

    private GeminiQuestionPoolResponse callGeminiAndParse(CandidateContext context, InterviewDifficulty difficulty) {
        if (apiKey == null || apiKey.trim().isEmpty() || apiKey.equals("${GEMINI_API_KEY}")) {
            throw new GeminiApiException("Gemini API key is missing or not configured. Please set the GEMINI_API_KEY environment variable.");
        }

        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey;

        String prompt = buildPrompt(context, difficulty);

        // Build Payload
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", prompt);

        Map<String, Object> partsObj = new HashMap<>();
        partsObj.put("parts", Collections.singletonList(textPart));

        requestBody.put("contents", Collections.singletonList(partsObj));

        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("responseMimeType", "application/json");
        requestBody.put("generationConfig", generationConfig);

        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(url, requestBody, String.class);
        } catch (Exception e) {
            throw new GeminiApiException("Failed to communicate with Gemini API: " + e.getMessage(), e);
        }

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new GeminiApiException("Gemini API call failed with status: " + response.getStatusCode());
        }

        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode candidates = rootNode.path("candidates");
            if (candidates.isArray() && candidates.size() > 0) {
                JsonNode parts = candidates.get(0).path("content").path("parts");
                if (parts.isArray() && parts.size() > 0) {
                    String rawText = parts.get(0).path("text").asText();
                    if (rawText != null && !rawText.trim().isEmpty()) {
                        String cleanedJson = cleanJsonString(rawText);
                        return objectMapper.readValue(cleanedJson, GeminiQuestionPoolResponse.class);
                    }
                }
            }
            throw new GeminiApiException("Failed to parse questions: Gemini response candidates block was empty or malformed.");
        } catch (Exception e) {
            throw new GeminiApiException("Failed to parse Gemini API response: " + e.getMessage(), e);
        }
    }

    private String buildPrompt(CandidateContext context, InterviewDifficulty difficulty) {
        StringBuilder companiesSection = new StringBuilder();
        if (context.targetCompanies() != null && !context.targetCompanies().isEmpty()) {
            companiesSection.append("\nTarget Companies: ")
                    .append(String.join(", ", context.targetCompanies()))
                    .append("\nNote: Tailor technical/scenario questions to the engineering standards and hiring patterns of these companies.");
        }

        return "You are an elite placement preparation interviewer. Generate exactly 50 personalized interview questions for the following candidate based ONLY on the supplied candidate context.\n" +
                "Never invent experience, projects, or internships not present in the candidate context.\n\n" +
                "Candidate Context:\n" +
                "Name: " + context.name() + "\n" +
                "College: " + context.college() + "\n" +
                "Degree: " + context.degree() + "\n" +
                "Branch: " + context.branch() + "\n" +
                "CGPA: " + context.cgpa() + "\n" +
                "Target Role: " + context.targetRole() + "\n" +
                "Projects: " + (context.projects() != null ? context.projects() : "None specified") + "\n" +
                "Internship: " + (context.internship() != null ? context.internship() : "None specified") + "\n" +
                "Skills: " + String.join(", ", context.skills()) + "\n" +
                "Resume Summary: " + context.resumeSummary() + "\n" +
                "Resume Strengths: " + String.join("; ", context.resumeStrengths()) + "\n" +
                "Resume Weaknesses: " + String.join("; ", context.resumeWeaknesses()) + "\n" +
                "Missing Skills: " + String.join(", ", context.resumeMissingSkills()) + "\n" +
                companiesSection.toString() + "\n\n" +
                "Requirements:\n" +
                "1. Generate exactly 50 questions.\n" +
                "2. The questions must match the requested difficulty level: " + difficulty.name() + ".\n" +
                "3. Distribute the questions across categories as follows:\n" +
                "   - PROJECTS: exactly 15 questions (deeply evaluating the candidate's listed projects/experience)\n" +
                "   - TECHNICAL: exactly 15 questions (evaluating candidate's skills, programming languages, databases, or algorithms)\n" +
                "   - SCENARIO: exactly 10 questions (system design, situation judgement, architectural/scaling problems)\n" +
                "   - BEHAVIORAL: exactly 5 questions (collaboration, conflict resolution, ownership, growth mindset)\n" +
                "   - HR: exactly 5 questions (career goals, values alignment, college preparation, cultural fit)\n" +
                "4. Clean, valid JSON output matching this schema ONLY:\n" +
                "{\n" +
                "  \"questions\": [\n" +
                "    {\n" +
                "      \"question\": \"Question text here\",\n" +
                "      \"idealAnswer\": \"Detailed model answer that matches candidate skills/profile\",\n" +
                "      \"keyPoints\": [\n" +
                "        \"Important point 1\",\n" +
                "        \"Important point 2\"\n" +
                "      ],\n" +
                "      \"topic\": \"Specific Topic (e.g. Java Streams, React Hooks, ATS Optimization)\",\n" +
                "      \"category\": \"PROJECT\",\n" +
                "      \"difficulty\": \"" + difficulty.name() + "\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n\n" +
                "Do not prefix or suffix the response with markdown formatting like ```json or ```. Return the raw JSON block directly.";
    }

    private void validatePoolResponse(GeminiQuestionPoolResponse pool) {
        if (pool == null || pool.questions() == null) {
            throw new QuestionPoolGenerationException("Gemini returned null pool or empty questions list");
        }

        List<GeminiQuestionDto> questions = pool.questions();

        // 1. Validate exactly 50 questions
        if (questions.size() != 50) {
            throw new QuestionPoolGenerationException("Generated question pool must contain exactly 50 questions. Got: " + questions.size());
        }

        // 2. Validate category distribution
        int projectsCount = 0;
        int technicalCount = 0;
        int scenarioCount = 0;
        int behavioralCount = 0;
        int hrCount = 0;

        Set<String> uniqueQuestionTexts = new HashSet<>();

        for (int i = 0; i < questions.size(); i++) {
            GeminiQuestionDto q = questions.get(i);
            
            // 3. Quality and null/empty validation
            if (q.question() == null || q.question().trim().length() < 15) {
                throw new QuestionPoolGenerationException("Question " + (i + 1) + " text is missing or too short (must be at least 15 chars).");
            }
            if (q.idealAnswer() == null || q.idealAnswer().trim().length() < 15) {
                throw new QuestionPoolGenerationException("Question " + (i + 1) + " idealAnswer is missing or too short (must be at least 15 chars).");
            }
            if (q.topic() == null || q.topic().trim().isEmpty()) {
                throw new QuestionPoolGenerationException("Question " + (i + 1) + " topic is missing.");
            }
            if (q.category() == null) {
                throw new QuestionPoolGenerationException("Question " + (i + 1) + " category is missing.");
            }
            if (q.difficulty() == null) {
                throw new QuestionPoolGenerationException("Question " + (i + 1) + " difficulty is missing.");
            }

            // 4. Duplicate checks
            String cleanQuestion = q.question().trim().toLowerCase();
            if (uniqueQuestionTexts.contains(cleanQuestion)) {
                throw new QuestionPoolGenerationException("Duplicate question text detected: " + q.question());
            }
            uniqueQuestionTexts.add(cleanQuestion);

            // Distribution aggregation
            String cat = q.category().toUpperCase().trim();
            switch (cat) {
                case "PROJECT":
                case "PROJECTS":
                    projectsCount++;
                    break;
                case "TECHNICAL":
                    technicalCount++;
                    break;
                case "SCENARIO":
                    scenarioCount++;
                    break;
                case "BEHAVIORAL":
                    behavioralCount++;
                    break;
                case "HR":
                    hrCount++;
                    break;
                default:
                    throw new QuestionPoolGenerationException("Unknown question category: " + q.category());
            }
        }

        if (projectsCount != 15 || technicalCount != 15 || scenarioCount != 10 || behavioralCount != 5 || hrCount != 5) {
            throw new QuestionPoolGenerationException(String.format(
                    "Invalid category distribution: Projects=%d (expected 15), Technical=%d (expected 15), " +
                            "Scenario=%d (expected 10), Behavioral=%d (expected 5), HR=%d (expected 5)",
                    projectsCount, technicalCount, scenarioCount, behavioralCount, hrCount
            ));
        }
    }

    private String cleanJsonString(String text) {
        if (text == null) {
            return null;
        }
        text = text.trim();
        if (text.startsWith("```json")) {
            text = text.substring(7);
        } else if (text.startsWith("```")) {
            text = text.substring(3);
        }
        if (text.endsWith("```")) {
            text = text.substring(0, text.length() - 3);
        }
        return text.trim();
    }

    private QuestionCategory parseCategory(String categoryStr) {
        String cat = categoryStr.toUpperCase().trim();
        if (cat.equals("PROJECTS")) {
            return QuestionCategory.PROJECT;
        }
        try {
            return QuestionCategory.valueOf(cat);
        } catch (IllegalArgumentException e) {
            throw new QuestionPoolGenerationException("Invalid question category value: " + categoryStr);
        }
    }

    private InterviewDifficulty parseDifficulty(String diffStr) {
        try {
            return InterviewDifficulty.valueOf(diffStr.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            throw new QuestionPoolGenerationException("Invalid question difficulty value: " + diffStr);
        }
    }

    // JSON mapping helper records
    public record GeminiQuestionDto(
            String question,
            String idealAnswer,
            List<String> keyPoints,
            String topic,
            String category,
            String difficulty
    ) {}

    public record GeminiQuestionPoolResponse(
            List<GeminiQuestionDto> questions
    ) {}
}
