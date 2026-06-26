package com.placement.platform.service;

import com.placement.platform.dto.AddCompanyRequestDto;
import com.placement.platform.dto.CompanyDto;
import com.placement.platform.dto.CompanyDetailResponseDto;
import com.placement.platform.dto.CompanyHubResponseDto;
import com.placement.platform.dto.CreateCompanyRequestDto;
import com.placement.platform.dto.EligibilityResponseDto;
import com.placement.platform.entity.Skill;
import com.placement.platform.entity.TargetCompany;
import com.placement.platform.entity.User;
import com.placement.platform.entity.UserTargetCompany;
import com.placement.platform.exception.CompanyAlreadyExistsException;
import com.placement.platform.exception.ResourceNotFoundException;
import com.placement.platform.exception.UserNotFoundException;
import com.placement.platform.mapper.TargetCompanyMapper;
import com.placement.platform.repository.SkillRepository;
import com.placement.platform.repository.TargetCompanyRepository;
import com.placement.platform.repository.UserRepository;
import com.placement.platform.repository.UserTargetCompanyRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final UserRepository userRepository;
    private final TargetCompanyRepository targetCompanyRepository;
    private final UserTargetCompanyRepository userTargetCompanyRepository;
    private final TargetCompanyMapper targetCompanyMapper;
    private final EligibilityService eligibilityService;
    private final SkillRepository skillRepository;

    public CompanyServiceImpl(
            UserRepository userRepository,
            TargetCompanyRepository targetCompanyRepository,
            UserTargetCompanyRepository userTargetCompanyRepository,
            TargetCompanyMapper targetCompanyMapper,
            EligibilityService eligibilityService,
            SkillRepository skillRepository
    ) {
        this.userRepository = userRepository;
        this.targetCompanyRepository = targetCompanyRepository;
        this.userTargetCompanyRepository = userTargetCompanyRepository;
        this.targetCompanyMapper = targetCompanyMapper;
        this.eligibilityService = eligibilityService;
        this.skillRepository = skillRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyDto> getUserCompanies() {
        User user = getAuthenticatedUser();
        List<UserTargetCompany> userCompanies = userTargetCompanyRepository.findByUserId(user.getId());
        return userCompanies.stream()
                .map(userCompany -> targetCompanyMapper.toDto(userCompany.getCompany()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CompanyDto addCompany(AddCompanyRequestDto request) {
        User user = getAuthenticatedUser();
        String companyName = request.name().trim();

        // 1. Find or create global target company
        TargetCompany company = targetCompanyRepository.findByNameIgnoreCase(companyName)
                .orElseGet(() -> targetCompanyRepository.save(new TargetCompany(companyName)));

        // 2. Check if mapping already exists for this user to prevent duplicates
        Optional<UserTargetCompany> existingMapping = userTargetCompanyRepository.findByUserIdAndCompanyId(
                user.getId(),
                company.getId()
        );

        if (existingMapping.isPresent()) {
            throw new CompanyAlreadyExistsException("Company '" + companyName + "' already exists in your profile.");
        }

        // 3. Create and save the mapping
        UserTargetCompany userTargetCompany = new UserTargetCompany(user, company);
        userTargetCompanyRepository.save(userTargetCompany);

        return targetCompanyMapper.toDto(company);
    }

    @Override
    @Transactional
    public void deleteCompany(Long companyId) {
        User user = getAuthenticatedUser();

        UserTargetCompany userTargetCompany = userTargetCompanyRepository.findByUserIdAndCompanyId(user.getId(), companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company mapping not found in your profile."));

        userTargetCompanyRepository.delete(userTargetCompany);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyHubResponseDto> getAllCompanies() {
        User user = getAuthenticatedUser();
        List<TargetCompany> companies = targetCompanyRepository.findAll();
        return companies.stream()
                .map(company -> {
                    EligibilityResponseDto eligibility = eligibilityService.checkEligibility(user, company);
                    return targetCompanyMapper.toHubDto(company, eligibility.eligible());
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyDetailResponseDto getCompanyDetails(Long companyId) {
        User user = getAuthenticatedUser();
        TargetCompany company = targetCompanyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + companyId));
        EligibilityResponseDto eligibility = eligibilityService.checkEligibility(user, company);
        return targetCompanyMapper.toDetailDto(company, eligibility);
    }

    @Override
    @Transactional
    public CompanyDetailResponseDto createCompany(CreateCompanyRequestDto request) {
        User user = getAuthenticatedUser();
        String name = request.name().trim();

        if (targetCompanyRepository.findByNameIgnoreCase(name).isPresent()) {
            throw new CompanyAlreadyExistsException("Company '" + name + "' already exists.");
        }

        TargetCompany company = new TargetCompany(name);
        company.setDescription(request.description());
        company.setMinCgpa(request.minCgpa());

        if (request.eligibleBranches() != null) {
            company.setEligibleBranches(new HashSet<>(request.eligibleBranches()));
        }

        if (request.requiredSkills() != null) {
            Set<Skill> skills = new HashSet<>();
            for (String skillName : request.requiredSkills()) {
                if (skillName != null && !skillName.isBlank()) {
                    String trimmedSkillName = skillName.trim();
                    Skill skill = skillRepository.findByNameIgnoreCase(trimmedSkillName)
                            .orElseGet(() -> skillRepository.save(new Skill(trimmedSkillName)));
                    skills.add(skill);
                }
            }
            company.setRequiredSkills(skills);
        }

        TargetCompany savedCompany = targetCompanyRepository.save(company);
        EligibilityResponseDto eligibility = eligibilityService.checkEligibility(user, savedCompany);
        return targetCompanyMapper.toDetailDto(savedCompany, eligibility);
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
