package com.placement.platform.dto.dashboard;

public record ReadinessBreakdownDto(
    Integer resumeScore,
    Integer interviewScore,
    Integer profileScore,
    Integer skillScore,
    Integer companyScore,
    Integer resumeWeight,
    Integer interviewWeight,
    Integer profileWeight,
    Integer skillWeight,
    Integer companyWeight,
    Double weightedResumeContribution,
    Double weightedInterviewContribution,
    Double weightedProfileContribution,
    Double weightedSkillContribution,
    Double weightedCompanyContribution
) {}
