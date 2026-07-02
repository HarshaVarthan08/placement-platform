package com.placement.platform.job.dto;

import com.placement.platform.job.entity.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class JobResponseDto {
    private Long id;
    private String externalId;
    private JobSourceType source;
    private String company;
    private String title;
    private String normalizedRole;
    private String description;
    private EmploymentType employmentType;
    private Integer minimumExperience;
    private BigDecimal minimumCGPA;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private CurrencyType salaryCurrency;
    private JobStatus status;
    private String fingerprint;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastSyncedAt;
    private List<JobSkillDto> skills;
    private List<JobLocationDto> locations;

    public JobResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public JobSourceType getSource() {
        return source;
    }

    public void setSource(JobSourceType source) {
        this.source = source;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNormalizedRole() {
        return normalizedRole;
    }

    public void setNormalizedRole(String normalizedRole) {
        this.normalizedRole = normalizedRole;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public Integer getMinimumExperience() {
        return minimumExperience;
    }

    public void setMinimumExperience(Integer minimumExperience) {
        this.minimumExperience = minimumExperience;
    }

    public BigDecimal getMinimumCGPA() {
        return minimumCGPA;
    }

    public void setMinimumCGPA(BigDecimal minimumCGPA) {
        this.minimumCGPA = minimumCGPA;
    }

    public BigDecimal getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(BigDecimal salaryMin) {
        this.salaryMin = salaryMin;
    }

    public BigDecimal getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(BigDecimal salaryMax) {
        this.salaryMax = salaryMax;
    }

    public CurrencyType getSalaryCurrency() {
        return salaryCurrency;
    }

    public void setSalaryCurrency(CurrencyType salaryCurrency) {
        this.salaryCurrency = salaryCurrency;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getLastSyncedAt() {
        return lastSyncedAt;
    }

    public void setLastSyncedAt(LocalDateTime lastSyncedAt) {
        this.lastSyncedAt = lastSyncedAt;
    }

    public List<JobSkillDto> getSkills() {
        return skills;
    }

    public void setSkills(List<JobSkillDto> skills) {
        this.skills = skills;
    }

    public List<JobLocationDto> getLocations() {
        return locations;
    }

    public void setLocations(List<JobLocationDto> locations) {
        this.locations = locations;
    }
}
