package com.placement.platform.job.service.source;

import com.placement.platform.job.dto.JobImportDto;
import com.placement.platform.job.entity.Job;

import java.util.ArrayList;
import java.util.List;

public class JobImportResult {
    private final List<Job> validJobs = new ArrayList<>();
    private final List<JobImportDto> invalidJobs = new ArrayList<>();
    private final List<JobImportDto> duplicateJobs = new ArrayList<>();
    private final List<String> validationErrors = new ArrayList<>();

    public JobImportResult() {
    }

    public List<Job> getValidJobs() {
        return validJobs;
    }

    public List<JobImportDto> getInvalidJobs() {
        return invalidJobs;
    }

    public List<JobImportDto> getDuplicateJobs() {
        return duplicateJobs;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public void addValidJob(Job job) {
        this.validJobs.add(job);
    }

    public void addInvalidJob(JobImportDto dto, String error) {
        this.invalidJobs.add(dto);
        this.validationErrors.add("ExternalID [" + dto.getExternalId() + "]: " + error);
    }

    public void addDuplicateJob(JobImportDto dto) {
        this.duplicateJobs.add(dto);
        this.validationErrors.add("ExternalID [" + dto.getExternalId() + "]: Batch-level duplicate fingerprint/external ID.");
    }
}
