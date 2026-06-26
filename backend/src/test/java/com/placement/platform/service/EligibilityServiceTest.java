package com.placement.platform.service;

import com.placement.platform.dto.EligibilityResponseDto;
import com.placement.platform.entity.Skill;
import com.placement.platform.entity.TargetCompany;
import com.placement.platform.entity.User;
import com.placement.platform.entity.UserSkill;
import com.placement.platform.repository.UserSkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EligibilityServiceTest {

    @Mock
    private UserSkillRepository userSkillRepository;

    @InjectMocks
    private EligibilityServiceImpl eligibilityService;

    private User student;
    private TargetCompany company;

    @BeforeEach
    void setUp() {
        student = new User();
        student.setId(1L);
        student.setName("Harsha");
        student.setEmail("harsha@gmail.com");
        student.setCgpa(new BigDecimal("8.50"));
        student.setBranch("Computer Science");

        company = new TargetCompany("Google");
        company.setId(101L);
        company.setMinCgpa(new BigDecimal("8.00"));
        company.getEligibleBranches().add("Computer Science");
        company.getEligibleBranches().add("Information Technology");
    }

    @Test
    void checkEligibility_AllMatched_ShouldReturnTrue() {
        // Setup company required skills
        Skill java = new Skill("Java");
        Skill sql = new Skill("SQL");
        company.getRequiredSkills().addAll(Set.of(java, sql));

        // Setup student skills
        UserSkill studentJava = new UserSkill(student, java);
        UserSkill studentSql = new UserSkill(student, sql);
        when(userSkillRepository.findByUserId(1L)).thenReturn(List.of(studentJava, studentSql));

        EligibilityResponseDto response = eligibilityService.checkEligibility(student, company);

        assertTrue(response.eligible());
        assertTrue(response.cgpaMatched());
        assertTrue(response.branchMatched());
        assertTrue(response.skillsMatched());
        assertTrue(response.missingSkills().isEmpty());
    }

    @Test
    void checkEligibility_CgpaBelowThreshold_ShouldReturnFalse() {
        student.setCgpa(new BigDecimal("7.99"));

        EligibilityResponseDto response = eligibilityService.checkEligibility(student, company);

        assertFalse(response.eligible());
        assertFalse(response.cgpaMatched());
        assertTrue(response.branchMatched());
        assertTrue(response.skillsMatched());
    }

    @Test
    void checkEligibility_CgpaNull_ShouldReturnFalse() {
        student.setCgpa(null);

        EligibilityResponseDto response = eligibilityService.checkEligibility(student, company);

        assertFalse(response.eligible());
        assertFalse(response.cgpaMatched());
        assertTrue(response.branchMatched());
    }

    @Test
    void checkEligibility_CompanyCgpaNull_ShouldReturnTrueForCgpa() {
        company.setMinCgpa(null);
        student.setCgpa(null);

        EligibilityResponseDto response = eligibilityService.checkEligibility(student, company);

        assertTrue(response.cgpaMatched());
    }

    @Test
    void checkEligibility_BranchMismatch_ShouldReturnFalse() {
        student.setBranch("Mechanical");

        EligibilityResponseDto response = eligibilityService.checkEligibility(student, company);

        assertFalse(response.eligible());
        assertTrue(response.cgpaMatched());
        assertFalse(response.branchMatched());
    }

    @Test
    void checkEligibility_BranchCaseInsensitiveMatch_ShouldReturnTrue() {
        student.setBranch("  computer science  ");

        EligibilityResponseDto response = eligibilityService.checkEligibility(student, company);

        assertTrue(response.branchMatched());
    }

    @Test
    void checkEligibility_BranchNull_ShouldReturnFalse() {
        student.setBranch(null);

        EligibilityResponseDto response = eligibilityService.checkEligibility(student, company);

        assertFalse(response.eligible());
        assertFalse(response.branchMatched());
    }

    @Test
    void checkEligibility_CompanyBranchesEmpty_ShouldReturnTrue() {
        company.getEligibleBranches().clear();
        student.setBranch(null);

        EligibilityResponseDto response = eligibilityService.checkEligibility(student, company);

        assertTrue(response.branchMatched());
    }

    @Test
    void checkEligibility_MissingSkills_ShouldReturnFalseAndListMissing() {
        Skill java = new Skill("Java");
        Skill python = new Skill("Python");
        Skill sql = new Skill("SQL");
        company.getRequiredSkills().addAll(Set.of(java, python, sql));

        UserSkill studentJava = new UserSkill(student, java);
        when(userSkillRepository.findByUserId(1L)).thenReturn(List.of(studentJava));

        EligibilityResponseDto response = eligibilityService.checkEligibility(student, company);

        assertFalse(response.eligible());
        assertTrue(response.cgpaMatched());
        assertTrue(response.branchMatched());
        assertFalse(response.skillsMatched());
        assertEquals(2, response.missingSkills().size());
        assertTrue(response.missingSkills().contains("Python"));
        assertTrue(response.missingSkills().contains("SQL"));
    }

    @Test
    void checkEligibility_SkillsCaseInsensitive_ShouldReturnTrue() {
        Skill companyJava = new Skill("JAVA");
        company.getRequiredSkills().add(companyJava);

        Skill studentJava = new Skill("java");
        UserSkill studentSkill = new UserSkill(student, studentJava);
        when(userSkillRepository.findByUserId(1L)).thenReturn(List.of(studentSkill));

        EligibilityResponseDto response = eligibilityService.checkEligibility(student, company);

        assertTrue(response.skillsMatched());
        assertTrue(response.missingSkills().isEmpty());
    }
}
