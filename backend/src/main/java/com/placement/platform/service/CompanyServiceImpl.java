package com.placement.platform.service;

import com.placement.platform.dto.AddCompanyRequestDto;
import com.placement.platform.dto.CompanyDto;
import com.placement.platform.entity.TargetCompany;
import com.placement.platform.entity.User;
import com.placement.platform.entity.UserTargetCompany;
import com.placement.platform.exception.CompanyAlreadyExistsException;
import com.placement.platform.exception.ResourceNotFoundException;
import com.placement.platform.exception.UserNotFoundException;
import com.placement.platform.mapper.TargetCompanyMapper;
import com.placement.platform.repository.TargetCompanyRepository;
import com.placement.platform.repository.UserRepository;
import com.placement.platform.repository.UserTargetCompanyRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final UserRepository userRepository;
    private final TargetCompanyRepository targetCompanyRepository;
    private final UserTargetCompanyRepository userTargetCompanyRepository;
    private final TargetCompanyMapper targetCompanyMapper;

    public CompanyServiceImpl(
            UserRepository userRepository,
            TargetCompanyRepository targetCompanyRepository,
            UserTargetCompanyRepository userTargetCompanyRepository,
            TargetCompanyMapper targetCompanyMapper
    ) {
        this.userRepository = userRepository;
        this.targetCompanyRepository = targetCompanyRepository;
        this.userTargetCompanyRepository = userTargetCompanyRepository;
        this.targetCompanyMapper = targetCompanyMapper;
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
