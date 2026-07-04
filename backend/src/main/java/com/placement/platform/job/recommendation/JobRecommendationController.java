package com.placement.platform.job.recommendation;

import com.placement.platform.entity.User;
import com.placement.platform.exception.UserNotFoundException;
import com.placement.platform.job.dto.*;
import com.placement.platform.job.entity.Job;
import com.placement.platform.job.repository.JobRepository;
import com.placement.platform.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs/recommendations")
public class JobRecommendationController {

    private final RecommendationService recommendationService;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public JobRecommendationController(
            RecommendationService recommendationService,
            UserRepository userRepository,
            JobRepository jobRepository
    ) {
        this.recommendationService = recommendationService;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    @PostMapping("/generate")
    public ResponseEntity<List<JobRecommendationResponseDto>> generateRecommendations() {
        User user = getAuthenticatedUser();
        List<JobRecommendation> recommendations = recommendationService.generateRecommendations(user);

        // Pre-fetch all jobs in a single query to avoid N+1 queries
        List<Long> jobIds = recommendations.stream()
                .map(JobRecommendation::getJobId)
                .collect(Collectors.toList());
        List<Job> jobs = jobRepository.findAllById(jobIds);
        Map<Long, Job> jobMap = jobs.stream()
                .collect(Collectors.toMap(Job::getId, j -> j));

        List<JobRecommendationResponseDto> response = recommendations.stream()
                .map(rec -> mapToResponseDto(rec, Optional.ofNullable(jobMap.get(rec.getJobId()))))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<JobRecommendationResponseDto>> getRecommendations(Pageable pageable) {
        User user = getAuthenticatedUser();
        Page<JobRecommendation> recommendationsPage = recommendationService.getRecommendations(user, pageable);

        // Pre-fetch all jobs in a single query to avoid N+1 queries
        List<Long> jobIds = recommendationsPage.getContent().stream()
                .map(JobRecommendation::getJobId)
                .collect(Collectors.toList());
        List<Job> jobs = jobRepository.findAllById(jobIds);
        Map<Long, Job> jobMap = jobs.stream()
                .collect(Collectors.toMap(Job::getId, j -> j));

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

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("User is not authenticated");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
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
