package com.placement.platform.job.service.source;

import com.placement.platform.job.dto.JobImportDto;
import com.placement.platform.job.dto.SyncReportDto;
import com.placement.platform.job.entity.*;
import com.placement.platform.job.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobSynchronizationServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private JobNormalizer jobNormalizer;

    @Mock
    private JobSource jobSource;

    @InjectMocks
    private JobSynchronizationService syncService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(syncService, "archiveDays", 90);
    }

    @Test
    void testFirstSynchronization_InsertNewJobs() {
        // Arrange
        when(jobSource.getSourceType()).thenReturn(JobSourceType.STATIC);
        
        JobImportDto importDto = new JobImportDto();
        importDto.setExternalId("STATIC-001");
        List<JobImportDto> imports = Collections.singletonList(importDto);
        when(jobSource.fetchJobs()).thenReturn(imports);

        JobImportResult importResult = new JobImportResult();
        Job normalizedJob = createNormalizedJob("STATIC-001", "hash123");
        importResult.addValidJob(normalizedJob);
        
        when(jobNormalizer.processImports(JobSourceType.STATIC, imports)).thenReturn(importResult);
        when(jobRepository.findBySourceAndExternalId(JobSourceType.STATIC, "STATIC-001")).thenReturn(Optional.empty());
        when(jobRepository.findBySource(JobSourceType.STATIC)).thenReturn(Collections.singletonList(normalizedJob));

        // Act
        SyncReportDto report = syncService.synchronize(jobSource);

        // Assert
        assertEquals(1, report.getInsertedJobsCount());
        assertEquals(0, report.getUpdatedJobsCount());
        assertEquals(0, report.getExpiredJobsCount());
        verify(jobRepository, atLeastOnce()).save(normalizedJob);
    }

    @Test
    void testSynchronization_Idempotency_NoChanges() {
        // Arrange
        when(jobSource.getSourceType()).thenReturn(JobSourceType.STATIC);
        
        JobImportDto importDto = new JobImportDto();
        importDto.setExternalId("STATIC-001");
        List<JobImportDto> imports = Collections.singletonList(importDto);
        when(jobSource.fetchJobs()).thenReturn(imports);

        JobImportResult importResult = new JobImportResult();
        Job incomingJob = createNormalizedJob("STATIC-001", "hash123");
        importResult.addValidJob(incomingJob);
        
        when(jobNormalizer.processImports(JobSourceType.STATIC, imports)).thenReturn(importResult);

        Job existingJob = createNormalizedJob("STATIC-001", "hash123");
        existingJob.setId(101L);
        existingJob.setVersion(1);
        when(jobRepository.findBySourceAndExternalId(JobSourceType.STATIC, "STATIC-001")).thenReturn(Optional.of(existingJob));
        when(jobRepository.findBySource(JobSourceType.STATIC)).thenReturn(Collections.singletonList(existingJob));

        // Act
        SyncReportDto report = syncService.synchronize(jobSource);

        // Assert
        assertEquals(0, report.getInsertedJobsCount());
        assertEquals(0, report.getUpdatedJobsCount()); // Should be 0 since nothing changed
        assertEquals(1, existingJob.getVersion()); // Version remains 1
        verify(jobRepository).save(existingJob);
    }

    @Test
    void testSynchronization_UpdateOnChanges() {
        // Arrange
        when(jobSource.getSourceType()).thenReturn(JobSourceType.STATIC);
        
        JobImportDto importDto = new JobImportDto();
        importDto.setExternalId("STATIC-001");
        List<JobImportDto> imports = Collections.singletonList(importDto);
        when(jobSource.fetchJobs()).thenReturn(imports);

        JobImportResult importResult = new JobImportResult();
        Job incomingJob = createNormalizedJob("STATIC-001", "hash123");
        incomingJob.setTitle("New Updated Title"); // Changed title
        importResult.addValidJob(incomingJob);
        
        when(jobNormalizer.processImports(JobSourceType.STATIC, imports)).thenReturn(importResult);

        Job existingJob = createNormalizedJob("STATIC-001", "hash123");
        existingJob.setId(101L);
        existingJob.setVersion(1);
        when(jobRepository.findBySourceAndExternalId(JobSourceType.STATIC, "STATIC-001")).thenReturn(Optional.of(existingJob));
        when(jobRepository.findBySource(JobSourceType.STATIC)).thenReturn(Collections.singletonList(existingJob));

        // Act
        SyncReportDto report = syncService.synchronize(jobSource);

        // Assert
        assertEquals(0, report.getInsertedJobsCount());
        assertEquals(1, report.getUpdatedJobsCount());
        assertEquals(2, existingJob.getVersion()); // Version should increment to 2
        assertEquals("New Updated Title", existingJob.getTitle());
    }

    @Test
    void testSynchronization_ExpireMissingJobs() {
        // Arrange
        when(jobSource.getSourceType()).thenReturn(JobSourceType.STATIC);
        when(jobSource.fetchJobs()).thenReturn(Collections.emptyList());
        when(jobNormalizer.processImports(eq(JobSourceType.STATIC), anyList())).thenReturn(new JobImportResult());

        Job oldJob = createNormalizedJob("STATIC-999", "hash999");
        oldJob.setStatus(JobStatus.ACTIVE);
        oldJob.setLastSyncedAt(LocalDateTime.now().minusHours(1)); // Was not synced in current run
        when(jobRepository.findBySource(JobSourceType.STATIC)).thenReturn(Collections.singletonList(oldJob));

        // Act
        SyncReportDto report = syncService.synchronize(jobSource);

        // Assert
        assertEquals(1, report.getExpiredJobsCount());
        assertEquals(JobStatus.EXPIRED, oldJob.getStatus());
        verify(jobRepository).save(oldJob);
    }

    @Test
    void testSynchronization_ArchiveOldExpiredJobs() {
        // Arrange
        when(jobSource.getSourceType()).thenReturn(JobSourceType.STATIC);
        when(jobSource.fetchJobs()).thenReturn(Collections.emptyList());
        when(jobNormalizer.processImports(eq(JobSourceType.STATIC), anyList())).thenReturn(new JobImportResult());

        Job oldExpiredJob = createNormalizedJob("STATIC-888", "hash888");
        oldExpiredJob.setStatus(JobStatus.EXPIRED);
        // Set updatedAt to be 100 days ago, which is older than the 90 days retention period
        oldExpiredJob.setUpdatedAt(LocalDateTime.now().minusDays(100));
        oldExpiredJob.setLastSyncedAt(LocalDateTime.now().minusDays(100));
        
        when(jobRepository.findBySource(JobSourceType.STATIC)).thenReturn(Collections.singletonList(oldExpiredJob));

        // Act
        SyncReportDto report = syncService.synchronize(jobSource);

        // Assert
        assertEquals(1, report.getArchivedJobsCount());
        assertEquals(JobStatus.ARCHIVED, oldExpiredJob.getStatus());
        verify(jobRepository).save(oldExpiredJob);
    }

    private Job createNormalizedJob(String extId, String fingerprint) {
        Job job = new Job();
        job.setExternalId(extId);
        job.setSource(JobSourceType.STATIC);
        job.setCompany("Oracle");
        job.setTitle("Software Engineer");
        job.setNormalizedRole("SOFTWARE_ENGINEER");
        job.setDescription("Original description");
        job.setEmploymentType(EmploymentType.FULL_TIME);
        job.setMinimumExperience(0);
        job.setMinimumCGPA(BigDecimal.ZERO);
        job.setSalaryMin(BigDecimal.TEN);
        job.setSalaryMax(BigDecimal.valueOf(15));
        job.setSalaryCurrency(CurrencyType.INR);
        job.setStatus(JobStatus.ACTIVE);
        job.setFingerprint(fingerprint);
        job.setVersion(1);
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());
        job.setLastSyncedAt(LocalDateTime.now());
        return job;
    }
}
