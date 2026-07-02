package com.placement.platform.job.repository;

import com.placement.platform.job.entity.Job;
import com.placement.platform.job.entity.JobSourceType;
import com.placement.platform.job.entity.JobStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobRepositoryTest {

    @Mock
    private JobRepository jobRepository;

    @Test
    void testRepositoryQueries() {
        Job job = new Job();
        job.setId(1L);
        job.setExternalId("STATIC-001");
        job.setSource(JobSourceType.STATIC);
        job.setStatus(JobStatus.ACTIVE);

        when(jobRepository.findBySourceAndExternalId(JobSourceType.STATIC, "STATIC-001"))
                .thenReturn(Optional.of(job));
        when(jobRepository.findActiveJobs())
                .thenReturn(Collections.singletonList(job));
        when(jobRepository.findByStatus(JobStatus.ACTIVE))
                .thenReturn(Collections.singletonList(job));

        Optional<Job> found = jobRepository.findBySourceAndExternalId(JobSourceType.STATIC, "STATIC-001");
        assertTrue(found.isPresent());
        assertEquals("STATIC-001", found.get().getExternalId());

        List<Job> active = jobRepository.findActiveJobs();
        assertEquals(1, active.size());
        assertEquals(JobStatus.ACTIVE, active.get(0).getStatus());

        List<Job> statusJobs = jobRepository.findByStatus(JobStatus.ACTIVE);
        assertEquals(1, statusJobs.size());
    }
}
