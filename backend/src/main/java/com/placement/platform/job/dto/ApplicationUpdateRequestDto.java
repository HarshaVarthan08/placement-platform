package com.placement.platform.job.dto;

import com.placement.platform.job.recommendation.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ApplicationUpdateRequestDto {
    @NotNull
    private ApplicationStatus applicationStatus;
    
    @Size(max = 2000)
    private String applicationNotes;

    public ApplicationUpdateRequestDto() {}

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getApplicationNotes() {
        return applicationNotes;
    }

    public void setApplicationNotes(String applicationNotes) {
        this.applicationNotes = applicationNotes;
    }
}
