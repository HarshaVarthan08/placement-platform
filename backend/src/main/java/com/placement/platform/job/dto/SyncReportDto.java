package com.placement.platform.job.dto;

import com.placement.platform.job.entity.JobSourceType;
import com.placement.platform.job.entity.SyncStatus;

public class SyncReportDto {
    private JobSourceType source;
    private int importedJobsCount;
    private int insertedJobsCount;
    private int updatedJobsCount;
    private int expiredJobsCount;
    private int archivedJobsCount;
    private int duplicateJobsCount;
    private long executionTimeMs;
    private SyncStatus status;

    public SyncReportDto() {
    }

    public JobSourceType getSource() {
        return source;
    }

    public void setSource(JobSourceType source) {
        this.source = source;
    }

    public int getImportedJobsCount() {
        return importedJobsCount;
    }

    public void setImportedJobsCount(int importedJobsCount) {
        this.importedJobsCount = importedJobsCount;
    }

    public int getInsertedJobsCount() {
        return insertedJobsCount;
    }

    public void setInsertedJobsCount(int insertedJobsCount) {
        this.insertedJobsCount = insertedJobsCount;
    }

    public int getUpdatedJobsCount() {
        return updatedJobsCount;
    }

    public void setUpdatedJobsCount(int updatedJobsCount) {
        this.updatedJobsCount = updatedJobsCount;
    }

    public int getExpiredJobsCount() {
        return expiredJobsCount;
    }

    public void setExpiredJobsCount(int expiredJobsCount) {
        this.expiredJobsCount = expiredJobsCount;
    }

    public int getArchivedJobsCount() {
        return archivedJobsCount;
    }

    public void setArchivedJobsCount(int archivedJobsCount) {
        this.archivedJobsCount = archivedJobsCount;
    }

    public int getDuplicateJobsCount() {
        return duplicateJobsCount;
    }

    public void setDuplicateJobsCount(int duplicateJobsCount) {
        this.duplicateJobsCount = duplicateJobsCount;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public SyncStatus getStatus() {
        return status;
    }

    public void setStatus(SyncStatus status) {
        this.status = status;
    }
}
