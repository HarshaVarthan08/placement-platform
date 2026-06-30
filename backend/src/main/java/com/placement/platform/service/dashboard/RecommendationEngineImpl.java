package com.placement.platform.service.dashboard;

import com.placement.platform.dto.dashboard.RecommendationCard;
import com.placement.platform.dto.dashboard.RecommendationDto;
import com.placement.platform.entity.*;
import com.placement.platform.service.dashboard.PlacementDataAggregator.PlacementData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationEngineImpl implements RecommendationEngine {

    @Value("${application.dashboard.ats.threshold:80}")
    private int atsThreshold;

    @Value("${application.dashboard.health.resume-days:30}")
    private int resumeDaysLimit;

    @Value("${application.dashboard.health.interview-days:30}")
    private int interviewDaysLimit;

    @Override
    public RecommendationCard generateRecommendations(User user, PlacementData data) {
        List<RecommendationDto> rawRecommendations = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // Rule 1: Skill Gaps (Resume missing + Interview Weakness/Recommended Topic match)
        if (data.resumeAnalysis().isPresent()) {
            ResumeAnalysis ra = data.resumeAnalysis().get();
            List<String> missingSkills = ra.getMissingSkills();
            
            // Gather interview weaknesses
            Set<String> interviewWeaknesses = new HashSet<>();
            if (data.latestEvaluation().isPresent()) {
                InterviewEvaluation ie = data.latestEvaluation().get();
                if (ie.getWeaknesses() != null) {
                    for (String w : ie.getWeaknesses()) {
                        interviewWeaknesses.add(w.toLowerCase().trim());
                    }
                }
                if (ie.getRecommendedTopics() != null) {
                    for (String t : ie.getRecommendedTopics()) {
                        interviewWeaknesses.add(t.toLowerCase().trim());
                    }
                }
            }

            if (missingSkills != null) {
                for (String skill : missingSkills) {
                    String skillLower = skill.toLowerCase().trim();
                    boolean weakInInterview = interviewWeaknesses.stream()
                            .anyMatch(w -> w.contains(skillLower) || skillLower.contains(w));
                    
                    if (weakInInterview) {
                        rawRecommendations.add(new RecommendationDto(
                                "Focus on " + skill + " to address resume and interview gaps",
                                "HIGH",
                                90,
                                96,
                                "Both your resume analysis and mock interviews indicate a skill gap in " + skill + ".",
                                "Practice mock questions on " + skill + " and add projects related to it.",
                                "Resume Analysis & Interview Evaluation"
                        ));
                    }
                }
            }
        }

        // Rule 2: ATS Score < configured threshold
        if (data.resumeAnalysis().isPresent()) {
            int atsScore = data.resumeAnalysis().get().getAtsScore();
            if (atsScore < atsThreshold) {
                int confidence = atsScore < 50 ? 95 : 90;
                rawRecommendations.add(new RecommendationDto(
                        "Improve Resume ATS Score",
                        "HIGH",
                        85,
                        confidence,
                        "Your resume ATS score (" + atsScore + ") is below the target threshold of " + atsThreshold + ".",
                        "Incorporate missing skills and review suggestions in the Resume Analysis tab.",
                        "Resume Analysis"
                ));
            }
        } else {
            // Missing Resume Fallback
            rawRecommendations.add(new RecommendationDto(
                    "Upload Your Resume",
                    "HIGH",
                    95,
                    100,
                    "You haven't uploaded a resume yet.",
                    "Upload your resume in PDF or Word format to generate an ATS report.",
                    "Resume Analysis"
            ));
        }

        // Rule 3: Latest Interview Verdict = NO_HIRE
        if (data.latestEvaluation().isPresent()) {
            InterviewEvaluation ie = data.latestEvaluation().get();
            if (ie.getVerdict() == Verdict.NO_HIRE) {
                rawRecommendations.add(new RecommendationDto(
                        "Schedule a Mock Interview",
                        "HIGH",
                        88,
                        92,
                        "Your latest mock interview verdict was NO_HIRE.",
                        "Take another mock interview to practice communication and problem-solving.",
                        "Interview Evaluation"
                ));
            }
        } else {
            // Missing Interview Fallback
            rawRecommendations.add(new RecommendationDto(
                    "Start Your First Practice Interview",
                    "HIGH",
                    80,
                    95,
                    "You have not completed any interviews on the platform.",
                    "Take a learning interview to get comfortable with the interface.",
                    "Interview Evaluation"
                ));
        }

        // Rule 4: Profile Completion < 100%
        int profileCompletion = calculateProfileCompletion(user);
        if (profileCompletion < 100) {
            rawRecommendations.add(new RecommendationDto(
                    "Complete Your Profile",
                    "MEDIUM",
                    70,
                    100,
                    "Your profile is only " + profileCompletion + "% complete. Some key details are missing.",
                    "Go to the Profile page and fill in all details including CGPA, graduation year, projects, and internships.",
                    "Candidate Profile"
            ));
        }

        // Rule 5: Resume Analysis is stale
        if (data.resumeAnalysis().isPresent()) {
            LocalDateTime resumeDate = data.resumeAnalysis().get().getUpdatedAt();
            long resumeAgeDays = ChronoUnit.DAYS.between(resumeDate, now);
            if (resumeAgeDays > resumeDaysLimit) {
                rawRecommendations.add(new RecommendationDto(
                        "Refresh Resume Analysis",
                        "MEDIUM",
                        60,
                        85,
                        "Your resume analysis is " + resumeAgeDays + " days old, which is older than the recommended " + resumeDaysLimit + " days.",
                        "Upload an updated resume or re-run the ATS analysis to get fresh suggestions.",
                        "Dashboard Health"
                ));
            }
        }

        // Rule 6: No interview taken in configured number of days
        if (data.latestEvaluation().isPresent()) {
            LocalDateTime lastInterviewDate = data.latestEvaluation().get().getEvaluatedAt();
            long interviewAgeDays = ChronoUnit.DAYS.between(lastInterviewDate, now);
            if (interviewAgeDays > interviewDaysLimit) {
                rawRecommendations.add(new RecommendationDto(
                        "Take a Practice Interview",
                        "MEDIUM",
                        65,
                        90,
                        "You haven't taken any interviews in the last " + interviewAgeDays + " days.",
                        "Take a mock or learning interview to keep your skills sharp.",
                        "Dashboard Health"
                ));
            }
        }

        // Deduplicate recommendations by Title
        List<RecommendationDto> deduplicated = deduplicate(rawRecommendations);

        // Sort: Highest priority score first
        deduplicated.sort((r1, r2) -> Integer.compare(r2.priorityScore(), r1.priorityScore()));

        // Resolve overall status
        String status = deduplicated.isEmpty() ? "NOT_AVAILABLE" : "AVAILABLE";

        return new RecommendationCard(status, deduplicated, now);
    }

    private int calculateProfileCompletion(User user) {
        int filled = 0;
        if (user.getName() != null && !user.getName().trim().isEmpty()) filled++;
        if (user.getCollege() != null && !user.getCollege().trim().isEmpty()) filled++;
        if (user.getDegree() != null && !user.getDegree().trim().isEmpty()) filled++;
        if (user.getBranch() != null && !user.getBranch().trim().isEmpty()) filled++;
        if (user.getCgpa() != null) filled++;
        if (user.getGraduationYear() != null) filled++;
        if (user.getTargetRole() != null && !user.getTargetRole().trim().isEmpty()) filled++;
        if (user.getProjects() != null && !user.getProjects().trim().isEmpty()) filled++;
        if (user.getInternship() != null && !user.getInternship().trim().isEmpty()) filled++;

        return (int) Math.round((filled / 9.0) * 100.0);
    }

    private List<RecommendationDto> deduplicate(List<RecommendationDto> recommendations) {
        Map<String, RecommendationDto> merged = new LinkedHashMap<>();

        for (RecommendationDto rec : recommendations) {
            String titleLower = rec.title().toLowerCase().trim();
            if (merged.containsKey(titleLower)) {
                RecommendationDto existing = merged.get(titleLower);
                
                // Keep highest priority and score
                int newScore = Math.max(existing.priorityScore(), rec.priorityScore());
                String newPriority = newScore >= 80 ? "HIGH" : (newScore >= 60 ? "MEDIUM" : "LOW");
                
                // Boost confidence since multiple rules trigger it
                int newConfidence = Math.min(100, Math.max(existing.confidenceScore(), rec.confidenceScore()) + 5);
                
                // Merge reasons
                String newReason = existing.reason().contains(rec.reason()) ? existing.reason() : existing.reason() + " | " + rec.reason();
                
                // Merge triggers
                String newTriggeredBy;
                if (existing.triggeredBy().equalsIgnoreCase(rec.triggeredBy())) {
                    newTriggeredBy = existing.triggeredBy();
                } else if (existing.triggeredBy().contains(rec.triggeredBy())) {
                    newTriggeredBy = existing.triggeredBy();
                } else if (rec.triggeredBy().contains(existing.triggeredBy())) {
                    newTriggeredBy = rec.triggeredBy();
                } else {
                    newTriggeredBy = "Multiple Sources (" + existing.triggeredBy() + ", " + rec.triggeredBy() + ")";
                }

                merged.put(titleLower, new RecommendationDto(
                        existing.title(),
                        newPriority,
                        newScore,
                        newConfidence,
                        newReason,
                        existing.suggestedAction(), // Keep action of the highest priority
                        newTriggeredBy
                ));
            } else {
                merged.put(titleLower, rec);
            }
        }

        return new ArrayList<>(merged.values());
    }
}
