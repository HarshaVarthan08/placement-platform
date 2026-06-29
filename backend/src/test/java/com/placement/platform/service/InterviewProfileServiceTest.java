package com.placement.platform.service;

import com.placement.platform.entity.*;
import com.placement.platform.repository.InterviewProfileRepository;
import com.placement.platform.repository.InterviewQuestionPoolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InterviewProfileServiceTest {

    @Mock
    private InterviewProfileRepository interviewProfileRepository;

    @Mock
    private InterviewQuestionPoolRepository interviewQuestionPoolRepository;

    @InjectMocks
    private InterviewProfileServiceImpl interviewProfileService;

    private User student;

    @BeforeEach
    void setUp() {
        student = new User();
        student.setId(1L);
        student.setEmail("harsha@gmail.com");
    }

    @Test
    void getOrCreateProfile_ProfileExists_ReturnsExisting() {
        InterviewProfile profile = new InterviewProfile();
        profile.setUser(student);
        profile.setProfileVersion(2);

        when(interviewProfileRepository.findByUser(student)).thenReturn(Optional.of(profile));

        InterviewProfile result = interviewProfileService.getOrCreateProfile(student);

        assertNotNull(result);
        assertEquals(2, result.getProfileVersion());
        verify(interviewProfileRepository, never()).save(any(InterviewProfile.class));
    }

    @Test
    void getOrCreateProfile_ProfileDoesNotExist_CreatesNew() {
        when(interviewProfileRepository.findByUser(student)).thenReturn(Optional.empty());
        when(interviewProfileRepository.save(any(InterviewProfile.class))).thenAnswer(inv -> inv.getArgument(0));

        InterviewProfile result = interviewProfileService.getOrCreateProfile(student);

        assertNotNull(result);
        assertEquals(1, result.getProfileVersion());
        assertEquals(student, result.getUser());
        verify(interviewProfileRepository, times(1)).save(any(InterviewProfile.class));
    }

    @Test
    void incrementProfileVersion_IncrementsVersionAndOutdatesPool() {
        InterviewProfile profile = new InterviewProfile();
        profile.setUser(student);
        profile.setProfileVersion(3);

        when(interviewProfileRepository.findByUser(student)).thenReturn(Optional.of(profile));
        when(interviewProfileRepository.save(profile)).thenReturn(profile);

        InterviewQuestionPool activePool = new InterviewQuestionPool();
        activePool.setUser(student);
        activePool.setStatus(QuestionPoolStatus.ACTIVE);

        when(interviewQuestionPoolRepository.findByUserAndStatus(student, QuestionPoolStatus.ACTIVE))
                .thenReturn(Optional.of(activePool));

        interviewProfileService.incrementProfileVersion(student);

        assertEquals(4, profile.getProfileVersion());
        assertEquals(QuestionPoolStatus.OUTDATED, activePool.getStatus());

        verify(interviewProfileRepository, times(1)).save(profile);
        verify(interviewQuestionPoolRepository, times(1)).save(activePool);
    }
}
