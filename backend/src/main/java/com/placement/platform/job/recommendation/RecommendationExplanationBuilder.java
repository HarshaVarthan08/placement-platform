package com.placement.platform.job.recommendation;

import com.placement.platform.job.matching.MatchComponentResult;
import com.placement.platform.job.matching.RecommendationResult;
import com.placement.platform.job.matching.ScoreBreakdown;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecommendationExplanationBuilder {

    public String buildExplanation(RecommendationResult result) {
        String skillAnalysis = buildSkillSection(result);
        String eligibilityAnalysis = buildEligibilitySection(result);
        String resumeAnalysis = buildResumeSection(result);
        String finalRecommendation = buildFinalSection(result);

        return String.join("\n\n",
                "### RECOMMENDATION EXPLANATION",
                skillAnalysis,
                eligibilityAnalysis,
                resumeAnalysis,
                finalRecommendation
        );
    }

    private String buildSkillSection(RecommendationResult result) {
        int matched = result.matchedSkillCount();
        int totalReq = result.totalRequiredSkills();
        List<String> missing = result.missingSkills();

        StringBuilder sb = new StringBuilder();
        sb.append("#### Skill Analysis\n");
        if (totalReq == 0) {
            sb.append("- No specific required skills are listed for this role.");
        } else {
            sb.append(String.format("- Matched %d of %d required skills (%.0f%% Match).\n",
                    matched, totalReq, result.skillMatchPercentage()));
            if (!missing.isEmpty()) {
                sb.append("- Missing skills: ").append(String.join(", ", missing));
            } else {
                sb.append("- You satisfy all required skill criteria!");
            }
        }
        return sb.toString();
    }

    private String buildEligibilitySection(RecommendationResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("#### Eligibility Analysis\n");

        MatchComponentResult eligibilityRes = result.matchComponentResults().stream()
                .filter(r -> "Eligibility Match".equals(r.componentName()))
                .findFirst()
                .orElse(null);

        if (eligibilityRes != null && eligibilityRes.score() == 100) {
            sb.append("- Eligible: Yes. You satisfy all eligibility and CGPA requirements.");
        } else if (eligibilityRes != null) {
            sb.append("- Eligible: No. ").append(eligibilityRes.reason());
        } else {
            sb.append("- Eligibility criteria not evaluated.");
        }

        return sb.toString();
    }

    private String buildResumeSection(RecommendationResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("#### Resume Analysis\n");

        ScoreBreakdown breakdown = result.scoreBreakdown();
        Integer resumeScore = breakdown != null ? breakdown.resumeScore() : 0;

        if (resumeScore != null && resumeScore > 0) {
            sb.append(String.format("- Your Resume ATS score is %d. ", resumeScore));
            if (resumeScore >= 80) {
                sb.append("Your resume shows strong alignment with platform averages.");
            } else {
                sb.append("Consider updating your resume layout and adding target keywords to improve visibility.");
            }
        } else {
            sb.append("- Resume analysis is currently unavailable. Please upload a resume for full matching.");
        }

        return sb.toString();
    }

    private String buildFinalSection(RecommendationResult result) {
        int score = result.finalMatchScore();
        boolean isEligible = result.matchComponentResults().stream()
                .filter(r -> "Eligibility Match".equals(r.componentName()))
                .map(r -> r.score() == 100)
                .findFirst()
                .orElse(true);

        RecommendationLevel level = RecommendationBuilder.resolveLevel(score, isEligible);
        RecommendationPriority priority = RecommendationBuilder.resolvePriority(score, isEligible);
        RecommendationAction action = RecommendationBuilder.resolveAction(score, isEligible);

        StringBuilder sb = new StringBuilder();
        sb.append("#### Final Recommendation\n");
        sb.append(String.format("- **Level**: %s (%d/100 match score)\n", level, score));
        sb.append(String.format("- **Priority**: %s\n", priority));
        sb.append(String.format("- **Suggested Action**: %s", action.name().replace("_", " ")));

        return sb.toString();
    }
}
