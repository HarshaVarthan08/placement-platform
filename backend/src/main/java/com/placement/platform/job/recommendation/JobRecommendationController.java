package com.placement.platform.job.recommendation;

import com.placement.platform.entity.User;
import com.placement.platform.exception.UserNotFoundException;
import com.placement.platform.job.dto.*;
import com.placement.platform.job.entity.Job;
import com.placement.platform.job.repository.JobRepository;
import com.placement.platform.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs/recommendations")
public class JobRecommendationController {

    private final RecommendationService recommendationService;
    private final RecommendationLifecycleService lifecycleService;
    private final ApplicationTrackingService applicationTrackingService;
    private final RecommendationAnalyticsService analyticsService;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final JobRecommendationRepository jobRecommendationRepository;

    public JobRecommendationController(
            RecommendationService recommendationService,
            RecommendationLifecycleService lifecycleService,
            ApplicationTrackingService applicationTrackingService,
            RecommendationAnalyticsService analyticsService,
            UserRepository userRepository,
            JobRepository jobRepository,
            JobRecommendationRepository jobRecommendationRepository
    ) {
        this.recommendationService = recommendationService;
        this.lifecycleService = lifecycleService;
        this.applicationTrackingService = applicationTrackingService;
        this.analyticsService = analyticsService;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.jobRecommendationRepository = jobRecommendationRepository;
    }

    @PostMapping("/generate")
    public ResponseEntity<List<JobRecommendationResponseDto>> generateRecommendations() {
        User user = getAuthenticatedUser();
        List<JobRecommendation> recommendations = recommendationService.generateRecommendations(user);

        List<Long> jobIds = recommendations.stream()
                .map(JobRecommendation::getJobId)
                .collect(Collectors.toList());
        List<Job> jobs = jobRepository.findAllById(jobIds);
        Map<Long, Job> jobMap = jobs.stream()
                .collect(Collectors.toMap(Job::getId, j -> j, (a, b) -> a));

        List<JobRecommendationResponseDto> response = recommendations.stream()
                .map(rec -> mapToResponseDto(rec, Optional.ofNullable(jobMap.get(rec.getJobId()))))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<List<JobRecommendationResponseDto>> refreshRecommendations() {
        User user = getAuthenticatedUser();
        List<JobRecommendation> recommendations = recommendationService.generateRecommendations(user, RecommendationGenerationReason.MANUAL);

        List<Long> jobIds = recommendations.stream()
                .map(JobRecommendation::getJobId)
                .collect(Collectors.toList());
        List<Job> jobs = jobRepository.findAllById(jobIds);
        Map<Long, Job> jobMap = jobs.stream()
                .collect(Collectors.toMap(Job::getId, j -> j, (a, b) -> a));

        List<JobRecommendationResponseDto> response = recommendations.stream()
                .map(rec -> mapToResponseDto(rec, Optional.ofNullable(jobMap.get(rec.getJobId()))))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<JobRecommendationResponseDto>> getRecommendations(Pageable pageable) {
        User user = getAuthenticatedUser();
        Page<JobRecommendation> recommendationsPage = recommendationService.getRecommendations(user, pageable);

        List<Long> jobIds = recommendationsPage.getContent().stream()
                .map(JobRecommendation::getJobId)
                .collect(Collectors.toList());
        List<Job> jobs = jobRepository.findAllById(jobIds);
        Map<Long, Job> jobMap = jobs.stream()
                .collect(Collectors.toMap(Job::getId, j -> j, (a, b) -> a));

        List<JobRecommendationResponseDto> dtos = recommendationsPage.getContent().stream()
                .map(rec -> mapToResponseDto(rec, Optional.ofNullable(jobMap.get(rec.getJobId()))))
                .collect(Collectors.toList());

        Page<JobRecommendationResponseDto> responsePage = new PageImpl<>(dtos, pageable, recommendationsPage.getTotalElements());
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobRecommendationResponseDto> getRecommendationDetails(@PathVariable Long id) {
        JobRecommendation recommendation = recommendationService.getRecommendationDetails(id);
        Optional<Job> jobOpt = jobRepository.findById(recommendation.getJobId());
        JobRecommendationResponseDto response = mapToResponseDto(recommendation, jobOpt);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    public ResponseEntity<RecommendationSummaryDto> getRecommendationSummary() {
        User user = getAuthenticatedUser();
        RecommendationSummaryDto summary = recommendationService.getRecommendationSummary(user);
        return ResponseEntity.ok(summary);
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<JobRecommendationResponseDto> markViewed(@PathVariable Long id) {
        JobRecommendation rec = lifecycleService.markViewed(id);
        Optional<Job> jobOpt = jobRepository.findById(rec.getJobId());
        return ResponseEntity.ok(mapToResponseDto(rec, jobOpt));
    }

    @PostMapping("/{id}/save")
    public ResponseEntity<JobRecommendationResponseDto> saveRecommendation(@PathVariable Long id) {
        JobRecommendation rec = lifecycleService.saveRecommendation(id);
        Optional<Job> jobOpt = jobRepository.findById(rec.getJobId());
        return ResponseEntity.ok(mapToResponseDto(rec, jobOpt));
    }

    @PostMapping("/{id}/unsave")
    public ResponseEntity<JobRecommendationResponseDto> unsaveRecommendation(@PathVariable Long id) {
        JobRecommendation rec = lifecycleService.unsaveRecommendation(id);
        Optional<Job> jobOpt = jobRepository.findById(rec.getJobId());
        return ResponseEntity.ok(mapToResponseDto(rec, jobOpt));
    }

    @PostMapping("/{id}/hide")
    public ResponseEntity<JobRecommendationResponseDto> hideRecommendation(@PathVariable Long id) {
        JobRecommendation rec = lifecycleService.hideRecommendation(id);
        Optional<Job> jobOpt = jobRepository.findById(rec.getJobId());
        return ResponseEntity.ok(mapToResponseDto(rec, jobOpt));
    }

    @PostMapping("/{id}/archive")
    public ResponseEntity<JobRecommendationResponseDto> archiveRecommendation(@PathVariable Long id) {
        JobRecommendation rec = lifecycleService.archiveRecommendation(id);
        Optional<Job> jobOpt = jobRepository.findById(rec.getJobId());
        return ResponseEntity.ok(mapToResponseDto(rec, jobOpt));
    }

    @PostMapping("/{id}/feedback")
    public ResponseEntity<JobRecommendationResponseDto> submitFeedback(
            @PathVariable Long id,
            @Valid @RequestBody FeedbackRequestDto request) {
        JobRecommendation rec = lifecycleService.submitFeedback(id, request.getFeedbackType(), request.getFeedbackNotes());
        Optional<Job> jobOpt = jobRepository.findById(rec.getJobId());
        return ResponseEntity.ok(mapToResponseDto(rec, jobOpt));
    }

    @PostMapping("/{id}/apply")
    public ResponseEntity<JobRecommendationResponseDto> applyForRecommendation(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationRequestDto request) {
        JobRecommendation rec = applicationTrackingService.applyForRecommendation(
                id, request.getApplicationUrl(), request.getApplicationReference(), request.getApplicationNotes());
        Optional<Job> jobOpt = jobRepository.findById(rec.getJobId());
        return ResponseEntity.ok(mapToResponseDto(rec, jobOpt));
    }

    @PutMapping("/{id}/application")
    public ResponseEntity<JobRecommendationResponseDto> updateApplicationStage(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationUpdateRequestDto request) {
        JobRecommendation rec = applicationTrackingService.updateApplicationStage(id, request.getApplicationStatus(), request.getApplicationNotes());
        Optional<Job> jobOpt = jobRepository.findById(rec.getJobId());
        return ResponseEntity.ok(mapToResponseDto(rec, jobOpt));
    }

    @GetMapping("/applied")
    public ResponseEntity<Page<AppliedRecommendationResponseDto>> getAppliedRecommendations(Pageable pageable) {
        User user = getAuthenticatedUser();
        Page<JobRecommendation> page = applicationTrackingService.getAppliedRecommendations(user, pageable);

        List<Long> jobIds = page.getContent().stream().map(JobRecommendation::getJobId).collect(Collectors.toList());
        List<Job> jobs = jobRepository.findAllById(jobIds);
        Map<Long, Job> jobMap = jobs.stream()
                .collect(Collectors.toMap(Job::getId, j -> j, (a, b) -> a));

        List<AppliedRecommendationResponseDto> dtos = page.getContent().stream()
                .map(rec -> mapToAppliedDto(rec, Optional.ofNullable(jobMap.get(rec.getJobId()))))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new PageImpl<>(dtos, pageable, page.getTotalElements()));
    }

    @GetMapping("/saved")
    public ResponseEntity<Page<SavedRecommendationResponseDto>> getSavedRecommendations(Pageable pageable) {
        User user = getAuthenticatedUser();
        Page<JobRecommendation> page = jobRecommendationRepository
                .findByUserIdAndRecommendationStatusAndHiddenFalse(user.getId(), RecommendationStatus.SAVED, pageable);

        List<Long> jobIds = page.getContent().stream().map(JobRecommendation::getJobId).collect(Collectors.toList());
        List<Job> jobs = jobRepository.findAllById(jobIds);
        Map<Long, Job> jobMap = jobs.stream()
                .collect(Collectors.toMap(Job::getId, j -> j, (a, b) -> a));

        List<SavedRecommendationResponseDto> dtos = page.getContent().stream()
                .map(rec -> mapToSavedDto(rec, Optional.ofNullable(jobMap.get(rec.getJobId()))))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new PageImpl<>(dtos, pageable, page.getTotalElements()));
    }

    @GetMapping("/analytics")
    public ResponseEntity<RecommendationAnalyticsResponseDto> getAnalytics(
            @RequestParam(value = "includeHistory", defaultValue = "false") boolean includeHistory) {
        User user = getAuthenticatedUser();
        RecommendationAnalyticsResponseDto analytics = analyticsService.getAnalytics(user, includeHistory);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/history")
    public ResponseEntity<List<RecommendationHistoryResponseDto>> getHistory() {
        User user = getAuthenticatedUser();
        List<JobRecommendation> allRecs = jobRecommendationRepository.findByUserId(user.getId());

        // Group by generation ID
        Map<String, List<JobRecommendation>> grouped = allRecs.stream()
                .filter(r -> r.getGenerationId() != null)
                .collect(Collectors.groupingBy(JobRecommendation::getGenerationId));

        List<Long> jobIds = allRecs.stream().map(JobRecommendation::getJobId).distinct().collect(Collectors.toList());
        List<Job> jobs = jobRepository.findAllById(jobIds);
        Map<Long, Job> jobMap = jobs.stream()
                .collect(Collectors.toMap(Job::getId, j -> j, (a, b) -> a));

        List<RecommendationHistoryResponseDto> historyList = grouped.entrySet().stream()
                .map(entry -> {
                    String genId = entry.getKey();
                    List<JobRecommendation> list = entry.getValue();
                    LocalDateTime generatedAt = list.isEmpty() ? LocalDateTime.now() : list.get(0).getGeneratedAt();
                    RecommendationGenerationReason reason = list.isEmpty() ? RecommendationGenerationReason.MANUAL : list.get(0).getGenerationReason();

                    RecommendationHistoryResponseDto histDto = new RecommendationHistoryResponseDto();
                    histDto.setGenerationId(genId);
                    histDto.setGeneratedAt(generatedAt);
                    histDto.setGenerationReason(reason);
                    histDto.setRecommendationsCount(list.size());

                    List<JobRecommendationResponseDto> recDtos = list.stream()
                            .map(rec -> mapToResponseDto(rec, Optional.ofNullable(jobMap.get(rec.getJobId()))))
                            .collect(Collectors.toList());
                    histDto.setRecommendations(recDtos);

                    return histDto;
                })
                .sorted(Comparator.comparing(RecommendationHistoryResponseDto::getGeneratedAt).reversed())
                .collect(Collectors.toList());

        return ResponseEntity.ok(historyList);
    }

    @GetMapping("/{id}/timeline")
    public ResponseEntity<RecommendationTimelineResponseDto> getTimeline(@PathVariable Long id) {
        JobRecommendation rec = recommendationService.getRecommendationDetails(id);
        Optional<Job> jobOpt = jobRepository.findById(rec.getJobId());
        RecommendationTimelineResponseDto timeline = getTimelineDto(rec, jobOpt.orElse(null));
        return ResponseEntity.ok(timeline);
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

    private SavedRecommendationResponseDto mapToSavedDto(JobRecommendation rec, Optional<Job> jobOpt) {
        SavedRecommendationResponseDto dto = new SavedRecommendationResponseDto();
        dto.setRecommendationId(rec.getId());
        dto.setJobId(rec.getJobId());
        dto.setCompany(jobOpt.map(Job::getCompany).orElse("Unknown"));
        dto.setTitle(jobOpt.map(Job::getTitle).orElse("Unknown"));
        dto.setMatchScore(rec.getMatchScore());
        dto.setSavedAt(rec.getSavedAt());
        return dto;
    }

    private AppliedRecommendationResponseDto mapToAppliedDto(JobRecommendation rec, Optional<Job> jobOpt) {
        AppliedRecommendationResponseDto dto = new AppliedRecommendationResponseDto();
        dto.setRecommendationId(rec.getId());
        dto.setJobId(rec.getJobId());
        dto.setCompany(jobOpt.map(Job::getCompany).orElse("Unknown"));
        dto.setTitle(jobOpt.map(Job::getTitle).orElse("Unknown"));
        dto.setMatchScore(rec.getMatchScore());
        dto.setAppliedAt(rec.getAppliedAt());
        dto.setApplicationStatus(rec.getApplicationStatus());
        dto.setApplicationReference(rec.getApplicationReference());
        return dto;
    }

    private RecommendationTimelineResponseDto getTimelineDto(JobRecommendation rec, Job job) {
        RecommendationTimelineResponseDto dto = new RecommendationTimelineResponseDto();
        dto.setRecommendationId(rec.getId());
        dto.setJobId(rec.getJobId());
        dto.setCompany(job != null ? job.getCompany() : "Unknown");
        dto.setTitle(job != null ? job.getTitle() : "Unknown");

        List<RecommendationTimelineEvent> events = new ArrayList<>();

        // 1. Generation Event
        events.add(new RecommendationTimelineEvent(
                "GENERATED",
                rec.getGeneratedAt(),
                "Recommendation Generated",
                "Job recommendation generated using reason: " + rec.getGenerationReason() + 
                " (Match Score: " + rec.getMatchScore() + "%, Confidence: " + rec.getConfidenceScore() + "%)"
        ));

        // 2. Viewed Event
        if (rec.getViewedAt() != null) {
            events.add(new RecommendationTimelineEvent(
                    "VIEWED",
                    rec.getViewedAt(),
                    "Recommendation Viewed",
                    "Candidate viewed this job recommendation. Total views: " + rec.getViewCount()
            ));
        }

        // 3. Saved Event
        if (rec.getSavedAt() != null) {
            events.add(new RecommendationTimelineEvent(
                    "SAVED",
                    rec.getSavedAt(),
                    "Recommendation Saved",
                    "Job recommendation saved for quick access."
            ));
        }

        // 4. Hidden Event
        if (rec.getHiddenAt() != null) {
            events.add(new RecommendationTimelineEvent(
                    "HIDDEN",
                    rec.getHiddenAt(),
                    "Recommendation Hidden",
                    "Recommendation was hidden from the active list."
            ));
        }

        // 5. Applied Event
        if (rec.getAppliedAt() != null) {
            String details = "Applied for job.";
            if (rec.getApplicationReference() != null && !rec.getApplicationReference().isBlank()) {
                details += " Reference: " + rec.getApplicationReference() + ".";
            }
            if (rec.getApplicationUrl() != null && !rec.getApplicationUrl().isBlank()) {
                details += " External Link: " + rec.getApplicationUrl();
            }
            events.add(new RecommendationTimelineEvent(
                    "APPLIED",
                    rec.getAppliedAt(),
                    "Job Application Submitted",
                    details
            ));
        }

        // 6. Application Stage Progress
        if (rec.getApplicationStatus() != null && rec.getApplicationStatus() != ApplicationStatus.APPLIED) {
            events.add(new RecommendationTimelineEvent(
                    "STAGE_CHANGE",
                    rec.getUpdatedAt() != null ? rec.getUpdatedAt() : LocalDateTime.now(),
                    "Application Stage: " + rec.getApplicationStatus(),
                    "Application progressed to " + rec.getApplicationStatus() + 
                    (rec.getApplicationNotes() != null ? ". Notes: " + rec.getApplicationNotes() : "")
            ));
        }

        // 7. Archived Event
        if (rec.getArchivedAt() != null) {
            events.add(new RecommendationTimelineEvent(
                    "ARCHIVED",
                    rec.getArchivedAt(),
                    "Recommendation Archived",
                    "Recommendation moved to archives."
            ));
        }

        // 8. Feedback Event
        if (rec.getFeedbackType() != null) {
            events.add(new RecommendationTimelineEvent(
                    "FEEDBACK",
                    rec.getUpdatedAt() != null ? rec.getUpdatedAt() : LocalDateTime.now(),
                    "Feedback Submitted",
                    "Type: " + rec.getFeedbackType() + 
                    (rec.getFeedbackNotes() != null ? ". Notes: " + rec.getFeedbackNotes() : "")
            ));
        }

        // Sort events chronologically (by timestamp ascending)
        events.sort(Comparator.comparing(RecommendationTimelineEvent::getTimestamp));
        dto.setEvents(events);

        return dto;
    }

    private JobRecommendationResponseDto mapToResponseDto(JobRecommendation recommendation, Optional<Job> jobOpt) {
        JobRecommendationResponseDto dto = new JobRecommendationResponseDto();
        dto.setId(recommendation.getId());
        dto.setUserId(recommendation.getUserId());
        dto.setMatchScore(recommendation.getMatchScore());
        dto.setConfidenceScore(recommendation.getConfidenceScore());
        dto.setRecommendationLevel(recommendation.getRecommendationLevel());
        dto.setRecommendationPriority(recommendation.getRecommendationPriority());
        dto.setRecommendationAction(recommendation.getRecommendationAction());
        dto.setMatchedSkills(recommendation.getMatchedSkills());
        dto.setMissingSkills(recommendation.getMissingSkills());
        dto.setScoreBreakdown(recommendation.getScoreBreakdown());
        dto.setRecommendationReason(recommendation.getRecommendationReason());
        dto.setMatchedSkillCount(recommendation.getMatchedSkillCount());
        dto.setTotalRequiredSkills(recommendation.getTotalRequiredSkills());
        dto.setSkillMatchPercentage(recommendation.getSkillMatchPercentage());
        dto.setGeneratedAt(recommendation.getGeneratedAt());
        dto.setUpdatedAt(recommendation.getUpdatedAt());

        // New fields
        dto.setRecommendationStatus(recommendation.getRecommendationStatus());
        dto.setViewedAt(recommendation.getViewedAt());
        dto.setSavedAt(recommendation.getSavedAt());
        dto.setAppliedAt(recommendation.getAppliedAt());
        dto.setArchivedAt(recommendation.getArchivedAt());
        dto.setHiddenAt(recommendation.getHiddenAt());
        dto.setViewCount(recommendation.getViewCount());
        dto.setRecommendationVersion(recommendation.getRecommendationVersion());
        dto.setGenerationId(recommendation.getGenerationId());
        dto.setGenerationReason(recommendation.getGenerationReason());
        dto.setLastRefreshedAt(recommendation.getLastRefreshedAt());
        dto.setHidden(recommendation.getHidden());
        dto.setAtsScore(recommendation.getAtsScore());
        dto.setReadinessScore(recommendation.getReadinessScore());
        dto.setResumeVersion(recommendation.getResumeVersion());
        dto.setApplicationStatus(recommendation.getApplicationStatus());
        dto.setApplicationUrl(recommendation.getApplicationUrl());
        dto.setApplicationReference(recommendation.getApplicationReference());
        dto.setApplicationNotes(recommendation.getApplicationNotes());
        dto.setFeedbackType(recommendation.getFeedbackType());
        dto.setFeedbackNotes(recommendation.getFeedbackNotes());

        jobOpt.ifPresent(job -> dto.setJob(mapJobToResponseDto(job)));

        return dto;
    }

    private JobResponseDto mapJobToResponseDto(Job job) {
        JobResponseDto dto = new JobResponseDto();
        dto.setId(job.getId());
        dto.setExternalId(job.getExternalId());
        dto.setSource(job.getSource());
        dto.setCompany(job.getCompany());
        dto.setTitle(job.getTitle());
        dto.setNormalizedRole(job.getNormalizedRole());
        dto.setDescription(job.getDescription());
        dto.setEmploymentType(job.getEmploymentType());
        dto.setMinimumExperience(job.getMinimumExperience());
        dto.setMinimumCGPA(job.getMinimumCGPA());
        dto.setSalaryMin(job.getSalaryMin());
        dto.setSalaryMax(job.getSalaryMax());
        dto.setSalaryCurrency(job.getSalaryCurrency());
        dto.setStatus(job.getStatus());
        dto.setFingerprint(job.getFingerprint());
        dto.setVersion(job.getVersion());
        dto.setCreatedAt(job.getCreatedAt());
        dto.setUpdatedAt(job.getUpdatedAt());
        dto.setLastSyncedAt(job.getLastSyncedAt());

        if (job.getSkills() != null) {
            dto.setSkills(job.getSkills().stream()
                    .map(s -> new JobSkillDto(s.getId(), s.getSkillName(), s.isPreferred()))
                    .collect(Collectors.toList()));
        }

        if (job.getLocations() != null) {
            dto.setLocations(job.getLocations().stream()
                    .map(l -> new JobLocationDto(l.getId(), l.getLocation()))
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
