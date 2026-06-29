package com.placement.platform.service;

import com.placement.platform.dto.ProfileResponseDto;
import com.placement.platform.dto.UpdateProfileRequestDto;
import com.placement.platform.entity.User;
import com.placement.platform.exception.UserNotFoundException;
import com.placement.platform.mapper.ProfileMapper;
import com.placement.platform.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.placement.platform.service.InterviewProfileService;
import java.util.Objects;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;
    private final InterviewProfileService interviewProfileService;

    public ProfileServiceImpl(
            UserRepository userRepository,
            ProfileMapper profileMapper,
            InterviewProfileService interviewProfileService
    ) {
        this.userRepository = userRepository;
        this.profileMapper = profileMapper;
        this.interviewProfileService = interviewProfileService;
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponseDto getCurrentUserProfile() {
        User user = getAuthenticatedUser();
        return profileMapper.toDto(user);
    }

    @Override
    @Transactional
    public ProfileResponseDto updateProfile(UpdateProfileRequestDto request) {
        User user = getAuthenticatedUser();
        
        boolean targetRoleChanged = !Objects.equals(user.getTargetRole(), request.targetRole());
        boolean projectsChanged = !Objects.equals(user.getProjects(), request.projects());
        boolean internshipChanged = !Objects.equals(user.getInternship(), request.internship());

        user.setName(request.name());
        user.setCollege(request.college());
        user.setDegree(request.degree());
        user.setBranch(request.branch());
        user.setCgpa(request.cgpa());
        user.setGraduationYear(request.graduationYear());
        user.setTargetRole(request.targetRole());
        user.setProjects(request.projects());
        user.setInternship(request.internship());
        
        User updatedUser = userRepository.save(user);

        if (targetRoleChanged || projectsChanged || internshipChanged) {
            interviewProfileService.incrementProfileVersion(updatedUser);
        }

        return profileMapper.toDto(updatedUser);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("User is not authenticated");
        }
        
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }
}
