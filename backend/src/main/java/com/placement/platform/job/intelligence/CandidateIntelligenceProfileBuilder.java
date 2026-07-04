package com.placement.platform.job.intelligence;

import com.placement.platform.entity.*;
import com.placement.platform.repository.*;
import com.placement.platform.service.EligibilityService;
import com.placement.platform.service.dashboard.PlacementDataAggregator;
import com.placement.platform.service.dashboard.PlacementDataAggregator.PlacementData;
import com.placement.platform.service.dashboard.ReadinessService;
import com.placement.platform.dto.dashboard.PlacementReadinessCard;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CandidateIntelligenceProfileBuilder {

    private final ResumeRepository resumeRepository;
    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final UserSkillRepository userSkillRepository;
    private final UserTargetCompanyRepository userTargetCompanyRepository;
    private final InterviewEvaluationRepository interviewEvaluationRepository;
    private final TargetCompanyRepository targetCompanyRepository;
    private final EligibilityService eligibilityService;
    private final PlacementDataAggregator placementDataAggregator;
    private final ReadinessService readinessService;

    public CandidateIntelligenceProfileBuilder(
            ResumeRepository resumeRepository,
            ResumeAnalysisRepository resumeAnalysisRepository,
            UserSkillRepository userSkillRepository,
            UserTargetCompanyRepository userTargetCompanyRepository,
            InterviewEvaluationRepository interviewEvaluationRepository,
            TargetCompanyRepository targetCompanyRepository,
            EligibilityService eligibilityService,
            PlacementDataAggregator placementDataAggregator,
            ReadinessService readinessService
    ) {
        this.resumeRepository = resumeRepository;
        this.resumeAnalysisRepository = resumeAnalysisRepository;
        this.userSkillRepository = userSkillRepository;
        this.userTargetCompanyRepository = userTargetCompanyRepository;
        this.interviewEvaluationRepository = interviewEvaluationRepository;
        this.targetCompanyRepository = targetCompanyRepository;
        this.eligibilityService = eligibilityService;
        this.placementDataAggregator = placementDataAggregator;
        this.readinessService = readinessService;
    }

    @Transactional(readOnly = true)
    public CandidateIntelligenceProfile buildProfile(User user) {
        Long userId = user.getId();

        // 1. Profile Module - Roles
        List<String> preferredRoles = new ArrayList<>();
        if (user.getTargetRole() != null && !user.getTargetRole().isBlank()) {
            preferredRoles.add(user.getTargetRole().trim());
        }

        // 2. Profile Module - Target Companies
        List<String> preferredCompanies = userTargetCompanyRepository.findByUserId(userId)
                .stream()
                .map(utc -> utc.getCompany().getName())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 3. Profile Module - Skills
        List<String> skills = userSkillRepository.findByUserId(userId)
                .stream()
                .map(us -> us.getSkill().getName())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 4. Profile Module - Education
        StringBuilder eduBuilder = new StringBuilder();
        if (user.getDegree() != null && !user.getDegree().isBlank()) {
            eduBuilder.append(user.getDegree().trim());
        }
        if (user.getBranch() != null && !user.getBranch().isBlank()) {
            if (eduBuilder.length() > 0) eduBuilder.append(" in ");
            eduBuilder.append(user.getBranch().trim());
        }
        if (user.getCollege() != null && !user.getCollege().isBlank()) {
            if (eduBuilder.length() > 0) eduBuilder.append(" from ");
            eduBuilder.append(user.getCollege().trim());
        }
        String education = eduBuilder.toString();

        // 5. Profile Module - Experience (years)
        // Heuristic: Check if user has projects/internship text and set base experience
        int experience = 0;
        if (user.getInternship() != null && !user.getInternship().isBlank()) {
            experience += 1; // Basic assumption of experience if internship is listed
        }

        // 6. Resume Module - Resume Analysis
        Integer resumeATSScore = null;
        List<String> resumeExtractedSkills = new ArrayList<>();
        Optional<Resume> resumeOpt = resumeRepository.findByUserId(userId);
        if (resumeOpt.isPresent()) {
            Optional<ResumeAnalysis> analysisOpt = resumeAnalysisRepository.findByResume(resumeOpt.get());
            if (analysisOpt.isPresent()) {
                ResumeAnalysis analysis = analysisOpt.get();
                resumeATSScore = analysis.getAtsScore();
                // Extracted skills from resume: We can assume verified skills as extracted,
                // or if the resume text extraction has loaded them.
                // We'll populate it with user skills as baseline.
                resumeExtractedSkills.addAll(skills);
            }
        }

        // 7. Interview Module - Latest Evaluation
        Integer interviewTechnicalScore = null;
        Integer interviewCommunicationScore = null;
        Integer interviewConfidenceScore = null;
        List<InterviewEvaluation> latestEvaluations = interviewEvaluationRepository.findLatestEvaluationsByUserId(userId, PageRequest.of(0, 1));
        if (!latestEvaluations.isEmpty()) {
            InterviewEvaluation eval = latestEvaluations.get(0);
            interviewTechnicalScore = eval.getTechnicalScore();
            interviewCommunicationScore = eval.getCommunicationScore();
            interviewConfidenceScore = eval.getConfidenceScore();
        }

        // 8. Placement Dashboard - Readiness Score
        Integer placementReadinessScore = null;
        try {
            PlacementData placementData = placementDataAggregator.aggregate(user);
            PlacementReadinessCard readinessCard = readinessService.calculateReadiness(user, placementData);
            if (readinessCard != null) {
                placementReadinessScore = readinessCard.readinessScore();
            }
        } catch (Exception e) {
            // Fallback if aggregation fails
            placementReadinessScore = 0;
        }

        // 9. Eligibility Module - Eligible Companies
        List<String> eligibleCompanies = new ArrayList<>();
        List<TargetCompany> allCompanies = targetCompanyRepository.findAll();
        for (TargetCompany company : allCompanies) {
            try {
                if (eligibilityService.checkEligibility(user, company).eligible()) {
                    eligibleCompanies.add(company.getName());
                }
            } catch (Exception e) {
                // Ignore single company checking failure
            }
        }

        // 10. Preferred Locations
        // Default to empty list (open to relocate / work anywhere)
        List<String> preferredLocations = new ArrayList<>();

        return new CandidateIntelligenceProfile(
                userId,
                preferredRoles,
                preferredCompanies,
                skills,
                experience,
                education,
                user.getCgpa(),
                resumeATSScore,
                resumeExtractedSkills,
                interviewTechnicalScore,
                interviewCommunicationScore,
                interviewConfidenceScore,
                placementReadinessScore,
                eligibleCompanies,
                preferredLocations
        );
    }
}
