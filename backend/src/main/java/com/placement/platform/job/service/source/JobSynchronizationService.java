package com.placement.platform.job.service.source;

import com.placement.platform.job.dto.JobImportDto;
import com.placement.platform.job.dto.SyncReportDto;
import com.placement.platform.job.entity.*;
import com.placement.platform.job.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobSynchronizationService {

    private static final Logger log = LoggerFactory.getLogger(JobSynchronizationService.class);

    private final JobRepository jobRepository;
    private final JobNormalizer jobNormalizer;

    @Value("${application.jobs.archive.days:90}")
    private int archiveDays;

    public JobSynchronizationService(JobRepository jobRepository, JobNormalizer jobNormalizer) {
        this.jobRepository = jobRepository;
        this.jobNormalizer = jobNormalizer;
    }

    @Transactional
    public SyncReportDto synchronize(JobSource source) {
        long startTimeMs = System.currentTimeMillis();
        LocalDateTime syncStartTime = LocalDateTime.now();

        log.info("Synchronization Started for source {}", source.getSourceType());

        // 1. Fetch raw jobs from provider
        List<JobImportDto> rawImports = source.fetchJobs();
        log.info("{} Jobs Read from source {}", rawImports.size(), source.getSourceType());

        // 2. Normalize and validate imports
        JobImportResult importResult = jobNormalizer.processImports(source.getSourceType(), rawImports);
        log.info("Normalization Complete. Valid: {}, Invalid: {}, Duplicate in batch: {}",
                importResult.getValidJobs().size(),
                importResult.getInvalidJobs().size(),
                importResult.getDuplicateJobs().size());

        int insertedJobsCount = 0;
        int updatedJobsCount = 0;

        // 3. Sync valid jobs with database
        for (Job incoming : importResult.getValidJobs()) {
            // Find existing job by source and external ID first, then fallback to fingerprint
            Optional<Job> existingOpt = jobRepository.findBySourceAndExternalId(incoming.getSource(), incoming.getExternalId());
            if (existingOpt.isEmpty()) {
                existingOpt = jobRepository.findByFingerprint(incoming.getFingerprint());
            }

            if (existingOpt.isPresent()) {
                Job existing = existingOpt.get();
                if (hasJobChanged(existing, incoming)) {
                    // Update details and increment version
                    updateJobDetails(existing, incoming, syncStartTime);
                    jobRepository.save(existing);
                    updatedJobsCount++;
                } else {
                    // Update only lastSyncedAt to keep it ACTIVE and show it was seen
                    existing.setLastSyncedAt(syncStartTime);
                    jobRepository.save(existing);
                }
            } else {
                // New job insert
                incoming.setLastSyncedAt(syncStartTime);
                jobRepository.save(incoming);
                insertedJobsCount++;
            }
        }

        // 4. Detect and expire missing jobs
        // Missing jobs are ACTIVE jobs of this source whose lastSyncedAt is older than syncStartTime
        List<Job> activeJobs = jobRepository.findBySource(source.getSourceType());
        List<Job> missingJobs = activeJobs.stream()
                .filter(j -> j.getStatus() == JobStatus.ACTIVE && j.getLastSyncedAt().isBefore(syncStartTime))
                .collect(Collectors.toList());

        for (Job missing : missingJobs) {
            missing.setStatus(JobStatus.EXPIRED);
            missing.setUpdatedAt(LocalDateTime.now());
            jobRepository.save(missing);
        }
        int expiredJobsCount = missingJobs.size();

        // 5. Archive old expired jobs
        // Retention is based on archiveDays from updatedAt
        LocalDateTime archiveThreshold = LocalDateTime.now().minusDays(archiveDays);
        List<Job> expiredJobs = activeJobs.stream()
                .filter(j -> j.getStatus() == JobStatus.EXPIRED && j.getUpdatedAt().isBefore(archiveThreshold))
                .collect(Collectors.toList());

        for (Job oldExpired : expiredJobs) {
            oldExpired.setStatus(JobStatus.ARCHIVED);
            oldExpired.setUpdatedAt(LocalDateTime.now());
            jobRepository.save(oldExpired);
        }
        int archivedJobsCount = expiredJobs.size();

        long endTimeMs = System.currentTimeMillis();
        long executionTimeMs = endTimeMs - startTimeMs;

        // Structured Logging
        log.info("Reading {} JobSource completed.", source.getSourceType());
        log.info("{} Inserted", insertedJobsCount);
        log.info("{} Updated", updatedJobsCount);
        log.info("{} Expired", expiredJobsCount);
        log.info("{} Archived", archivedJobsCount);
        log.info("Synchronization Completed Successfully. Execution Time: {} ms", executionTimeMs);

        // Build Report DTO
        SyncReportDto report = new SyncReportDto();
        report.setSource(source.getSourceType());
        report.setImportedJobsCount(rawImports.size());
        report.setInsertedJobsCount(insertedJobsCount);
        report.setUpdatedJobsCount(updatedJobsCount);
        report.setExpiredJobsCount(expiredJobsCount);
        report.setArchivedJobsCount(archivedJobsCount);
        report.setDuplicateJobsCount(importResult.getDuplicateJobs().size());
        report.setExecutionTimeMs(executionTimeMs);
        report.setStatus(SyncStatus.SUCCESS);

        return report;
    }

    private boolean hasJobChanged(Job existing, Job incoming) {
        // Compare primitive/simple business fields
        if (!Objects.equals(existing.getTitle(), incoming.getTitle())) return true;
        if (!Objects.equals(existing.getDescription(), incoming.getDescription())) return true;
        if (!Objects.equals(existing.getCompany(), incoming.getCompany())) return true;
        if (!Objects.equals(existing.getNormalizedRole(), incoming.getNormalizedRole())) return true;
        if (!Objects.equals(existing.getEmploymentType(), incoming.getEmploymentType())) return true;
        if (!Objects.equals(existing.getMinimumExperience(), incoming.getMinimumExperience())) return true;
        
        if (existing.getMinimumCGPA() == null || incoming.getMinimumCGPA() == null || 
            existing.getMinimumCGPA().compareTo(incoming.getMinimumCGPA()) != 0) return true;
        if (existing.getSalaryMin() == null || incoming.getSalaryMin() == null || 
            existing.getSalaryMin().compareTo(incoming.getSalaryMin()) != 0) return true;
        if (existing.getSalaryMax() == null || incoming.getSalaryMax() == null || 
            existing.getSalaryMax().compareTo(incoming.getSalaryMax()) != 0) return true;
        if (!Objects.equals(existing.getSalaryCurrency(), incoming.getSalaryCurrency())) return true;
        
        // If it was expired/archived, and is now imported, it has changed (should be set to ACTIVE)
        if (existing.getStatus() != JobStatus.ACTIVE) return true;

        // Check locations change
        Set<String> existingLocations = existing.getLocations().stream()
                .map(JobLocation::getLocation)
                .collect(Collectors.toSet());
        Set<String> incomingLocations = incoming.getLocations().stream()
                .map(JobLocation::getLocation)
                .collect(Collectors.toSet());
        if (!existingLocations.equals(incomingLocations)) return true;

        // Check skills change
        Set<String> existingSkills = existing.getSkills().stream()
                .map(s -> s.getSkillName().toLowerCase() + ":" + s.isPreferred())
                .collect(Collectors.toSet());
        Set<String> incomingSkills = incoming.getSkills().stream()
                .map(s -> s.getSkillName().toLowerCase() + ":" + s.isPreferred())
                .collect(Collectors.toSet());
        if (!existingSkills.equals(incomingSkills)) return true;

        return false;
    }

    private void updateJobDetails(Job existing, Job incoming, LocalDateTime syncTime) {
        existing.setTitle(incoming.getTitle());
        existing.setDescription(incoming.getDescription());
        existing.setCompany(incoming.getCompany());
        existing.setNormalizedRole(incoming.getNormalizedRole());
        existing.setEmploymentType(incoming.getEmploymentType());
        existing.setMinimumExperience(incoming.getMinimumExperience());
        existing.setMinimumCGPA(incoming.getMinimumCGPA());
        existing.setSalaryMin(incoming.getSalaryMin());
        existing.setSalaryMax(incoming.getSalaryMax());
        existing.setSalaryCurrency(incoming.getSalaryCurrency());
        existing.setStatus(JobStatus.ACTIVE);
        existing.setFingerprint(incoming.getFingerprint());
        
        // Update version and update timestamps
        existing.setVersion(existing.getVersion() + 1);
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setLastSyncedAt(syncTime);

        // Update Locations
        existing.getLocations().clear();
        for (JobLocation incomingLoc : incoming.getLocations()) {
            existing.addLocation(new JobLocation(existing, incomingLoc.getLocation()));
        }

        // Update Skills
        existing.getSkills().clear();
        for (JobSkill incomingSkill : incoming.getSkills()) {
            existing.addSkill(new JobSkill(existing, incomingSkill.getSkillName(), incomingSkill.isPreferred()));
        }
    }
}
