package com.placement.platform.job.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_sync_history")
public class JobSyncHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private JobSourceType source;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completedAt;

    @Column(name = "imported_jobs")
    private Integer importedJobs;

    @Column(name = "inserted_jobs")
    private Integer insertedJobs;

    @Column(name = "updated_jobs")
    private Integer updatedJobs;

    @Column(name = "expired_jobs")
    private Integer expiredJobs;

    @Column(name = "archived_jobs")
    private Integer archivedJobs;

    @Column(name = "duplicate_jobs")
    private Integer duplicateJobs;

    @Column(name = "execution_time_ms")
    private Long executionTimeMs;

    @Enumerated(EnumType.STRING)
    @Column(name = "sync_status")
    private SyncStatus syncStatus;

    public JobSyncHistory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobSourceType getSource() {
        return source;
    }

    public void setSource(JobSourceType source) {
        this.source = source;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Integer getImportedJobs() {
        return importedJobs;
    }

    public void setImportedJobs(Integer importedJobs) {
        this.importedJobs = importedJobs;
    }

    public Integer getInsertedJobs() {
        return insertedJobs;
    }

    public void setInsertedJobs(Integer insertedJobs) {
        this.insertedJobs = insertedJobs;
    }

    public Integer getUpdatedJobs() {
        return updatedJobs;
    }

    public void setUpdatedJobs(Integer updatedJobs) {
        this.updatedJobs = updatedJobs;
    }

    public Integer getExpiredJobs() {
        return expiredJobs;
    }

    public void setExpiredJobs(Integer expiredJobs) {
        this.expiredJobs = expiredJobs;
    }

    public Integer getArchivedJobs() {
        return archivedJobs;
    }

    public void setArchivedJobs(Integer archivedJobs) {
        this.archivedJobs = archivedJobs;
    }

    public Integer getDuplicateJobs() {
        return duplicateJobs;
    }

    public void setDuplicateJobs(Integer duplicateJobs) {
        this.duplicateJobs = duplicateJobs;
    }

    public Long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(Long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public SyncStatus getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(SyncStatus syncStatus) {
        this.syncStatus = syncStatus;
    }
}
