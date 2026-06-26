package com.placement.platform.mapper;

import com.placement.platform.dto.CompanyDto;
import com.placement.platform.dto.CompanyDetailResponseDto;
import com.placement.platform.dto.CompanyHubResponseDto;
import com.placement.platform.dto.EligibilityResponseDto;
import com.placement.platform.dto.SkillDto;
import com.placement.platform.entity.TargetCompany;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TargetCompanyMapper {

    private final SkillMapper skillMapper;

    public TargetCompanyMapper(SkillMapper skillMapper) {
        this.skillMapper = skillMapper;
    }

    public CompanyDto toDto(TargetCompany company) {
        if (company == null) {
            return null;
        }
        return new CompanyDto(
                company.getId(),
                company.getName()
        );
    }

    public CompanyHubResponseDto toHubDto(TargetCompany company, boolean eligible) {
        if (company == null) {
            return null;
        }
        Set<SkillDto> skills = company.getRequiredSkills() == null ? Set.of() :
                company.getRequiredSkills().stream()
                        .map(skillMapper::toDto)
                        .collect(Collectors.toSet());
        return new CompanyHubResponseDto(
                company.getId(),
                company.getName(),
                company.getDescription(),
                company.getMinCgpa(),
                company.getEligibleBranches() == null ? Set.of() : new HashSet<>(company.getEligibleBranches()),
                skills,
                eligible
        );
    }

    public CompanyDetailResponseDto toDetailDto(TargetCompany company, EligibilityResponseDto eligibility) {
        if (company == null) {
            return null;
        }
        Set<SkillDto> skills = company.getRequiredSkills() == null ? Set.of() :
                company.getRequiredSkills().stream()
                        .map(skillMapper::toDto)
                        .collect(Collectors.toSet());
        return new CompanyDetailResponseDto(
                company.getId(),
                company.getName(),
                company.getDescription(),
                company.getMinCgpa(),
                company.getEligibleBranches() == null ? Set.of() : new HashSet<>(company.getEligibleBranches()),
                skills,
                eligibility
        );
    }
}
