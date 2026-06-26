package com.placement.platform.service;

import com.placement.platform.dto.ResumeAnalysisResponseDto;
import com.placement.platform.entity.Resume;
import com.placement.platform.entity.ResumeAnalysis;
import com.placement.platform.entity.User;
import com.placement.platform.exception.GeminiApiException;
import com.placement.platform.exception.ResourceNotFoundException;
import com.placement.platform.exception.ResumeAnalysisException;
import com.placement.platform.exception.ResumeExtractionException;
import com.placement.platform.mapper.ResumeAnalysisMapper;
import com.placement.platform.repository.ResumeAnalysisRepository;
import com.placement.platform.repository.ResumeRepository;
import com.placement.platform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResumeAnalysisServiceTest {

    @Mock
    private ResumeRepository resumeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ResumeAnalysisRepository resumeAnalysisRepository;

    @Mock
    private ResumeTextExtractor resumeTextExtractor;

    @Mock
    private AIService aiService;

    @Mock
    private ResumeAnalysisMapper resumeAnalysisMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ResumeAnalysisServiceImpl resumeAnalysisService;

    private User student;
    private Resume pdfResume;
    private Resume docxResume;
    private ResumeAnalysisResponseDto validResponse;
    private ResumeAnalysis mockAnalysis;

    @BeforeEach
    void setUp() {
        // Setup authenticated user
        student = new User();
        student.setId(1L);
        student.setName("Harsha Varthan");
        student.setEmail("harsha@gmail.com");

        // Mock SecurityContextHolder
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("harsha@gmail.com");
        when(userRepository.findByEmail("harsha@gmail.com")).thenReturn(Optional.of(student));

        // Setup Resumes
        pdfResume = new Resume(student, "resume.pdf", "user_1_1234_resume.pdf", "/uploads/resumes/user_1_1234_resume.pdf", "application/pdf", 1024L);
        docxResume = new Resume(student, "resume.docx", "user_1_1234_resume.docx", "/uploads/resumes/user_1_1234_resume.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", 2048L);

        java.time.LocalDateTime analyzedTime = java.time.LocalDateTime.of(2026, 6, 26, 12, 0);
        // Setup valid response
        validResponse = new ResumeAnalysisResponseDto(
                85,
                "Strong developer profile",
                List.of("Java coding", "Clean architecture"),
                List.of("Short project descriptions"),
                List.of("Docker", "Kubernetes"),
                List.of("Add containerization details to project section"),
                analyzedTime
        );

        // Setup saved analysis entity
        mockAnalysis = new ResumeAnalysis();
        mockAnalysis.setId(10L);
        mockAnalysis.setResume(pdfResume);
        mockAnalysis.setAtsScore(85);
        mockAnalysis.setSummary("Strong developer profile");
        mockAnalysis.setStrengths(List.of("Java coding", "Clean architecture"));
        mockAnalysis.setWeaknesses(List.of("Short project descriptions"));
        mockAnalysis.setMissingSkills(List.of("Docker", "Kubernetes"));
        mockAnalysis.setSuggestions(List.of("Add containerization details to project section"));
        mockAnalysis.setModelUsed("gemini-2.5-flash");
        mockAnalysis.setCreatedAt(analyzedTime);
    }

    @Test
    void analyzeResume_Pdf_Success() {
        when(resumeRepository.findByUser(student)).thenReturn(Optional.of(pdfResume));
        when(resumeAnalysisRepository.existsByResume(pdfResume)).thenReturn(false);
        when(resumeTextExtractor.extractText(pdfResume.getFilePath())).thenReturn("Mocked PDF Resume Text Content");
        when(aiService.analyzeResumeText("Mocked PDF Resume Text Content")).thenReturn(validResponse);
        when(aiService.getModelUsed()).thenReturn("gemini-2.5-flash");
        when(resumeAnalysisRepository.save(any(ResumeAnalysis.class))).thenReturn(mockAnalysis);
        when(resumeAnalysisMapper.toDto(any(ResumeAnalysis.class))).thenReturn(validResponse);

        ResumeAnalysisResponseDto result = resumeAnalysisService.analyzeResume();

        assertNotNull(result);
        assertEquals(85, result.atsScore());
        assertEquals("Strong developer profile", result.summary());
        assertEquals(2, result.strengths().size());
        assertEquals("Java coding", result.strengths().get(0));

        verify(resumeTextExtractor, times(1)).extractText(pdfResume.getFilePath());
        verify(aiService, times(1)).analyzeResumeText(anyString());
        verify(resumeAnalysisRepository, times(1)).save(any(ResumeAnalysis.class));
    }

    @Test
    void analyzeResume_Docx_Success() {
        mockAnalysis.setResume(docxResume);
        when(resumeRepository.findByUser(student)).thenReturn(Optional.of(docxResume));
        when(resumeAnalysisRepository.existsByResume(docxResume)).thenReturn(false);
        when(resumeTextExtractor.extractText(docxResume.getFilePath())).thenReturn("Mocked DOCX Resume Text Content");
        when(aiService.analyzeResumeText("Mocked DOCX Resume Text Content")).thenReturn(validResponse);
        when(aiService.getModelUsed()).thenReturn("gemini-2.5-flash");
        when(resumeAnalysisRepository.save(any(ResumeAnalysis.class))).thenReturn(mockAnalysis);
        when(resumeAnalysisMapper.toDto(any(ResumeAnalysis.class))).thenReturn(validResponse);

        ResumeAnalysisResponseDto result = resumeAnalysisService.analyzeResume();

        assertNotNull(result);
        assertEquals(85, result.atsScore());
        verify(resumeTextExtractor, times(1)).extractText(docxResume.getFilePath());
        verify(aiService, times(1)).analyzeResumeText(anyString());
    }

    @Test
    void analyzeResume_Cached_Success() {
        when(resumeRepository.findByUser(student)).thenReturn(Optional.of(pdfResume));
        when(resumeAnalysisRepository.existsByResume(pdfResume)).thenReturn(true);
        when(resumeAnalysisRepository.findByResume(pdfResume)).thenReturn(Optional.of(mockAnalysis));
        when(resumeAnalysisMapper.toDto(mockAnalysis)).thenReturn(validResponse);

        ResumeAnalysisResponseDto result = resumeAnalysisService.analyzeResume();

        assertNotNull(result);
        assertEquals(85, result.atsScore());

        // Text extractor and AI Service should NOT be invoked
        verify(resumeTextExtractor, never()).extractText(anyString());
        verify(aiService, never()).analyzeResumeText(anyString());
        verify(resumeAnalysisRepository, never()).save(any(ResumeAnalysis.class));
    }

    @Test
    void analyzeResume_MissingResume_ThrowsResourceNotFoundException() {
        when(resumeRepository.findByUser(student)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            resumeAnalysisService.analyzeResume();
        });

        verify(resumeTextExtractor, never()).extractText(anyString());
        verify(aiService, never()).analyzeResumeText(anyString());
    }

    @Test
    void analyzeResume_CorruptedFile_ThrowsResumeExtractionException() {
        when(resumeRepository.findByUser(student)).thenReturn(Optional.of(pdfResume));
        when(resumeAnalysisRepository.existsByResume(pdfResume)).thenReturn(false);
        when(resumeTextExtractor.extractText(pdfResume.getFilePath())).thenThrow(new ResumeExtractionException("File is corrupted"));

        assertThrows(ResumeExtractionException.class, () -> {
            resumeAnalysisService.analyzeResume();
        });

        verify(aiService, never()).analyzeResumeText(anyString());
        verify(resumeAnalysisRepository, never()).save(any(ResumeAnalysis.class));
    }

    @Test
    void analyzeResume_GeminiApiFailure_ThrowsGeminiApiException() {
        when(resumeRepository.findByUser(student)).thenReturn(Optional.of(pdfResume));
        when(resumeAnalysisRepository.existsByResume(pdfResume)).thenReturn(false);
        when(resumeTextExtractor.extractText(pdfResume.getFilePath())).thenReturn("Mocked PDF Resume Text Content");
        when(aiService.analyzeResumeText("Mocked PDF Resume Text Content")).thenThrow(new GeminiApiException("Quota exceeded"));

        assertThrows(GeminiApiException.class, () -> {
            resumeAnalysisService.analyzeResume();
        });

        verify(resumeAnalysisRepository, never()).save(any(ResumeAnalysis.class));
    }

    @Test
    void analyzeResume_IncompleteResponse_ThrowsResumeAnalysisException() {
        when(resumeRepository.findByUser(student)).thenReturn(Optional.of(pdfResume));
        when(resumeAnalysisRepository.existsByResume(pdfResume)).thenReturn(false);
        when(resumeTextExtractor.extractText(pdfResume.getFilePath())).thenReturn("Mocked PDF Resume Text Content");
        when(aiService.analyzeResumeText("Mocked PDF Resume Text Content")).thenThrow(new ResumeAnalysisException("Invalid analysis: strengths list is missing or empty"));

        assertThrows(ResumeAnalysisException.class, () -> {
            resumeAnalysisService.analyzeResume();
        });

        verify(resumeAnalysisRepository, never()).save(any(ResumeAnalysis.class));
    }
}
