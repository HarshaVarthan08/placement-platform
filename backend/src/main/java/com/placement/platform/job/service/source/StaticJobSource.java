package com.placement.platform.job.service.source;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.placement.platform.job.dto.JobImportDto;
import com.placement.platform.job.dto.StaticJobImportDto;
import com.placement.platform.job.entity.CurrencyType;
import com.placement.platform.job.entity.JobSourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class StaticJobSource implements JobSource {

    private static final Logger log = LoggerFactory.getLogger(StaticJobSource.class);

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    @Value("${application.jobs.static.path:jobs/jobs.json}")
    private String staticPath;

    public StaticJobSource(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<JobImportDto> fetchJobs() {
        log.info("Reading StaticJobSource from path: {}", staticPath);
        List<JobImportDto> canonicalJobs = new ArrayList<>();
        try {
            Resource resource = resourceLoader.getResource("classpath:" + staticPath);
            if (!resource.exists()) {
                log.error("Static jobs file does not exist at classpath:{}", staticPath);
                return canonicalJobs;
            }
            try (InputStream is = resource.getInputStream()) {
                List<StaticJobImportDto> rawJobs = objectMapper.readValue(is, new TypeReference<List<StaticJobImportDto>>() {});
                log.info("Successfully read {} raw jobs from JSON source file.", rawJobs.size());
                
                // Provider-specific mapping pipeline: StaticJobImportDto -> JobImportDto
                for (StaticJobImportDto raw : rawJobs) {
                    JobImportDto canonical = mapToCanonical(raw);
                    canonicalJobs.add(canonical);
                }
            }
        } catch (Exception e) {
            log.error("Failed to read static jobs from path: {}", staticPath, e);
            throw new RuntimeException("Failed to load static jobs", e);
        }
        return canonicalJobs;
    }

    @Override
    public JobSourceType getSourceType() {
        return JobSourceType.STATIC;
    }

    @Override
    public boolean supportsIncrementalSync() {
        return false;
    }

    private JobImportDto mapToCanonical(StaticJobImportDto raw) {
        JobImportDto canonical = new JobImportDto();
        canonical.setExternalId(raw.getExternalId());
        canonical.setCompany(raw.getCompany());
        canonical.setTitle(raw.getTitle());
        canonical.setEmploymentType(raw.getEmploymentType());
        canonical.setLocations(raw.getLocations());
        canonical.setRequiredSkills(raw.getRequiredSkills());
        canonical.setPreferredSkills(raw.getPreferredSkills());
        canonical.setMinimumCGPA(raw.getMinimumCGPA());
        canonical.setMinimumExperience(raw.getMinimumExperience());
        canonical.setSalaryMin(raw.getSalaryMin());
        canonical.setSalaryMax(raw.getSalaryMax());
        canonical.setDescription(raw.getDescription());
        canonical.setStatus(raw.getStatus());

        // Map CurrencyType with safe parsing
        if (raw.getSalaryCurrency() != null) {
            try {
                canonical.setSalaryCurrency(CurrencyType.valueOf(raw.getSalaryCurrency().trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                // If it doesn't match an enum, set to null and let normalizer/validation reject/warn
                canonical.setSalaryCurrency(null);
            }
        }
        return canonical;
    }
}
