package com.placement.platform.service.dashboard;

import com.placement.platform.dto.dashboard.DashboardHealthCard;
import com.placement.platform.dto.dashboard.HealthMetricDto;
import com.placement.platform.entity.User;
import com.placement.platform.service.dashboard.PlacementDataAggregator.PlacementData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardHealthServiceImpl implements DashboardHealthService {

    @Value("${application.dashboard.health.resume-days:30}")
    private int resumeDaysLimit;

    @Value("${application.dashboard.health.interview-days:30}")
    private int interviewDaysLimit;

    @Value("${application.dashboard.health.profile-days:60}")
    private int profileDaysLimit;

    @Override
    public DashboardHealthCard calculateHealth(User user, PlacementData data) {
        LocalDateTime now = LocalDateTime.now();

        // 1. Resume Health
        HealthMetricDto resumeHealth = calculateResumeHealth(data, now);

        // 2. Interview Health
        HealthMetricDto interviewHealth = calculateInterviewHealth(data, now);

        // 3. Profile Health
        HealthMetricDto profileHealth = calculateProfileHealth(user, now);

        // 4. Overall Health Status
        String overallStatus = "GOOD";
        if (resumeHealth.status().equals("CRITICAL") ||
                interviewHealth.status().equals("CRITICAL") ||
                profileHealth.status().equals("CRITICAL")) {
            overallStatus = "CRITICAL";
        } else if (resumeHealth.status().equals("STALE") ||
                interviewHealth.status().equals("STALE") ||
                profileHealth.status().equals("STALE")) {
            overallStatus = "STALE";
        }

        // Build overall message
        List<String> flags = new ArrayList<>();
        if (!resumeHealth.status().equals("GOOD")) {
            flags.add("Resume: " + resumeHealth.status());
        }
        if (!interviewHealth.status().equals("GOOD")) {
            flags.add("Interview: " + interviewHealth.status());
        }
        if (!profileHealth.status().equals("GOOD")) {
            flags.add("Profile: " + profileHealth.status());
        }

        String overallMessage = flags.isEmpty()
                ? "All data is fresh and complete. Excellent job!"
                : "The following components require attention: " + String.join(", ", flags);

        return new DashboardHealthCard(
                "AVAILABLE",
                resumeHealth,
                interviewHealth,
                profileHealth,
                overallStatus,
                overallMessage,
                now
        );
    }

    private HealthMetricDto calculateResumeHealth(PlacementData data, LocalDateTime now) {
        if (data.resumeAnalysis().isEmpty()) {
            return new HealthMetricDto(
                    null,
                    "CRITICAL",
                    "No resume analysis is available. Please analyze your resume.",
                    null
            );
        }

        LocalDateTime updatedAt = data.resumeAnalysis().get().getUpdatedAt();
        long ageDays = ChronoUnit.DAYS.between(updatedAt, now);

        String status = "GOOD";
        String reason = "Resume analysis is fresh (" + ageDays + " days old).";

        if (ageDays > 2 * resumeDaysLimit) {
            status = "CRITICAL";
            reason = "Resume analysis is " + ageDays + " days old, exceeding critical limit (" + (2 * resumeDaysLimit) + " days).";
        } else if (ageDays > resumeDaysLimit) {
            status = "STALE";
            reason = "Resume analysis is " + ageDays + " days old, exceeding recommended limit (" + resumeDaysLimit + " days).";
        }

        return new HealthMetricDto(ageDays, status, reason, updatedAt);
    }

    private HealthMetricDto calculateInterviewHealth(PlacementData data, LocalDateTime now) {
        if (data.latestEvaluation().isEmpty()) {
            return new HealthMetricDto(
                    null,
                    "CRITICAL",
                    "No completed mock or practice interviews found.",
                    null
            );
        }

        LocalDateTime evaluatedAt = data.latestEvaluation().get().getEvaluatedAt();
        long ageDays = ChronoUnit.DAYS.between(evaluatedAt, now);

        String status = "GOOD";
        String reason = "Your interview practice is up to date (" + ageDays + " days ago).";

        if (ageDays > 2 * interviewDaysLimit) {
            status = "CRITICAL";
            reason = "Your last interview was " + ageDays + " days ago, exceeding critical threshold (" + (2 * interviewDaysLimit) + " days).";
        } else if (ageDays > interviewDaysLimit) {
            status = "STALE";
            reason = "Your last interview was " + ageDays + " days ago, exceeding recommended threshold (" + interviewDaysLimit + " days).";
        }

        return new HealthMetricDto(ageDays, status, reason, evaluatedAt);
    }

    private HealthMetricDto calculateProfileHealth(User user, LocalDateTime now) {
        int completion = calculateProfileCompletion(user);
        LocalDateTime updatedAt = user.getUpdatedAt() != null ? user.getUpdatedAt() : user.getCreatedAt();
        if (updatedAt == null) {
            updatedAt = now;
        }
        long ageDays = ChronoUnit.DAYS.between(updatedAt, now);

        if (completion < 50) {
            return new HealthMetricDto(
                    ageDays,
                    "CRITICAL",
                    "Profile is only " + completion + "% complete. This is below the critical threshold of 50%.",
                    updatedAt
            );
        }

        String status = "GOOD";
        String reason = "Profile details are complete and up to date.";

        if (ageDays > 2 * profileDaysLimit) {
            status = "CRITICAL";
            reason = "Profile was last updated " + ageDays + " days ago, which is extremely stale (critical limit is " + (2 * profileDaysLimit) + " days).";
        } else if (ageDays > profileDaysLimit) {
            status = "STALE";
            reason = "Profile was last updated " + ageDays + " days ago, exceeding recommended limit (" + profileDaysLimit + " days).";
        } else if (completion < 100) {
            status = "STALE";
            reason = "Profile is only " + completion + "% complete. Please fill in all details.";
        }

        return new HealthMetricDto(ageDays, status, reason, updatedAt);
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
}
