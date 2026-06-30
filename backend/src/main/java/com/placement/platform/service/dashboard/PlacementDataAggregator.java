package com.placement.platform.service.dashboard;

import com.placement.platform.entity.*;
import com.placement.platform.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PlacementDataAggregator {

    private final ResumeRepository resumeRepository;
    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final InterviewEvaluationRepository interviewEvaluationRepository;
    private final InterviewSessionRepository interviewSessionRepository;
    private final InterviewSessionQuestionRepository interviewSessionQuestionRepository;
    private final QuestionEvaluationRepository questionEvaluationRepository;
    private final UserTargetCompanyRepository userTargetCompanyRepository;
    private final TargetCompanyRepository targetCompanyRepository;
    private final UserSkillRepository userSkillRepository;

    public PlacementDataAggregator(
            ResumeRepository resumeRepository,
            ResumeAnalysisRepository resumeAnalysisRepository,
            InterviewEvaluationRepository interviewEvaluationRepository,
            InterviewSessionRepository interviewSessionRepository,
            InterviewSessionQuestionRepository interviewSessionQuestionRepository,
            QuestionEvaluationRepository questionEvaluationRepository,
            UserTargetCompanyRepository userTargetCompanyRepository,
            TargetCompanyRepository targetCompanyRepository,
            UserSkillRepository userSkillRepository
    ) {
        this.resumeRepository = resumeRepository;
        this.resumeAnalysisRepository = resumeAnalysisRepository;
        this.interviewEvaluationRepository = interviewEvaluationRepository;
        this.interviewSessionRepository = interviewSessionRepository;
        this.interviewSessionQuestionRepository = interviewSessionQuestionRepository;
        this.questionEvaluationRepository = questionEvaluationRepository;
        this.userTargetCompanyRepository = userTargetCompanyRepository;
        this.targetCompanyRepository = targetCompanyRepository;
        this.userSkillRepository = userSkillRepository;
    }

    @Transactional(readOnly = true)
    public PlacementData aggregate(User user) {
        Long userId = user.getId();

        // 1. Resume & Resume Analysis
        Optional<Resume> resumeOpt = resumeRepository.findByUserId(userId);
        Optional<ResumeAnalysis> resumeAnalysisOpt = resumeOpt.flatMap(resumeAnalysisRepository::findByResume);

        // 2. Interview Evaluations (Latest & Previous)
        List<InterviewEvaluation> evaluations = interviewEvaluationRepository.findLatestEvaluationsByUserId(userId, PageRequest.of(0, 2));
        Optional<InterviewEvaluation> latestEvaluation = evaluations.size() > 0 ? Optional.of(evaluations.get(0)) : Optional.empty();
        Optional<InterviewEvaluation> previousEvaluation = evaluations.size() > 1 ? Optional.of(evaluations.get(1)) : Optional.empty();

        // 3. Interview Session Counts
        long completedLearningInterviews = interviewSessionRepository.countByUserAndModeAndStatusCompleted(userId, InterviewMode.LEARNING);
        long completedMockInterviews = interviewSessionRepository.countByUserAndModeAndStatusCompleted(userId, InterviewMode.MOCK);
        long totalInterviews = completedLearningInterviews + completedMockInterviews;

        // 4. Questions Attempted
        long learningQuestionsAttempted = interviewSessionQuestionRepository.countAttemptedQuestionsByUserAndMode(userId, InterviewMode.LEARNING);
        long mockQuestionsAttempted = interviewSessionQuestionRepository.countAttemptedQuestionsByUserAndMode(userId, InterviewMode.MOCK);

        // 5. Target Companies & All Companies (for fallback eligibility)
        List<UserTargetCompany> userTargetCompanies = userTargetCompanyRepository.findByUserId(userId);
        List<TargetCompany> allCompanies = targetCompanyRepository.findAll();

        // 6. User Skills
        List<UserSkill> userSkills = userSkillRepository.findByUserId(userId);

        // 7. Question Evaluations & Answered Questions
        List<QuestionEvaluation> questionEvaluations = questionEvaluationRepository.findAllByUserId(userId);
        List<InterviewSessionQuestion> answeredQuestions = interviewSessionQuestionRepository.findAllAnsweredQuestionsByUserId(userId);

        return new PlacementData(
                user,
                resumeOpt,
                resumeAnalysisOpt,
                latestEvaluation,
                previousEvaluation,
                completedLearningInterviews,
                completedMockInterviews,
                totalInterviews,
                learningQuestionsAttempted,
                mockQuestionsAttempted,
                userTargetCompanies,
                allCompanies,
                userSkills,
                questionEvaluations,
                answeredQuestions
        );
    }

    public record PlacementData(
            User user,
            Optional<Resume> resume,
            Optional<ResumeAnalysis> resumeAnalysis,
            Optional<InterviewEvaluation> latestEvaluation,
            Optional<InterviewEvaluation> previousEvaluation,
            long completedLearningInterviews,
            long completedMockInterviews,
            long totalInterviews,
            long learningQuestionsAttempted,
            long mockQuestionsAttempted,
            List<UserTargetCompany> targetCompanies,
            List<TargetCompany> allCompanies,
            List<UserSkill> userSkills,
            List<QuestionEvaluation> questionEvaluations,
            List<InterviewSessionQuestion> answeredQuestions
    ) {}
}
