package com.placement.platform.service;

import com.placement.platform.dto.AddSkillRequestDto;
import com.placement.platform.dto.SkillDto;
import com.placement.platform.entity.Skill;
import com.placement.platform.entity.User;
import com.placement.platform.entity.UserSkill;
import com.placement.platform.exception.ResourceNotFoundException;
import com.placement.platform.exception.SkillAlreadyExistsException;
import com.placement.platform.exception.UserNotFoundException;
import com.placement.platform.mapper.SkillMapper;
import com.placement.platform.repository.SkillRepository;
import com.placement.platform.repository.UserRepository;
import com.placement.platform.repository.UserSkillRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.placement.platform.service.InterviewProfileService;

@Service
public class SkillServiceImpl implements SkillService {

    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final UserSkillRepository userSkillRepository;
    private final SkillMapper skillMapper;
    private final InterviewProfileService interviewProfileService;

    public SkillServiceImpl(
            UserRepository userRepository,
            SkillRepository skillRepository,
            UserSkillRepository userSkillRepository,
            SkillMapper skillMapper,
            InterviewProfileService interviewProfileService
    ) {
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.userSkillRepository = userSkillRepository;
        this.skillMapper = skillMapper;
        this.interviewProfileService = interviewProfileService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillDto> getUserSkills() {
        User user = getAuthenticatedUser();
        List<UserSkill> userSkills = userSkillRepository.findByUserId(user.getId());
        return userSkills.stream()
                .map(userSkill -> skillMapper.toDto(userSkill.getSkill()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SkillDto addSkill(AddSkillRequestDto request) {
        User user = getAuthenticatedUser();
        String skillName = request.name().trim();

        // 1. Find or create global skill
        Skill skill = skillRepository.findByNameIgnoreCase(skillName)
                .orElseGet(() -> skillRepository.save(new Skill(skillName)));

        // 2. Check if mapping already exists for this user to prevent duplicates
        Optional<UserSkill> existingMapping = userSkillRepository.findByUserIdAndSkillId(
                user.getId(),
                skill.getId()
        );

        if (existingMapping.isPresent()) {
            throw new SkillAlreadyExistsException("Skill '" + skillName + "' already exists in your profile.");
        }

        // 3. Create and save the mapping
        UserSkill userSkill = new UserSkill(user, skill);
        userSkillRepository.save(userSkill);

        interviewProfileService.incrementProfileVersion(user);

        return skillMapper.toDto(skill);
    }

    @Override
    @Transactional
    public void deleteSkill(Long skillId) {
        User user = getAuthenticatedUser();

        UserSkill userSkill = userSkillRepository.findByUserIdAndSkillId(user.getId(), skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill mapping not found in your profile."));

        userSkillRepository.delete(userSkill);
        interviewProfileService.incrementProfileVersion(user);
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
