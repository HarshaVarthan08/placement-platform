package com.placement.platform.service;

import com.placement.platform.dto.EligibilityResponseDto;
import com.placement.platform.entity.Skill;
import com.placement.platform.entity.TargetCompany;
import com.placement.platform.entity.User;
import com.placement.platform.entity.UserSkill;
import com.placement.platform.repository.UserSkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EligibilityServiceImpl implements EligibilityService {

    private final UserSkillRepository userSkillRepository;

    public EligibilityServiceImpl(UserSkillRepository userSkillRepository) {
        this.userSkillRepository = userSkillRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public EligibilityResponseDto checkEligibility(User user, TargetCompany company) {
        // 1. CGPA Check
        boolean cgpaMatched = true;
        BigDecimal companyMinCgpa = company.getMinCgpa();
        if (companyMinCgpa != null) {
            BigDecimal studentCgpa = user.getCgpa();
            if (studentCgpa == null || studentCgpa.compareTo(companyMinCgpa) < 0) {
                cgpaMatched = false;
            }
        }

        // 2. Branch Check
        boolean branchMatched = true;
        Set<String> eligibleBranches = company.getEligibleBranches();
        if (eligibleBranches != null && !eligibleBranches.isEmpty()) {
            String studentBranch = user.getBranch();
            if (studentBranch == null || studentBranch.isBlank()) {
                branchMatched = false;
            } else {
                String normalizedStudentBranch = studentBranch.trim().toLowerCase();
                boolean matchesAny = eligibleBranches.stream()
                        .filter(Objects::nonNull)
                        .map(b -> b.trim().toLowerCase())
                        .anyMatch(normalizedStudentBranch::equals);
                if (!matchesAny) {
                    branchMatched = false;
                }
            }
        }

        // 3. Skills Check
        boolean skillsMatched = true;
        Set<String> missingSkills = new LinkedHashSet<>();
        Set<Skill> companyRequiredSkills = company.getRequiredSkills();
        if (companyRequiredSkills != null && !companyRequiredSkills.isEmpty()) {
            List<UserSkill> userSkills = userSkillRepository.findByUserId(user.getId());
            Set<String> studentSkillNames = userSkills.stream()
                    .map(UserSkill::getSkill)
                    .filter(Objects::nonNull)
                    .map(Skill::getName)
                    .filter(Objects::nonNull)
                    .map(name -> name.trim().toLowerCase())
                    .collect(Collectors.toSet());

            for (Skill reqSkill : companyRequiredSkills) {
                if (reqSkill != null && reqSkill.getName() != null) {
                    String reqSkillNameLower = reqSkill.getName().trim().toLowerCase();
                    if (!studentSkillNames.contains(reqSkillNameLower)) {
                        missingSkills.add(reqSkill.getName());
                    }
                }
            }

            if (!missingSkills.isEmpty()) {
                skillsMatched = false;
            }
        }

        boolean eligible = cgpaMatched && branchMatched && skillsMatched;

        return new EligibilityResponseDto(
                eligible,
                cgpaMatched,
                branchMatched,
                skillsMatched,
                missingSkills
        );
    }
}
