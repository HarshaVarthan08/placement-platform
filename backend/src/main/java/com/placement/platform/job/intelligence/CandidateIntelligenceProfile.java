package com.placement.platform.job.intelligence;

import java.math.BigDecimal;
import java.util.List;

/**
 * An immutable profile representing all aggregated intelligence about a candidate,
 * collected from multiple platform modules (Profile, Resume, Interview, Eligibility, Dashboard).
 */
public record CandidateIntelligenceProfile(
    Long userId,
    List<String> preferredRoles,
    List<String> preferredCompanies,
    List<String> skills,
    Integer experience,
    String education,
    BigDecimal cgpa,
    Integer resumeATSScore,
    List<String> resumeExtractedSkills,
    Integer interviewTechnicalScore,
    Integer interviewCommunicationScore,
    Integer interviewConfidenceScore,
    Integer placementReadinessScore,
    List<String> eligibleCompanies,
    List<String> preferredLocations
) {}
