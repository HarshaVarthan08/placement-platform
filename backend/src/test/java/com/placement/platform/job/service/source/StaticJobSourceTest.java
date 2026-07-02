package com.placement.platform.job.service.source;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.placement.platform.job.dto.JobImportDto;
import com.placement.platform.job.entity.CurrencyType;
import com.placement.platform.job.entity.JobSourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StaticJobSourceTest {

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private Resource resource;

    private StaticJobSource staticJobSource;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        staticJobSource = new StaticJobSource(resourceLoader, objectMapper);
        ReflectionTestUtils.setField(staticJobSource, "staticPath", "jobs/jobs.json");
    }

    @Test
    void testFetchJobs_Success() throws IOException {
        String json = "[\n" +
                "  {\n" +
                "    \"externalId\": \"STATIC-001\",\n" +
                "    \"company\": \"Google\",\n" +
                "    \"title\": \"Software Engineer\",\n" +
                "    \"employmentType\": \"FULL_TIME\",\n" +
                "    \"locations\": [\"Hyderabad\"],\n" +
                "    \"requiredSkills\": [\"Java\"],\n" +
                "    \"preferredSkills\": [\"Docker\"],\n" +
                "    \"minimumCGPA\": 8.0,\n" +
                "    \"minimumExperience\": 0,\n" +
                "    \"salaryMin\": 12,\n" +
                "    \"salaryMax\": 18,\n" +
                "    \"salaryCurrency\": \"INR\",\n" +
                "    \"description\": \"Test description\",\n" +
                "    \"status\": \"ACTIVE\"\n" +
                "  }\n" +
                "]";

        when(resourceLoader.getResource("classpath:jobs/jobs.json")).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));

        List<JobImportDto> jobs = staticJobSource.fetchJobs();

        assertEquals(1, jobs.size());
        JobImportDto job = jobs.get(0);
        assertEquals("STATIC-001", job.getExternalId());
        assertEquals("Google", job.getCompany());
        assertEquals("Software Engineer", job.getTitle());
        assertEquals("FULL_TIME", job.getEmploymentType());
        assertEquals(CurrencyType.INR, job.getSalaryCurrency());
        assertEquals(JobSourceType.STATIC, staticJobSource.getSourceType());
        assertFalse(staticJobSource.supportsIncrementalSync());
    }

    @Test
    void testFetchJobs_FileNotFound() {
        when(resourceLoader.getResource("classpath:jobs/jobs.json")).thenReturn(resource);
        when(resource.exists()).thenReturn(false);

        List<JobImportDto> jobs = staticJobSource.fetchJobs();
        assertTrue(jobs.isEmpty());
    }

    @Test
    void testFetchJobs_InvalidJson_ShouldThrowException() throws IOException {
        String invalidJson = "{ invalid json }";

        when(resourceLoader.getResource("classpath:jobs/jobs.json")).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(invalidJson.getBytes(StandardCharsets.UTF_8)));

        assertThrows(RuntimeException.class, () -> staticJobSource.fetchJobs());
    }
}
