package com.placement.platform.service.dashboard;

import com.placement.platform.dto.dashboard.PlacementReadinessCard;
import com.placement.platform.dto.dashboard.ReadinessBreakdownDto;
import com.placement.platform.entity.*;
import com.placement.platform.service.EligibilityService;
import com.placement.platform.service.dashboard.PlacementDataAggregator.PlacementData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReadinessServiceImpl implements ReadinessService {

    private final EligibilityService eligibilityService;

    @Value("${application.dashboard.readiness.resume-weight:30}")
    private int resumeWeight;

    @Value("${application.dashboard.readiness.interview-weight:35}")
    private int interviewWeight;

    @Value("${application.dashboard.readiness.profile-weight:15}")
    private int profileWeight;

    @Value("${application.dashboard.readiness.skills-weight:10}")
    private int skillsWeight;

    @Value("${application.dashboard.readiness.company-weight:10}")
    private int companyWeight;

    public ReadinessServiceImpl(EligibilityService eligibilityService) {
        this.eligibilityService = eligibilityService;
    }

    @Override
    public PlacementReadinessCard calculateReadiness(User user, PlacementData data) {
        // Calculate raw components
        Integer resumeScore = calculateResumeScore(data);
        Integer interviewScore = calculateInterviewScore(data.latestEvaluation());
        Integer profileScore = calculateProfileScore(user);
        Integer skillScore = calculateSkillScore(data);
        Integer companyScore = calculateCompanyScore(user, data);

        // Calculate current readiness
        int currentScore = calculateWeightedScore(
                resumeScore, interviewScore, profileScore, skillScore, companyScore
        );

        // Calculate previous readiness if possible (use previous interview score)
        Integer prevInterviewScore = calculateInterviewScore(data.previousEvaluation());
        int previousScore = calculateWeightedScore(
                resumeScore, prevInterviewScore, profileScore, skillScore, companyScore
        );

        // Trend and delta
        int trend = currentScore - previousScore;
        String deltaPercentage;
        if (data.previousEvaluation().isPresent()) {
            if (previousScore > 0) {
                double deltaVal = ((currentScore - previousScore) / (double) previousScore) * 100.0;
                deltaPercentage = String.format("%s%.1f%%", deltaVal >= 0 ? "+" : "", deltaVal);
            } else {
                deltaPercentage = currentScore > 0 ? "+100.0%" : "0.0%";
            }
        } else {
            deltaPercentage = "N/A";
        }

        String readinessBand = resolveReadinessBand(currentScore);
        ReadinessBreakdownDto breakdown = buildBreakdown(
                resumeScore, interviewScore, profileScore, skillScore, companyScore
        );

        String explanation = String.format(
                "Resume ATS: %s x %d%%, Interview: %s x %d%%, Profile: %d x %d%%, Skills: %d x %d%%, Eligibility: %d x %d%% (Normalized based on available data)",
                resumeScore != null ? resumeScore.toString() : "N/A", resumeWeight,
                interviewScore != null ? interviewScore.toString() : "N/A", interviewWeight,
                profileScore, profileWeight,
                skillScore, skillsWeight,
                companyScore, companyWeight
        );

        // Expose lastUpdated timestamp (maximum of updated timestamps of available sections)
        LocalDateTime lastUpdated = LocalDateTime.now();
        if (data.resumeAnalysis().isPresent()) {
            lastUpdated = data.resumeAnalysis().get().getUpdatedAt();
        }
        if (data.latestEvaluation().isPresent() && data.latestEvaluation().get().getEvaluatedAt().isAfter(lastUpdated)) {
            lastUpdated = data.latestEvaluation().get().getEvaluatedAt();
        }
        if (user.getUpdatedAt() != null && user.getUpdatedAt().isAfter(lastUpdated)) {
            lastUpdated = user.getUpdatedAt();
        }

        String status = (resumeScore == null && interviewScore == null) ? "NOT_AVAILABLE" : "AVAILABLE";

        return new PlacementReadinessCard(
                status,
                currentScore,
                readinessBand,
                breakdown,
                explanation,
                trend,
                deltaPercentage,
                lastUpdated
        );
    }

    private Integer calculateResumeScore(PlacementData data) {
        return data.resumeAnalysis().map(ResumeAnalysis::getAtsScore).orElse(null);
    }

    private Integer calculateInterviewScore(java.util.Optional<InterviewEvaluation> evaluation) {
        return evaluation.map(InterviewEvaluation::getOverallScore).orElse(null);
    }

    private Integer calculateProfileScore(User user) {
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

    private Integer calculateSkillScore(PlacementData data) {
        // Collect target company unique required skills
        Set<String> requiredSkillNames = new HashSet<>();
        for (UserTargetCompany utc : data.targetCompanies()) {
            TargetCompany tc = utc.getCompany();
            if (tc != null && tc.getRequiredSkills() != null) {
                for (Skill skill : tc.getRequiredSkills()) {
                    if (skill.getName() != null) {
                        requiredSkillNames.add(skill.getName().trim().toLowerCase());
                    }
                }
            }
        }

        List<String> userSkillNames = data.userSkills().stream()
                .map(us -> us.getSkill().getName().trim().toLowerCase())
                .collect(Collectors.toList());

        if (!requiredSkillNames.isEmpty()) {
            long matched = requiredSkillNames.stream()
                    .filter(userSkillNames::contains)
                    .count();
            return (int) Math.round((matched / (double) requiredSkillNames.size()) * 100.0);
        }

        // Fallback: ratio of user skills to (user skills + missing resume skills)
        int userSkillsSize = userSkillNames.size();
        int missingSkillsSize = data.resumeAnalysis()
                .map(ra -> ra.getMissingSkills() != null ? ra.getMissingSkills().size() : 0)
                .orElse(0);

        if (userSkillsSize + missingSkillsSize > 0) {
            return (int) Math.round((userSkillsSize / (double) (userSkillsSize + missingSkillsSize)) * 100.0);
        }

        return userSkillsSize > 0 ? 100 : 0;
    }

    private Integer calculateCompanyScore(User user, PlacementData data) {
        if (!data.targetCompanies().isEmpty()) {
            long eligibleCount = data.targetCompanies().stream()
                    .map(UserTargetCompany::getCompany)
                    .filter(company -> eligibilityService.checkEligibility(user, company).eligible())
                    .count();
            return (int) Math.round((eligibleCount / (double) data.targetCompanies().size()) * 100.0);
        }

        // Fallback: check eligibility against all companies in database
        if (!data.allCompanies().isEmpty()) {
            long eligibleCount = data.allCompanies().stream()
                    .filter(company -> eligibilityService.checkEligibility(user, company).eligible())
                    .count();
            return (int) Math.round((eligibleCount / (double) data.allCompanies().size()) * 100.0);
        }

        return 0;
    }

    private int calculateWeightedScore(
            Integer resume, Integer interview, int profile, int skills, int company
    ) {
        double weightedSum = 0.0;
        double totalWeight = 0.0;

        if (resume != null) {
            weightedSum += resume * resumeWeight;
            totalWeight += resumeWeight;
        }
        if (interview != null) {
            weightedSum += interview * interviewWeight;
            totalWeight += interviewWeight;
        }

        weightedSum += profile * profileWeight;
        totalWeight += profileWeight;

        weightedSum += skills * skillsWeight;
        totalWeight += skillsWeight;

        weightedSum += company * companyWeight;
        totalWeight += companyWeight;

        if (totalWeight > 0) {
            return (int) Math.round((weightedSum / totalWeight));
        }
        return 0;
    }

    private String resolveReadinessBand(int score) {
        if (score >= 90) return "EXCELLENT";
        if (score >= 75) return "GOOD";
        if (score >= 60) return "AVERAGE";
        return "NEEDS_IMPROVEMENT";
    }

    private ReadinessBreakdownDto buildBreakdown(
            Integer resume, Integer interview, int profile, int skills, int company
    ) {
        double totalWeight = 0.0;
        if (resume != null) totalWeight += resumeWeight;
        if (interview != null) totalWeight += interviewWeight;
        totalWeight += profileWeight + skillsWeight + companyWeight;

        double rCont = resume != null ? (resume * resumeWeight) / totalWeight : 0.0;
        double iCont = interview != null ? (interview * interviewWeight) / totalWeight : 0.0;
        double pCont = (profile * profileWeight) / totalWeight;
        double sCont = (skills * skillsWeight) / totalWeight;
        double cCont = (company * companyWeight) / totalWeight;

        return new ReadinessBreakdownDto(
                resume != null ? resume : 0,
                interview != null ? interview : 0,
                profile,
                skills,
                company,
                resumeWeight,
                interviewWeight,
                profileWeight,
                skillsWeight,
                companyWeight,
                Math.round(rCont * 100.0) / 100.0,
                Math.round(iCont * 100.0) / 100.0,
                Math.round(pCont * 100.0) / 100.0,
                Math.round(sCont * 100.0) / 100.0,
                Math.round(cCont * 100.0) / 100.0
        );
    }
}
