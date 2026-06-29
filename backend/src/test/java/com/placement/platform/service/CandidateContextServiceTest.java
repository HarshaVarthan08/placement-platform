package com.placement.platform.service;

import com.placement.platform.dto.CandidateContext;
import com.placement.platform.entity.*;
import com.placement.platform.exception.*;
import com.placement.platform.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CandidateContextServiceTest {

    @Mock
    private ResumeRepository resumeRepository;

    @Mock
    private ResumeAnalysisRepository resumeAnalysisRepository;

    @Mock
    private UserSkillRepository userSkillRepository;

    @Mock
    private UserTargetCompanyRepository userTargetCompanyRepository;

    @InjectMocks
    private CandidateContextServiceImpl candidateContextService;

    private User student;
    private Resume resume;
    private ResumeAnalysis analysis;

    @BeforeEach
    void setUp() {
        student = new User();
        student.setId(1L);
        student.setName("Harsha Varthan");
        student.setCollege("IIT Madras");
        student.setDegree("B.Tech");
        student.setBranch("Computer Science");
        student.setCgpa(new BigDecimal("9.50"));
        student.setTargetRole("Software Engineer");
        student.setProjects("AI Platform Project");
        student.setInternship("AI Research Intern");

        resume = new Resume();
        resume.setId(10L);
        resume.setUser(student);

        analysis = new ResumeAnalysis();
        analysis.setId(20L);
        analysis.setResume(resume);
        analysis.setSummary("An outstanding developer.");
        analysis.setStrengths(List.of("Problem solving"));
        analysis.setWeaknesses(List.of("Public speaking"));
        analysis.setMissingSkills(List.of("Docker"));
    }

    @Test
    void buildContext_Success() {
        when(resumeRepository.findByUser(student)).thenReturn(Optional.of(resume));
        when(resumeAnalysisRepository.findByResume(resume)).thenReturn(Optional.of(analysis));
        
        Skill javaSkill = new Skill("Java");
        UserSkill userSkill = new UserSkill(student, javaSkill);
        when(userSkillRepository.findByUserId(student.getId())).thenReturn(List.of(userSkill));

        TargetCompany google = new TargetCompany();
        google.setName("Google");
        UserTargetCompany userCompany = new UserTargetCompany(student, google);
        when(userTargetCompanyRepository.findByUserId(student.getId())).thenReturn(List.of(userCompany));

        CandidateContext context = candidateContextService.buildContext(student);

        assertNotNull(context);
        assertEquals("Harsha Varthan", context.name());
        assertEquals("IIT Madras", context.college());
        assertEquals("Software Engineer", context.targetRole());
        assertEquals("AI Platform Project", context.projects());
        assertEquals("AI Research Intern", context.internship());
        assertEquals(List.of("Java"), context.skills());
        assertEquals("An outstanding developer.", context.resumeSummary());
        assertEquals(List.of("Google"), context.targetCompanies());
    }

    @Test
    void buildContext_ProfileIncomplete_ThrowsException() {
        student.setBranch(null); // Incomplete profile

        assertThrows(ProfileIncompleteException.class, () -> {
            candidateContextService.buildContext(student);
        });
    }

    @Test
    void buildContext_TargetRoleMissing_ThrowsException() {
        student.setTargetRole(" "); // Missing target role

        assertThrows(TargetRoleMissingException.class, () -> {
            candidateContextService.buildContext(student);
        });
    }

    @Test
    void buildContext_ProjectsMissing_ThrowsException() {
        student.setProjects("  "); // whitespace projects

        assertThrows(ProfileIncompleteException.class, () -> {
            candidateContextService.buildContext(student);
        });
    }

    @Test
    void buildContext_InternshipMissing_ThrowsException() {
        student.setInternship(null); // null internship

        assertThrows(ProfileIncompleteException.class, () -> {
            candidateContextService.buildContext(student);
        });
    }

    @Test
    void buildContext_ResumeMissing_ThrowsException() {
        when(resumeRepository.findByUser(student)).thenReturn(Optional.empty());

        assertThrows(ResumeMissingException.class, () -> {
            candidateContextService.buildContext(student);
        });
    }

    @Test
    void buildContext_ResumeAnalysisMissing_ThrowsException() {
        when(resumeRepository.findByUser(student)).thenReturn(Optional.of(resume));
        when(resumeAnalysisRepository.findByResume(resume)).thenReturn(Optional.empty());

        assertThrows(ResumeAnalysisMissingException.class, () -> {
            candidateContextService.buildContext(student);
        });
    }
}
