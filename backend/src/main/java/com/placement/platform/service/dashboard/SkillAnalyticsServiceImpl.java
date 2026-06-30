package com.placement.platform.service.dashboard;

import com.placement.platform.dto.dashboard.SkillAnalyticsCard;
import com.placement.platform.dto.dashboard.SkillPerformanceDto;
import com.placement.platform.entity.*;
import com.placement.platform.service.dashboard.PlacementDataAggregator.PlacementData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SkillAnalyticsServiceImpl implements SkillAnalyticsService {

    @Override
    public SkillAnalyticsCard analyzeSkills(User user, PlacementData data) {
        LocalDateTime now = LocalDateTime.now();

        // 1. Gather all unique topics/skills from Profile, Resume Analysis, and Question Evaluations
        Map<String, SkillPerformanceData> performanceMap = new HashMap<>();

        // Add from user skills (Candidate Profile)
        if (data.userSkills() != null) {
            for (UserSkill us : data.userSkills()) {
                if (us.getSkill() != null && us.getSkill().getName() != null) {
                    String skillName = us.getSkill().getName().trim();
                    String key = skillName.toLowerCase();
                    performanceMap.put(key, new SkillPerformanceData(skillName, 80, 0, "Candidate Profile"));
                }
            }
        }

        // Add from resume missing skills
        if (data.resumeAnalysis().isPresent()) {
            List<String> missing = data.resumeAnalysis().get().getMissingSkills();
            if (missing != null) {
                for (String m : missing) {
                    String skillName = m.trim();
                    String key = skillName.toLowerCase();
                    // Missing skills default to score 0, source Resume Analysis
                    performanceMap.put(key, new SkillPerformanceData(skillName, 0, 0, "Resume Analysis"));
                }
            }
        }

        // Analyze Question Evaluations to compute scores and trends
        Map<String, List<QuestionEvaluation>> evalByTopic = new HashMap<>();
        for (QuestionEvaluation qe : data.questionEvaluations()) {
            InterviewSessionQuestion question = qe.getInterviewSessionQuestion();
            if (question != null && question.getTopic() != null) {
                String topicKey = question.getTopic().trim().toLowerCase();
                evalByTopic.computeIfAbsent(topicKey, k -> new ArrayList<>()).add(qe);
            }
        }

        for (Map.Entry<String, List<QuestionEvaluation>> entry : evalByTopic.entrySet()) {
            String topicKey = entry.getKey();
            List<QuestionEvaluation> evals = entry.getValue();

            // Calculate average score
            double avgScore = evals.stream()
                    .mapToInt(QuestionEvaluation::getScore)
                    .average()
                    .orElse(0.0);

            // Sort evaluations by date to calculate trend
            evals.sort(Comparator.comparing(QuestionEvaluation::getCreatedAt));
            int trend = 0;
            if (evals.size() > 1) {
                int latestScore = evals.get(evals.size() - 1).getScore();
                int prevScore = evals.get(evals.size() - 2).getScore();
                trend = latestScore - prevScore;
            }

            // Capitalize topic name
            String topicName = evals.get(0).getInterviewSessionQuestion().getTopic();

            // Resolve source
            String source = "Interview Evaluation";
            if (performanceMap.containsKey(topicKey)) {
                String existingSource = performanceMap.get(topicKey).source;
                if (existingSource.equals("Candidate Profile")) {
                    source = "Combined";
                }
            }

            performanceMap.put(topicKey, new SkillPerformanceData(topicName, (int) Math.round(avgScore), trend, source));
        }

        // Convert Map to DTO lists
        List<SkillPerformanceDto> allPerformances = performanceMap.values().stream()
                .map(p -> new SkillPerformanceDto(p.name, p.score, p.trend, p.source))
                .collect(Collectors.toList());

        // Segment into Top and Weakest
        List<SkillPerformanceDto> topSkills = allPerformances.stream()
                .filter(p -> p.score() >= 70)
                .sorted((p1, p2) -> Integer.compare(p2.score(), p1.score()))
                .collect(Collectors.toList());

        List<SkillPerformanceDto> weakestSkills = allPerformances.stream()
                .filter(p -> p.score() < 60)
                .sorted(Comparator.comparingInt(SkillPerformanceDto::score))
                .collect(Collectors.toList());

        // 2. Best and Lowest Interview Category Performance
        Map<QuestionCategory, List<Integer>> scoreByCategory = new HashMap<>();
        for (QuestionEvaluation qe : data.questionEvaluations()) {
            InterviewSessionQuestion question = qe.getInterviewSessionQuestion();
            if (question != null && question.getCategory() != null) {
                scoreByCategory.computeIfAbsent(question.getCategory(), k -> new ArrayList<>()).add(qe.getScore());
            }
        }

        String bestCategory = "N/A";
        double bestAvg = -1.0;
        String lowestCategory = "N/A";
        double lowestAvg = 101.0;

        for (Map.Entry<QuestionCategory, List<Integer>> entry : scoreByCategory.entrySet()) {
            double avg = entry.getValue().stream().mapToInt(Integer::intValue).average().orElse(0.0);
            if (avg > bestAvg) {
                bestAvg = avg;
                bestCategory = entry.getKey().name();
            }
            if (avg < lowestAvg) {
                lowestAvg = avg;
                lowestCategory = entry.getKey().name();
            }
        }

        // 3. Frequently Practiced Topics (based on answered questions exposure count)
        Map<String, Long> practiceTopicCounts = data.answeredQuestions().stream()
                .filter(q -> q.getTopic() != null)
                .collect(Collectors.groupingBy(q -> q.getTopic().trim(), Collectors.counting()));

        List<String> frequentlyPracticed = practiceTopicCounts.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .map(Map.Entry::getKey)
                .limit(5)
                .collect(Collectors.toList());

        // 4. Frequently Missed Topics (scores < 60)
        Map<String, Long> missedTopicCounts = data.questionEvaluations().stream()
                .filter(qe -> qe.getScore() < 60)
                .map(qe -> qe.getInterviewSessionQuestion())
                .filter(q -> q != null && q.getTopic() != null)
                .collect(Collectors.groupingBy(q -> q.getTopic().trim(), Collectors.counting()));

        List<String> frequentlyMissed = missedTopicCounts.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .map(Map.Entry::getKey)
                .limit(3)
                .collect(Collectors.toList());

        String status = allPerformances.isEmpty() ? "NOT_AVAILABLE" : "AVAILABLE";

        return new SkillAnalyticsCard(
                status,
                topSkills,
                weakestSkills,
                bestCategory,
                lowestCategory,
                frequentlyPracticed,
                frequentlyMissed,
                now
        );
    }

    private static class SkillPerformanceData {
        String name;
        int score;
        int trend;
        String source;

        SkillPerformanceData(String name, int score, int trend, String source) {
            this.name = name;
            this.score = score;
            this.trend = trend;
            this.source = source;
        }
    }
}
