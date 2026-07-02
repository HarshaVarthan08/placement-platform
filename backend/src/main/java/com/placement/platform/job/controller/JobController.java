package com.placement.platform.job.controller;

import com.placement.platform.job.dto.*;
import com.placement.platform.job.entity.Job;
import com.placement.platform.job.entity.JobStatus;
import com.placement.platform.job.repository.JobRepository;
import com.placement.platform.job.service.JobSynchronizationManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobSynchronizationManager syncManager;
    private final JobRepository jobRepository;

    public JobController(JobSynchronizationManager syncManager, JobRepository jobRepository) {
        this.syncManager = syncManager;
        this.jobRepository = jobRepository;
    }

    @PostMapping("/sync")
    public ResponseEntity<List<SyncReportDto>> synchronizeJobs() {
        List<SyncReportDto> reports = syncManager.synchronizeAll();
        return ResponseEntity.ok(reports);
    }

    @GetMapping
    public ResponseEntity<List<JobResponseDto>> getActiveJobs() {
        List<Job> activeJobs = jobRepository.findActiveJobs();
        List<JobResponseDto> response = activeJobs.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
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
