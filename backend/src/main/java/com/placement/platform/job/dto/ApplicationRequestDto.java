package com.placement.platform.job.dto;

import jakarta.validation.constraints.Size;

public class ApplicationRequestDto {
    private String applicationUrl;
    private String applicationReference;
    @Size(max = 2000)
    private String applicationNotes;

    public ApplicationRequestDto() {}

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

    public String getApplicationReference() {
        return applicationReference;
    }

    public void setApplicationReference(String applicationReference) {
        this.applicationReference = applicationReference;
    }

    public String getApplicationNotes() {
        return applicationNotes;
    }

    public void setApplicationNotes(String applicationNotes) {
        this.applicationNotes = applicationNotes;
    }
}
