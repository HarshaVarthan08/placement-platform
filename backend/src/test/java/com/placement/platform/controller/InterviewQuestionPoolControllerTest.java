package com.placement.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.placement.platform.dto.QuestionPoolResponseDto;
import com.placement.platform.dto.QuestionPoolStatusDto;
import com.placement.platform.dto.QuestionResponseDto;
import com.placement.platform.entity.*;
import com.placement.platform.mapper.InterviewQuestionPoolMapper;
import com.placement.platform.repository.InterviewQuestionPoolRepository;
import com.placement.platform.repository.InterviewQuestionRepository;
import com.placement.platform.repository.UserRepository;
import com.placement.platform.service.InterviewProfileService;
import com.placement.platform.service.QuestionPoolGenerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InterviewQuestionPoolController.class)
@AutoConfigureMockMvc(addFilters = false)
public class InterviewQuestionPoolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private InterviewProfileService interviewProfileService;

    @MockBean
    private QuestionPoolGenerationService questionPoolGenerationService;

    @MockBean
    private InterviewQuestionPoolRepository interviewQuestionPoolRepository;

    @MockBean
    private InterviewQuestionRepository interviewQuestionRepository;

    @MockBean
    private InterviewQuestionPoolMapper interviewQuestionPoolMapper;

    @MockBean
    private com.placement.platform.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    private User student;
    private InterviewProfile profile;

    @BeforeEach
    void setUp() {
        student = new User();
        student.setId(1L);
        student.setEmail("harsha@gmail.com");

        profile = new InterviewProfile();
        profile.setUser(student);
        profile.setProfileVersion(3);

        // Mock Security Context
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("harsha@gmail.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("harsha@gmail.com")).thenReturn(Optional.of(student));
        when(interviewProfileService.getOrCreateProfile(student)).thenReturn(profile);
    }

    @Test
    void generatePool_PoolAlreadyActiveAndMatchesVersion_ReturnsExisting() throws Exception {
        InterviewQuestionPool existingPool = new InterviewQuestionPool();
        existingPool.setId(50L);
        existingPool.setUser(student);
        existingPool.setProfileVersion(3); // Matches profile version 3
        existingPool.setStatus(QuestionPoolStatus.ACTIVE);

        when(interviewQuestionPoolRepository.findByUserAndStatus(student, QuestionPoolStatus.ACTIVE))
                .thenReturn(Optional.of(existingPool));

        List<InterviewQuestion> questions = Collections.emptyList();
        when(interviewQuestionRepository.findByQuestionPool(existingPool)).thenReturn(questions);

        QuestionPoolResponseDto responseDto = new QuestionPoolResponseDto(
                50L, 3, QuestionPoolStatus.ACTIVE, LocalDateTime.now(), LocalDateTime.now(), Collections.emptyList()
        );
        when(interviewQuestionPoolMapper.toDto(existingPool, questions)).thenReturn(responseDto);

        mockMvc.perform(post("/api/interview/question-pool/generate")
                        .param("difficulty", "MEDIUM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(50))
                .andExpect(jsonPath("$.profileVersion").value(3))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        // Verify generation service was NOT called
        verify(questionPoolGenerationService, never()).generateQuestionPool(any(), any());
    }

    @Test
    void generatePool_NoActivePool_GeneratesNew() throws Exception {
        when(interviewQuestionPoolRepository.findByUserAndStatus(student, QuestionPoolStatus.ACTIVE))
                .thenReturn(Optional.empty());

        InterviewQuestionPool newPool = new InterviewQuestionPool();
        newPool.setId(60L);
        newPool.setUser(student);
        newPool.setProfileVersion(3);
        newPool.setStatus(QuestionPoolStatus.ACTIVE);

        when(questionPoolGenerationService.generateQuestionPool(student, InterviewDifficulty.MEDIUM))
                .thenReturn(newPool);

        List<InterviewQuestion> questions = Collections.emptyList();
        when(interviewQuestionRepository.findByQuestionPool(newPool)).thenReturn(questions);

        QuestionPoolResponseDto responseDto = new QuestionPoolResponseDto(
                60L, 3, QuestionPoolStatus.ACTIVE, LocalDateTime.now(), LocalDateTime.now(), Collections.emptyList()
        );
        when(interviewQuestionPoolMapper.toDto(newPool, questions)).thenReturn(responseDto);

        mockMvc.perform(post("/api/interview/question-pool/generate")
                        .param("difficulty", "MEDIUM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(60))
                .andExpect(jsonPath("$.profileVersion").value(3))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(questionPoolGenerationService, times(1)).generateQuestionPool(student, InterviewDifficulty.MEDIUM);
    }

    @Test
    void getStatus_ActivePoolExists_ReturnsActive() throws Exception {
        InterviewQuestionPool activePool = new InterviewQuestionPool();
        activePool.setUser(student);
        activePool.setProfileVersion(3);
        activePool.setStatus(QuestionPoolStatus.ACTIVE);
        activePool.setGeneratedAt(LocalDateTime.of(2026, 6, 29, 12, 0, 0));

        when(interviewQuestionPoolRepository.findByUserAndStatus(student, QuestionPoolStatus.ACTIVE))
                .thenReturn(Optional.of(activePool));

        mockMvc.perform(get("/api/interview/question-pool/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.profileVersion").value(3)) // current
                .andExpect(jsonPath("$.poolVersion").value(3)); // pool's version
    }

    @Test
    void getStatus_NoPoolExists_ReturnsNotGenerated() throws Exception {
        when(interviewQuestionPoolRepository.findByUserAndStatus(student, QuestionPoolStatus.ACTIVE))
                .thenReturn(Optional.empty());
        when(interviewQuestionPoolRepository.findByUserAndStatus(student, QuestionPoolStatus.OUTDATED))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/interview/question-pool/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("NOT_GENERATED"))
                .andExpect(jsonPath("$.profileVersion").value(3))
                .andExpect(jsonPath("$.poolVersion").isEmpty());
    }
}
