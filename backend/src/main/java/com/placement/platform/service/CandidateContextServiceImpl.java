package com.placement.platform.service;

import com.placement.platform.dto.CandidateContext;
import com.placement.platform.entity.*;
import com.placement.platform.exception.*;
import com.placement.platform.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidateContextServiceImpl implements CandidateContextService {

    private final ResumeRepository resumeRepository;
    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final UserSkillRepository userSkillRepository;
    private final UserTargetCompanyRepository userTargetCompanyRepository;

    public CandidateContextServiceImpl(
            ResumeRepository resumeRepository,
            ResumeAnalysisRepository resumeAnalysisRepository,
            UserSkillRepository userSkillRepository,
            UserTargetCompanyRepository userTargetCompanyRepository
    ) {
        this.resumeRepository = resumeRepository;
        this.resumeAnalysisRepository = resumeAnalysisRepository;
        this.userSkillRepository = userSkillRepository;
        this.userTargetCompanyRepository = userTargetCompanyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public CandidateContext buildContext(User user) {
        // 1. Validate student profile completeness
        if (user.getName() == null || user.getName().trim().isEmpty() ||
                user.getCollege() == null || user.getCollege().trim().isEmpty() ||
                user.getDegree() == null || user.getDegree().trim().isEmpty() ||
                user.getBranch() == null || user.getBranch().trim().isEmpty() ||
                user.getCgpa() == null) {
            throw new ProfileIncompleteException("Student profile is incomplete. Please complete your profile details first.");
        }

        // Validate projects and internship (must not be empty/whitespace)
        if (user.getProjects() == null || user.getProjects().trim().isEmpty()) {
            throw new ProfileIncompleteException("Projects details are missing or incomplete.");
        }

        if (user.getInternship() == null || user.getInternship().trim().isEmpty()) {
            throw new ProfileIncompleteException("Internship details are missing or incomplete.");
        }

        // 2. Validate target role
        if (user.getTargetRole() == null || user.getTargetRole().trim().isEmpty()) {
            throw new TargetRoleMissingException("Target role is missing. Please set your target role first.");
        }

        // 3. Validate resume existence
        Resume resume = resumeRepository.findByUser(user)
                .orElseThrow(() -> new ResumeMissingException("No resume found. Please upload your resume first."));

        // 4. Validate resume analysis existence
        ResumeAnalysis resumeAnalysis = resumeAnalysisRepository.findByResume(resume)
                .orElseThrow(() -> new ResumeAnalysisMissingException("Resume analysis is missing. Please analyze your resume first."));

        // 5. Retrieve user skills
        List<String> skills = userSkillRepository.findByUserId(user.getId())
                .stream()
                .map(userSkill -> userSkill.getSkill().getName())
                .collect(Collectors.toList());

        // 6. Retrieve target companies
        List<String> targetCompanies = userTargetCompanyRepository.findByUserId(user.getId())
                .stream()
                .map(userCompany -> userCompany.getCompany().getName())
                .collect(Collectors.toList());

        // Build candidate context object
        return new CandidateContext(
                user.getName(),
                user.getCollege(),
                user.getDegree(),
                user.getBranch(),
                user.getCgpa(),
                user.getTargetRole(),
                user.getProjects(),
                user.getInternship(),
                skills,
                resumeAnalysis.getSummary(),
                resumeAnalysis.getStrengths(),
                resumeAnalysis.getWeaknesses(),
                resumeAnalysis.getMissingSkills(),
                targetCompanies
        );
    }
}
