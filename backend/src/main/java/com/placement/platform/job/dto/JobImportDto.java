package com.placement.platform.job.dto;

import com.placement.platform.job.entity.CurrencyType;
import java.math.BigDecimal;
import java.util.List;

public class JobImportDto {
    private String externalId;
    private String company;
    private String title;
    private String employmentType;
    private List<String> locations;
    private List<String> requiredSkills;
    private List<String> preferredSkills;
    private BigDecimal minimumCGPA;
    private Integer minimumExperience;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private CurrencyType salaryCurrency;
    private String description;
    private String status;

    public JobImportDto() {
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
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

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public List<String> getPreferredSkills() {
        return preferredSkills;
    }

    public void setPreferredSkills(List<String> preferredSkills) {
        this.preferredSkills = preferredSkills;
    }

    public BigDecimal getMinimumCGPA() {
        return minimumCGPA;
    }

    public void setMinimumCGPA(BigDecimal minimumCGPA) {
        this.minimumCGPA = minimumCGPA;
    }

    public Integer getMinimumExperience() {
        return minimumExperience;
    }

    public void setMinimumExperience(Integer minimumExperience) {
        this.minimumExperience = minimumExperience;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
