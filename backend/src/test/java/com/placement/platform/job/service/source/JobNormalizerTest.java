package com.placement.platform.job.service.source;

import com.placement.platform.job.dto.JobImportDto;
import com.placement.platform.job.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class JobNormalizerTest {

    private JobNormalizer normalizer;

    @BeforeEach
    void setUp() {
        normalizer = new JobNormalizer();
    }

    @Test
    void testNormalizeRoleNames() {
        List<JobImportDto> imports = new ArrayList<>();
        
        JobImportDto dto1 = createValidDto("STATIC-001");
        dto1.setTitle("Software Engineer II");
        imports.add(dto1);

        JobImportDto dto2 = createValidDto("STATIC-002");
        dto2.setTitle("Senior Backend Developer");
        imports.add(dto2);

        JobImportDto dto3 = createValidDto("STATIC-003");
        dto3.setTitle("React Frontend developer");
        imports.add(dto3);

        JobImportDto dto4 = createValidDto("STATIC-004");
        dto4.setTitle("AWS DevOps specialist");
        imports.add(dto4);

        JobImportResult result = normalizer.processImports(JobSourceType.STATIC, imports);
        assertEquals(4, result.getValidJobs().size());
        assertEquals("SOFTWARE_ENGINEER", result.getValidJobs().get(0).getNormalizedRole());
        assertEquals("BACKEND_DEVELOPER", result.getValidJobs().get(1).getNormalizedRole());
        assertEquals("FRONTEND_DEVELOPER", result.getValidJobs().get(2).getNormalizedRole());
        assertEquals("DEVOPS_ENGINEER", result.getValidJobs().get(3).getNormalizedRole());
    }

    @Test
    void testNormalizeEmploymentTypes() {
        List<JobImportDto> imports = new ArrayList<>();
        
        JobImportDto dto1 = createValidDto("STATIC-001");
        dto1.setEmploymentType("Full-Time");
        imports.add(dto1);

        JobImportDto dto2 = createValidDto("STATIC-002");
        dto2.setEmploymentType("internship");
        imports.add(dto2);

        JobImportResult result = normalizer.processImports(JobSourceType.STATIC, imports);
        assertEquals(2, result.getValidJobs().size());
        assertEquals(EmploymentType.FULL_TIME, result.getValidJobs().get(0).getEmploymentType());
        assertEquals(EmploymentType.INTERNSHIP, result.getValidJobs().get(1).getEmploymentType());
    }

    @Test
    void testNormalizeLocations() {
        JobImportDto dto = createValidDto("STATIC-001");
        dto.setLocations(Arrays.asList(" hyderabad ", "bangalore", "Hyderabad "));

        JobImportResult result = normalizer.processImports(JobSourceType.STATIC, Collections.singletonList(dto));
        assertEquals(1, result.getValidJobs().size());
        
        Job job = result.getValidJobs().get(0);
        assertEquals(2, job.getLocations().size());
        Set<String> locations = job.getLocations().stream()
                .map(JobLocation::getLocation)
                .collect(java.util.stream.Collectors.toSet());
        assertTrue(locations.contains("Hyderabad"));
        assertTrue(locations.contains("Bangalore"));
    }

    @Test
    void testNormalizeSkills() {
        JobImportDto dto = createValidDto("STATIC-001");
        dto.setRequiredSkills(Arrays.asList("spring", "springboot", "mysql", "JAVA"));
        dto.setPreferredSkills(Arrays.asList("docker", "AWS"));

        JobImportResult result = normalizer.processImports(JobSourceType.STATIC, Collections.singletonList(dto));
        assertEquals(1, result.getValidJobs().size());

        Job job = result.getValidJobs().get(0);
        Set<JobSkill> skills = job.getSkills();
        
        Map<String, Boolean> skillMap = skills.stream()
                .collect(java.util.stream.Collectors.toMap(JobSkill::getSkillName, JobSkill::isPreferred));

        assertEquals(6, skillMap.size());
        assertEquals(Boolean.FALSE, skillMap.get("Spring Framework"));
        assertEquals(Boolean.FALSE, skillMap.get("Spring Boot"));
        assertEquals(Boolean.FALSE, skillMap.get("MySQL"));
        assertEquals(Boolean.FALSE, skillMap.get("Java"));
        assertEquals(Boolean.TRUE, skillMap.get("Docker"));
        assertEquals(Boolean.TRUE, skillMap.get("AWS"));
    }

    @Test
    void testMandatoryFieldsValidation() {
        // Missing company
        JobImportDto dto1 = createValidDto("STATIC-001");
        dto1.setCompany(null);

        // Missing locations
        JobImportDto dto2 = createValidDto("STATIC-002");
        dto2.setLocations(null);

        // Invalid salary: negative
        JobImportDto dto3 = createValidDto("STATIC-003");
        dto3.setSalaryMin(new BigDecimal("-10"));

        // Invalid salary: min > max
        JobImportDto dto4 = createValidDto("STATIC-004");
        dto4.setSalaryMin(new BigDecimal("15"));
        dto4.setSalaryMax(new BigDecimal("10"));

        List<JobImportDto> list = Arrays.asList(dto1, dto2, dto3, dto4);
        JobImportResult result = normalizer.processImports(JobSourceType.STATIC, list);

        assertEquals(0, result.getValidJobs().size());
        assertEquals(4, result.getInvalidJobs().size());
        assertEquals(4, result.getValidationErrors().size());
    }

    @Test
    void testDuplicateValidation() {
        JobImportDto dto1 = createValidDto("STATIC-001");
        JobImportDto dto2 = createValidDto("STATIC-001"); // Duplicate external ID

        JobImportDto dto3 = createValidDto("STATIC-002");
        JobImportDto dto4 = createValidDto("STATIC-003");
        // Ensure same fingerprint: same source, company, normalizedRole, employmentType, location
        dto3.setCompany("Google");
        dto3.setTitle("Software Engineer");
        dto3.setEmploymentType("FULL_TIME");
        dto3.setLocations(Collections.singletonList("Hyderabad"));

        dto4.setCompany("Google");
        dto4.setTitle("Software Engineer");
        dto4.setEmploymentType("FULL_TIME");
        dto4.setLocations(Collections.singletonList("Hyderabad"));

        List<JobImportDto> list = Arrays.asList(dto1, dto2, dto3, dto4);
        JobImportResult result = normalizer.processImports(JobSourceType.STATIC, list);

        assertEquals(2, result.getValidJobs().size()); // dto1 and dto3 are valid
        assertEquals(1, result.getInvalidJobs().size()); // dto2 rejected as duplicate external ID
        assertEquals(1, result.getDuplicateJobs().size()); // dto4 rejected as duplicate fingerprint
    }

    private JobImportDto createValidDto(String extId) {
        JobImportDto dto = new JobImportDto();
        dto.setExternalId(extId);
        dto.setCompany("Oracle");
        dto.setTitle("Software Engineer");
        dto.setEmploymentType("FULL_TIME");
        dto.setLocations(Collections.singletonList("Hyderabad"));
        dto.setRequiredSkills(Collections.singletonList("Java"));
        dto.setMinimumCGPA(new BigDecimal("7.0"));
        dto.setMinimumExperience(1);
        dto.setSalaryMin(new BigDecimal("10"));
        dto.setSalaryMax(new BigDecimal("15"));
        dto.setSalaryCurrency(CurrencyType.INR);
        dto.setDescription("Description text");
        return dto;
    }
}
