package com.placement.platform.premium.enums;

public enum PremiumFeature {
    AI_RESUME_OPTIMIZER(
        "AI Resume Optimizer",
        "Optimize your resume for a selected job using AI.",
        "Resume",
        "sparkles",
        "Q4 2026"
    ),
    AI_CAREER_COACH(
        "AI Career Coach",
        "Receive personalized AI career guidance.",
        "Career",
        "compass",
        "Q4 2026"
    ),
    AI_INTERVIEW_COACH(
        "AI Interview Coach",
        "Practice interviews with AI-powered feedback.",
        "Interview",
        "briefcase",
        "Q4 2026"
    ),
    AI_COVER_LETTER(
        "AI Cover Letter",
        "Generate personalized cover letters for jobs.",
        "Resume",
        "document-text",
        "Q4 2026"
    ),
    AI_LEARNING_ROADMAP(
        "AI Learning Roadmap",
        "Get custom-tailored skill learning paths.",
        "Career",
        "academic-cap",
        "Q4 2026"
    ),
    COMPANY_PREPARATION_PLUS(
        "Company Preparation Plus",
        "Get advanced preparation tips for target companies.",
        "Preparation",
        "office-building",
        "Q4 2026"
    ),
    AI_JOB_MATCH_EXPLANATION(
        "AI Job Match Explanation",
        "Understand exactly why a job fits your profile.",
        "Job",
        "search-circle",
        "Q4 2026"
    );

    private final String displayName;
    private final String description;
    private final String category;
    private final String iconKey;
    private final String estimatedLaunch;

    PremiumFeature(String displayName, String description, String category, String iconKey, String estimatedLaunch) {
        this.displayName = displayName;
        this.description = description;
        this.category = category;
        this.iconKey = iconKey;
        this.estimatedLaunch = estimatedLaunch;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getIconKey() {
        return iconKey;
    }

    public String getEstimatedLaunch() {
        return estimatedLaunch;
    }
}
