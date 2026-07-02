package com.placement.platform.job.service;

import com.placement.platform.job.dto.SyncReportDto;
import com.placement.platform.job.entity.JobSyncHistory;
import com.placement.platform.job.entity.SyncStatus;
import com.placement.platform.job.repository.JobSyncHistoryRepository;
import com.placement.platform.job.service.source.JobSource;
import com.placement.platform.job.service.source.JobSourceRegistry;
import com.placement.platform.job.service.source.JobSynchronizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobSynchronizationManager {

    private static final Logger log = LoggerFactory.getLogger(JobSynchronizationManager.class);

    private final JobSourceRegistry sourceRegistry;
    private final JobSynchronizationService syncService;
    private final JobSyncHistoryRepository syncHistoryRepository;

    public JobSynchronizationManager(JobSourceRegistry sourceRegistry,
                                     JobSynchronizationService syncService,
                                     JobSyncHistoryRepository syncHistoryRepository) {
        this.sourceRegistry = sourceRegistry;
        this.syncService = syncService;
        this.syncHistoryRepository = syncHistoryRepository;
    }

    public List<SyncReportDto> synchronizeAll() {
        log.info("Starting orchestration of all registered JobSources...");
        List<SyncReportDto> reports = new ArrayList<>();

        for (JobSource source : sourceRegistry.getAllSources()) {
            LocalDateTime startedAt = LocalDateTime.now();
            SyncReportDto report;
            try {
                report = syncService.synchronize(source);
                saveHistory(report, startedAt, LocalDateTime.now(), SyncStatus.SUCCESS);
            } catch (Exception e) {
                log.error("Synchronization failed for source: {}", source.getSourceType(), e);
                report = new SyncReportDto();
                report.setSource(source.getSourceType());
                report.setStatus(SyncStatus.FAILED);
                report.setExecutionTimeMs(System.currentTimeMillis() - startedAt.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli());
                saveHistory(report, startedAt, LocalDateTime.now(), SyncStatus.FAILED);
            }
            reports.add(report);
        }

        log.info("Completed orchestration of all JobSources. Total sources synced: {}", reports.size());
        return reports;
    }

    private void saveHistory(SyncReportDto report, LocalDateTime startedAt, LocalDateTime completedAt, SyncStatus status) {
        try {
            JobSyncHistory history = new JobSyncHistory();
            history.setSource(report.getSource());
            history.setStartedAt(startedAt);
            history.setCompletedAt(completedAt);
            history.setImportedJobs(report.getImportedJobsCount());
            history.setInsertedJobs(report.getInsertedJobsCount());
            history.setUpdatedJobs(report.getUpdatedJobsCount());
            history.setExpiredJobs(report.getExpiredJobsCount());
            history.setArchivedJobs(report.getArchivedJobsCount());
            history.setDuplicateJobs(report.getDuplicateJobsCount());
            history.setExecutionTimeMs(report.getExecutionTimeMs());
            history.setSyncStatus(status);

            syncHistoryRepository.save(history);
            log.info("Persisted JobSyncHistory for source: {}", report.getSource());
        } catch (Exception ex) {
            log.error("Failed to save synchronization history for source: {}", report.getSource(), ex);
        }
    }
}
