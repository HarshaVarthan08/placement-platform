package com.placement.platform.service.dashboard;

import com.placement.platform.dto.dashboard.*;
import com.placement.platform.entity.*;
import com.placement.platform.service.EligibilityService;
import com.placement.platform.service.dashboard.PlacementDataAggregator.PlacementData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardAnalyticsServiceImpl implements DashboardAnalyticsService {

    private final ReadinessService readinessService;
    private final RecommendationEngine recommendationEngine;
    private final DashboardHealthService dashboardHealthService;
    private final SkillAnalyticsService skillAnalyticsService;
    private final EligibilityService eligibilityService;

    @Value("${application.dashboard.ats.threshold:80}")
    private int atsThreshold;

    public DashboardAnalyticsServiceImpl(
            ReadinessService readinessService,
            RecommendationEngine recommendationEngine,
            DashboardHealthService dashboardHealthService,
            SkillAnalyticsService skillAnalyticsService,
            EligibilityService eligibilityService
    ) {
        this.readinessService = readinessService;
        this.recommendationEngine = recommendationEngine;
        this.dashboardHealthService = dashboardHealthService;
        this.skillAnalyticsService = skillAnalyticsService;
        this.eligibilityService = eligibilityService;
    }

    @Override
    public DashboardResponseDto buildDashboardResponse(User user, PlacementData data) {
        LocalDateTime generatedAt = LocalDateTime.now();
        Integer dashboardVersion = 1;

        // Build individual cards using subordinate services or direct logic
        PlacementReadinessCard readinessCard = readinessService.calculateReadiness(user, data);
        ResumeCard resumeCard = buildResumeCard(data);
        InterviewCard interviewCard = buildInterviewCard(data);
        CompanyCard companyCard = buildCompanyCard(user, data);
        PracticeCard practiceCard = buildPracticeCard(data);
        RecommendationCard recommendationCard = recommendationEngine.generateRecommendations(user, data);
        DashboardHealthCard healthCard = dashboardHealthService.calculateHealth(user, data);
        SkillAnalyticsCard skillCard = skillAnalyticsService.analyzeSkills(user, data);
        QuickActionCard quickActionCard = buildQuickActionCard(user, data);

        return new DashboardResponseDto(
                generatedAt,
                dashboardVersion,
                readinessCard,
                resumeCard,
                interviewCard,
                companyCard,
                practiceCard,
                recommendationCard,
                healthCard,
                skillCard,
                quickActionCard
        );
    }

    private ResumeCard buildResumeCard(PlacementData data) {
        if (data.resume().isEmpty()) {
            return new ResumeCard(
                    "NOT_AVAILABLE",
                    null,
                    null,
                    false,
                    false,
                    null,
                    null,
                    0,
                    "N/A",
                    null
            );
        }

        Resume resume = data.resume().get();
        boolean analyzed = data.resumeAnalysis().isPresent();
        Integer ats = analyzed ? data.resumeAnalysis().get().getAtsScore() : null;
        LocalDateTime lastAnalysis = analyzed ? data.resumeAnalysis().get().getUpdatedAt() : null;

        // Since it's @OneToOne, trend comparison is null/0. Let's return 0 trend
        return new ResumeCard(
                "AVAILABLE",
                ats,
                1, // default resume version is 1 since we only have single resume
                true,
                analyzed,
                resume.getUploadedAt(),
                lastAnalysis,
                0,
                "0.0%",
                lastAnalysis != null ? lastAnalysis : resume.getUploadedAt()
        );
    }

    private InterviewCard buildInterviewCard(PlacementData data) {
        if (data.latestEvaluation().isEmpty()) {
            return new InterviewCard(
                    "NOT_AVAILABLE",
                    null, null, null, null, null,
                    null, null, null,
                    0, "N/A", null
            );
        }

        InterviewEvaluation latest = data.latestEvaluation().get();
        Integer trend = 0;
        String deltaPercentage = "N/A";

        if (data.previousEvaluation().isPresent()) {
            InterviewEvaluation prev = data.previousEvaluation().get();
            trend = latest.getOverallScore() - prev.getOverallScore();
            if (prev.getOverallScore() > 0) {
                double delta = (trend / (double) prev.getOverallScore()) * 100.0;
                deltaPercentage = String.format("%s%.1f%%", delta >= 0 ? "+" : "", delta);
            } else {
                deltaPercentage = latest.getOverallScore() > 0 ? "+100.0%" : "0.0%";
            }
        }

        return new InterviewCard(
                "AVAILABLE",
                latest.getOverallScore(),
                latest.getTechnicalScore(),
                latest.getCommunicationScore(),
                latest.getProblemSolvingScore(),
                latest.getConfidenceScore(),
                latest.getVerdict().name(),
                latest.getPerformanceBand().name(),
                latest.getEvaluatedAt(),
                trend,
                deltaPercentage,
                latest.getEvaluatedAt()
        );
    }

    private CompanyCard buildCompanyCard(User user, PlacementData data) {
        if (user.getTargetRole() == null || user.getTargetRole().trim().isEmpty()) {
            return new CompanyCard(
                    "NOT_AVAILABLE",
                    null,
                    0,
                    0,
                    "No target role specified. Please update your profile.",
                    user.getUpdatedAt() != null ? user.getUpdatedAt() : LocalDateTime.now()
            );
        }

        int targetCount = data.targetCompanies().size();
        int eligibleCount;
        String summary;

        if (targetCount > 0) {
            eligibleCount = (int) data.targetCompanies().stream()
                    .map(UserTargetCompany::getCompany)
                    .filter(c -> eligibilityService.checkEligibility(user, c).eligible())
                    .count();
            summary = String.format("You are eligible for %d out of %d target companies.", eligibleCount, targetCount);
        } else {
            // Check eligibility against all companies
            int allCount = data.allCompanies().size();
            eligibleCount = (int) data.allCompanies().stream()
                    .filter(c -> eligibilityService.checkEligibility(user, c).eligible())
                    .count();
            summary = String.format("You have not set any target companies. You are eligible for %d out of %d companies in the platform.", eligibleCount, allCount);
        }

        LocalDateTime lastUpdated = user.getUpdatedAt() != null ? user.getUpdatedAt() : user.getCreatedAt();
        if (lastUpdated == null) {
            lastUpdated = LocalDateTime.now();
        }

        return new CompanyCard(
                "AVAILABLE",
                user.getTargetRole(),
                eligibleCount,
                targetCount,
                summary,
                lastUpdated
        );
    }

    private PracticeCard buildPracticeCard(PlacementData data) {
        LocalDateTime lastUpdated = LocalDateTime.now();
        if (data.latestEvaluation().isPresent()) {
            lastUpdated = data.latestEvaluation().get().getEvaluatedAt();
        }

        return new PracticeCard(
                "AVAILABLE",
                data.completedLearningInterviews(),
                data.completedMockInterviews(),
                data.totalInterviews(),
                data.learningQuestionsAttempted(),
                data.mockQuestionsAttempted(),
                lastUpdated
        );
    }

    private QuickActionCard buildQuickActionCard(User user, PlacementData data) {
        List<QuickActionDto> actions = new ArrayList<>();

        // 1. Upload Resume
        boolean resumeExists = data.resume().isPresent();
        actions.add(new QuickActionDto(
                "Upload Resume",
                "/resume/upload",
                "upload",
                resumeExists ? "LOW" : "HIGH",
                resumeExists
        ));

        // 2. Improve Resume (depends on ATS)
        boolean atsPassed = data.resumeAnalysis().isPresent() && data.resumeAnalysis().get().getAtsScore() >= atsThreshold;
        boolean resumeAnalyzed = data.resumeAnalysis().isPresent();
        actions.add(new QuickActionDto(
                "Improve Resume",
                "/resume/analyze",
                "edit",
                (!resumeExists) ? "LOW" : (atsPassed ? "LOW" : "HIGH"),
                atsPassed
        ));

        // 3. Take Learning Interview
        boolean hasLearning = data.completedLearningInterviews() > 0;
        actions.add(new QuickActionDto(
                "Take Learning Interview",
                "/interview/start?mode=LEARNING",
                "play",
                hasLearning ? "LOW" : "HIGH",
                hasLearning
        ));

        // 4. Take Mock Interview
        boolean hasMock = data.completedMockInterviews() > 0;
        boolean latestVerdictNoHire = data.latestEvaluation().isPresent() && data.latestEvaluation().get().getVerdict() == Verdict.NO_HIRE;
        boolean mockCompleted = hasMock && !latestVerdictNoHire;
        actions.add(new QuickActionDto(
                "Take Mock Interview",
                "/interview/start?mode=MOCK",
                "shield",
                (!hasMock || latestVerdictNoHire) ? "HIGH" : "LOW",
                mockCompleted
        ));

        // 5. Complete Profile
        int completion = calculateProfileCompletion(user);
        boolean profileCompleted = completion == 100;
        actions.add(new QuickActionDto(
                "Complete Profile",
                "/profile",
                "user",
                profileCompleted ? "LOW" : "MEDIUM",
                profileCompleted
        ));

        return new QuickActionCard("AVAILABLE", actions, LocalDateTime.now());
    }

    private int calculateProfileCompletion(User user) {
        int filled = 0;
        if (user.getName() != null && !user.getName().trim().isEmpty()) filled++;
        if (user.getCollege() != null && !user.getCollege().trim().isEmpty()) filled++;
        if (user.getDegree() != null && !user.getDegree().trim().isEmpty()) filled++;
        if (user.getBranch() != null && !user.getBranch().trim().isEmpty()) filled++;
        if (user.getCgpa() != null) filled++;
        if (user.getGraduationYear() != null) filled++;
        if (user.getTargetRole() != null && !user.getTargetRole().trim().isEmpty()) filled++;
        if (user.getProjects() != null && !user.getProjects().trim().isEmpty()) filled++;
        if (user.getInternship() != null && !user.getInternship().trim().isEmpty()) filled++;

        return (int) Math.round((filled / 9.0) * 100.0);
    }
}
