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
import com.placement.platform.repository.ResumeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final CandidateIntelligenceProfileBuilder profileBuilder;
    private final JobRepository jobRepository;
    private final JobMatchingEngine matchingEngine;
    private final RecommendationBuilder recommendationBuilder;
    private final JobRecommendationRepository jobRecommendationRepository;
    private final ResumeRepository resumeRepository;

    public RecommendationServiceImpl(
            CandidateIntelligenceProfileBuilder profileBuilder,
            JobRepository jobRepository,
            JobMatchingEngine matchingEngine,
            RecommendationBuilder recommendationBuilder,
            JobRecommendationRepository jobRecommendationRepository,
            ResumeRepository resumeRepository
    ) {
        this.profileBuilder = profileBuilder;
        this.jobRepository = jobRepository;
        this.matchingEngine = matchingEngine;
        this.recommendationBuilder = recommendationBuilder;
        this.jobRecommendationRepository = jobRecommendationRepository;
        this.resumeRepository = resumeRepository;
    }

    @Override
    @Transactional
    public List<JobRecommendation> generateRecommendations(User user) {
        return generateRecommendations(user, RecommendationGenerationReason.MANUAL);
    }

    @Override
    @Transactional
    public List<JobRecommendation> generateRecommendations(User user, RecommendationGenerationReason reason) {
        Long userId = user.getId();

        // 1. Build Candidate Profile once
        CandidateIntelligenceProfile profile = profileBuilder.buildProfile(user);

        // 2. Load active jobs once
        List<Job> activeJobs = jobRepository.findActiveJobs();
        if (activeJobs.isEmpty()) {
            return List.of();
        }

        // 3. Perform all matching in memory
        List<RecommendationResult> matchResults = matchingEngine.evaluateJobs(profile, activeJobs);

        // 4. Set up generation metadata
        String generationId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        // 5. Gather snapshots
        Integer atsScoreSnapshot = profile.resumeATSScore();
        Integer readinessScoreSnapshot = profile.placementReadinessScore();
        String resumeVersionSnapshot = resumeRepository.findByUserId(userId)
                .map(r -> r.getId().toString())
                .orElse("NO_RESUME");

        List<JobRecommendation> toSave = new ArrayList<>();

        for (RecommendationResult result : matchResults) {
            Long jobId = result.job().getId();

            // Find the latest previous recommendation for this user and job to carry over state and increment version
            List<JobRecommendation> previousRecs = jobRecommendationRepository
                    .findByUserIdAndJobIdOrderByRecommendationVersionDesc(userId, jobId);

            int version = 1;
            RecommendationStatus status = RecommendationStatus.NEW;
            LocalDateTime viewedAt = null;
            LocalDateTime savedAt = null;
            LocalDateTime appliedAt = null;
            LocalDateTime archivedAt = null;
            LocalDateTime hiddenAt = null;
            int viewCount = 0;
            boolean isHidden = false;

            // Application tracking states
            ApplicationStatus appStatus = null;
            String appUrl = null;
            String appRef = null;
            String appNotes = null;

            // Feedback states
            RecommendationFeedbackType feedbackType = null;
            String feedbackNotes = null;

            if (!previousRecs.isEmpty()) {
                JobRecommendation latestPrev = previousRecs.get(0);
                version = latestPrev.getRecommendationVersion() + 1;
                status = latestPrev.getRecommendationStatus();
                viewedAt = latestPrev.getViewedAt();
                savedAt = latestPrev.getSavedAt();
                appliedAt = latestPrev.getAppliedAt();
                archivedAt = latestPrev.getArchivedAt();
                hiddenAt = latestPrev.getHiddenAt();
                viewCount = latestPrev.getViewCount();
                isHidden = latestPrev.getHidden();

                appStatus = latestPrev.getApplicationStatus();
                appUrl = latestPrev.getApplicationUrl();
                appRef = latestPrev.getApplicationReference();
                appNotes = latestPrev.getApplicationNotes();

                feedbackType = latestPrev.getFeedbackType();
                feedbackNotes = latestPrev.getFeedbackNotes();
            }

            // Build recommendation
            JobRecommendation recommendation = recommendationBuilder.build(result, userId);
            recommendation.setRecommendationStatus(status);
            recommendation.setRecommendationVersion(version);
            recommendation.setGenerationId(generationId);
            recommendation.setGenerationReason(reason);
            recommendation.setViewedAt(viewedAt);
            recommendation.setSavedAt(savedAt);
            recommendation.setAppliedAt(appliedAt);
            recommendation.setArchivedAt(archivedAt);
            recommendation.setHiddenAt(hiddenAt);
            recommendation.setViewCount(viewCount);
            recommendation.setLastRefreshedAt(now);
            recommendation.setHidden(isHidden);

            // Set immutable snapshots
            recommendation.setAtsScore(atsScoreSnapshot);
            recommendation.setReadinessScore(readinessScoreSnapshot);
            recommendation.setResumeVersion(resumeVersionSnapshot);

            // Set application details
            recommendation.setApplicationStatus(appStatus);
            recommendation.setApplicationUrl(appUrl);
            recommendation.setApplicationReference(appRef);
            recommendation.setApplicationNotes(appNotes);

            // Set feedback details
            recommendation.setFeedbackType(feedbackType);
            recommendation.setFeedbackNotes(feedbackNotes);

            toSave.add(recommendation);
        }

        // 6. Bulk persist new generation (no deletions of older generations)
        return jobRecommendationRepository.saveAll(toSave);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobRecommendation> getRecommendations(User user, Pageable pageable) {
        Optional<String> latestGenOpt = jobRecommendationRepository.findLatestGenerationId(user.getId());
        if (latestGenOpt.isEmpty()) {
            return Page.empty();
        }
        return jobRecommendationRepository.findByUserIdAndGenerationIdAndHiddenFalse(user.getId(), latestGenOpt.get(), pageable);
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
        Optional<String> latestGenOpt = jobRecommendationRepository.findLatestGenerationId(user.getId());
        if (latestGenOpt.isEmpty()) {
            return new RecommendationSummaryDto(0, 0, 0, 0.0, 0.0, "N/A");
        }

        List<JobRecommendation> recs = jobRecommendationRepository.findByUserIdAndGenerationId(user.getId(), latestGenOpt.get());
        if (recs.isEmpty()) {
            return new RecommendationSummaryDto(0, 0, 0, 0.0, 0.0, "N/A");
        }

        long total = recs.size();
        long excellent = recs.stream().filter(r -> r.getRecommendationLevel() == RecommendationLevel.EXCELLENT).count();
        long strong = recs.stream().filter(r -> r.getRecommendationLevel() == RecommendationLevel.STRONG).count();

        double avgScore = recs.stream().mapToInt(JobRecommendation::getMatchScore).average().orElse(0.0);
        double avgConf = recs.stream().mapToInt(JobRecommendation::getConfidenceScore).average().orElse(0.0);

        // Find top recommended company from active jobs
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
