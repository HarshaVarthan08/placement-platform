package com.placement.platform.job.recommendation;

import com.placement.platform.entity.User;
import com.placement.platform.exception.ResourceNotFoundException;
import com.placement.platform.job.dto.RecommendationSummaryDto;
import com.placement.platform.job.entity.Job;
import com.placement.platform.job.intelligence.CandidateIntelligenceProfile;
import com.placement.platform.job.intelligence.CandidateIntelligenceProfileBuilder;
import com.placement.platform.job.matching.JobMatchingEngine;
import com.placement.platform.job.matching.RecommendationResult;
import com.placement.platform.job.repository.JobRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final CandidateIntelligenceProfileBuilder profileBuilder;
    private final JobRepository jobRepository;
    private final JobMatchingEngine matchingEngine;
    private final RecommendationBuilder recommendationBuilder;
    private final JobRecommendationRepository jobRecommendationRepository;

    public RecommendationServiceImpl(
            CandidateIntelligenceProfileBuilder profileBuilder,
            JobRepository jobRepository,
            JobMatchingEngine matchingEngine,
            RecommendationBuilder recommendationBuilder,
            JobRecommendationRepository jobRecommendationRepository
    ) {
        this.profileBuilder = profileBuilder;
        this.jobRepository = jobRepository;
        this.matchingEngine = matchingEngine;
        this.recommendationBuilder = recommendationBuilder;
        this.jobRecommendationRepository = jobRecommendationRepository;
    }

    @Override
    @Transactional
    public List<JobRecommendation> generateRecommendations(User user) {
        Long userId = user.getId();

        // 1. Build Candidate Profile once
        CandidateIntelligenceProfile profile = profileBuilder.buildProfile(user);

        // 2. Load active jobs once
        List<Job> activeJobs = jobRepository.findActiveJobs();
        if (activeJobs.isEmpty()) {
            jobRecommendationRepository.deleteByUserId(userId);
            return List.of();
        }

        // 3. Perform all matching in memory (stateless matching engine)
        List<RecommendationResult> matchResults = matchingEngine.evaluateJobs(profile, activeJobs);

        // 4. Fetch existing recommendations for this user to avoid duplicates and perform clean updates
        List<JobRecommendation> existingRecommendations = jobRecommendationRepository.findByUserId(userId);
        Map<Long, JobRecommendation> existingMap = existingRecommendations.stream()
                .collect(Collectors.toMap(JobRecommendation::getJobId, r -> r));

        List<JobRecommendation> toSave = new ArrayList<>();
        Set<Long> activeJobIds = new HashSet<>();

        for (RecommendationResult result : matchResults) {
            Long jobId = result.job().getId();
            activeJobIds.add(jobId);

            JobRecommendation recommendation = existingMap.get(jobId);
            if (recommendation == null) {
                // Create
                recommendation = recommendationBuilder.build(result, userId);
            } else {
                // Update existing
                JobRecommendation newRec = recommendationBuilder.build(result, userId);
                recommendation.setMatchScore(newRec.getMatchScore());
                recommendation.setConfidenceScore(newRec.getConfidenceScore());
                recommendation.setRecommendationLevel(newRec.getRecommendationLevel());
                recommendation.setRecommendationPriority(newRec.getRecommendationPriority());
                recommendation.setRecommendationAction(newRec.getRecommendationAction());
                recommendation.setMatchedSkills(newRec.getMatchedSkills());
                recommendation.setMissingSkills(newRec.getMissingSkills());
                recommendation.setScoreBreakdown(newRec.getScoreBreakdown());
                recommendation.setRecommendationReason(newRec.getRecommendationReason());
                recommendation.setMatchedSkillCount(newRec.getMatchedSkillCount());
                recommendation.setTotalRequiredSkills(newRec.getTotalRequiredSkills());
                recommendation.setSkillMatchPercentage(newRec.getSkillMatchPercentage());
            }
            toSave.add(recommendation);
        }

        // 5. Bulk persist recommendations
        List<JobRecommendation> saved = jobRecommendationRepository.saveAll(toSave);

        // 6. Delete recommendations for jobs that are no longer active/removed
        jobRecommendationRepository.deleteByUserIdAndJobIdNotIn(userId, new ArrayList<>(activeJobIds));

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobRecommendation> getRecommendations(User user, Pageable pageable) {
        return jobRecommendationRepository.findByUserId(user.getId(), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public JobRecommendation getRecommendationDetails(Long id) {
        return jobRecommendationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job recommendation not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public RecommendationSummaryDto getRecommendationSummary(User user) {
        List<JobRecommendation> recs = jobRecommendationRepository.findByUserId(user.getId());
        if (recs.isEmpty()) {
            return new RecommendationSummaryDto(0, 0, 0, 0.0, 0.0, "N/A");
        }

        long total = recs.size();
        long excellent = recs.stream().filter(r -> r.getRecommendationLevel() == RecommendationLevel.EXCELLENT).count();
        long strong = recs.stream().filter(r -> r.getRecommendationLevel() == RecommendationLevel.STRONG).count();

        double avgScore = recs.stream().mapToInt(JobRecommendation::getMatchScore).average().orElse(0.0);
        double avgConf = recs.stream().mapToInt(JobRecommendation::getConfidenceScore).average().orElse(0.0);

        // Find top recommended company
        String topCompany = "N/A";
        JobRecommendation topRec = recs.stream()
                .max(Comparator.comparingInt(JobRecommendation::getMatchScore))
                .orElse(null);

        if (topRec != null) {
            Optional<Job> jobOpt = jobRepository.findById(topRec.getJobId());
            if (jobOpt.isPresent()) {
                topCompany = jobOpt.get().getCompany();
            }
        }

        return new RecommendationSummaryDto(total, excellent, strong, avgScore, avgConf, topCompany);
    }
}
