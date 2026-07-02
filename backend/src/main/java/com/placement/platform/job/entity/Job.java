package com.placement.platform.job.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", nullable = false)
    private String externalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private JobSourceType source;

    @Column(name = "company", nullable = false)
    private String company;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "normalized_role", nullable = false)
    private String normalizedRole;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false)
    private EmploymentType employmentType;

    @Column(name = "minimum_experience")
    private Integer minimumExperience;

    @Column(name = "minimum_cgpa", precision = 3, scale = 2)
    private BigDecimal minimumCGPA;

    @Column(name = "salary_min", precision = 12, scale = 2)
    private BigDecimal salaryMin;

    @Column(name = "salary_max", precision = 12, scale = 2)
    private BigDecimal salaryMax;

    @Enumerated(EnumType.STRING)
    @Column(name = "salary_currency")
    private CurrencyType salaryCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private JobStatus status = JobStatus.ACTIVE;

    @Column(name = "fingerprint", nullable = false, unique = true)
    private String fingerprint;

    @Column(name = "version", nullable = false)
    private Integer version = 1;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "last_synced_at", nullable = false)
    private LocalDateTime lastSyncedAt;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<JobSkill> skills = new LinkedHashSet<>();

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<JobLocation> locations = new LinkedHashSet<>();

    // Constructors
    public Job() {
    }

    // Helper methods for relationships
    public void addSkill(JobSkill skill) {
        skills.add(skill);
        skill.setJob(this);
    }

    public void removeSkill(JobSkill skill) {
        skills.remove(skill);
        skill.setJob(null);
    }

    public void addLocation(JobLocation location) {
        locations.add(location);
        location.setJob(this);
    }

    public void removeLocation(JobLocation location) {
        locations.remove(location);
        location.setJob(null);
    }

    // Getters and Setters
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

    public Set<JobSkill> getSkills() {
        return skills;
    }

    public void setSkills(Set<JobSkill> skills) {
        this.skills = skills;
    }

    public Set<JobLocation> getLocations() {
        return locations;
    }

    public void setLocations(Set<JobLocation> locations) {
        this.locations = locations;
    }
}
