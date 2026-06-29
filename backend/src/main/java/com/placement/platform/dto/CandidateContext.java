package com.placement.platform.dto;

import java.math.BigDecimal;
import java.util.List;

public record CandidateContext(
    String name,
    String college,
    String degree,
    String branch,
    BigDecimal cgpa,
    String targetRole,
    String projects,
    String internship,
    List<String> skills,
    String resumeSummary,
    List<String> resumeStrengths,
    List<String> resumeWeaknesses,
    List<String> resumeMissingSkills,
    List<String> targetCompanies
) {}
