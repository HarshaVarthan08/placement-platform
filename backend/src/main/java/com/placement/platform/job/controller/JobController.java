package com.placement.platform.job.controller;

import com.placement.platform.job.dto.*;
import com.placement.platform.job.entity.Job;
import com.placement.platform.job.entity.JobStatus;
import com.placement.platform.job.repository.JobRepository;
import com.placement.platform.job.service.JobSynchronizationManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
@Tag(name = "Jobs", description = "Endpoints for retrieving active job details")
public class JobController {

    private final JobSynchronizationManager syncManager;
    private final JobRepository jobRepository;

    public JobController(JobSynchronizationManager syncManager, JobRepository jobRepository) {
        this.syncManager = syncManager;
        this.jobRepository = jobRepository;
    }

    @PostMapping("/sync")
    @Operation(summary = "Synchronize job sources", description = "Manually triggers the ingestion process from all registered external job source adapters (requires ADMIN role).", tags = { "Administration" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Job synchronization complete"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin permissions required")
    })
    public ResponseEntity<List<SyncReportDto>> synchronizeJobs() {
        List<SyncReportDto> reports = syncManager.synchronizeAll();
        return ResponseEntity.ok(reports);
    }

    @GetMapping
    @Operation(summary = "Get active jobs", description = "Retrieves all currently active job postings in the database.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved active jobs list")
    public ResponseEntity<List<JobResponseDto>> getActiveJobs() {
        List<Job> activeJobs = jobRepository.findActiveJobs();
        List<JobResponseDto> response = activeJobs.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job details by ID", description = "Retrieves the full job description, eligibility rules, and location info for a specific job ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved job details"),
        @ApiResponse(responseCode = "404", description = "Job not found")
    })
    public ResponseEntity<JobResponseDto> getJobById(@PathVariable Long id) {
        return jobRepository.findById(id)
                .map(job -> ResponseEntity.ok(mapToResponseDto(job)))
                .orElse(ResponseEntity.notFound().build());
    }

    private JobResponseDto mapToResponseDto(Job job) {
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
