package com.placement.platform.career.intelligence;

import com.placement.platform.entity.*;
import com.placement.platform.repository.*;
import com.placement.platform.job.recommendation.JobRecommendation;
import com.placement.platform.job.entity.Job;
import com.placement.platform.career.config.CareerIntelligenceProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CareerIntelligenceProfileBuilder {

    private final ResumeRepository resumeRepository;
    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final InterviewEvaluationRepository interviewEvaluationRepository;
    private final UserSkillRepository userSkillRepository;
    private final UserTargetCompanyRepository userTargetCompanyRepository;
    private final QuestionEvaluationRepository questionEvaluationRepository;
    private final InterviewSessionQuestionRepository interviewSessionQuestionRepository;

    public CareerIntelligenceProfileBuilder(
            ResumeRepository resumeRepository,
            ResumeAnalysisRepository resumeAnalysisRepository,
            InterviewEvaluationRepository interviewEvaluationRepository,
            UserSkillRepository userSkillRepository,
            UserTargetCompanyRepository userTargetCompanyRepository,
            QuestionEvaluationRepository questionEvaluationRepository,
            InterviewSessionQuestionRepository interviewSessionQuestionRepository
    ) {
        this.resumeRepository = resumeRepository;
        this.resumeAnalysisRepository = resumeAnalysisRepository;
        this.interviewEvaluationRepository = interviewEvaluationRepository;
        this.userSkillRepository = userSkillRepository;
        this.userTargetCompanyRepository = userTargetCompanyRepository;
        this.questionEvaluationRepository = questionEvaluationRepository;
        this.interviewSessionQuestionRepository = interviewSessionQuestionRepository;
    }

    public CareerIntelligenceProfile buildProfile(
            User user,
            JobRecommendation recommendation,
            Job job,
            CareerIntelligenceProperties properties
    ) {
        Long userId = user.getId();

        // 1. Resolve Scores
        int placementReadiness = recommendation.getReadinessScore() != null ? recommendation.getReadinessScore() : 0;
        int matchScore = recommendation.getMatchScore() != null ? recommendation.getMatchScore() : 0;

        // Fetch Live Resume analysis
        Optional<Resume> resumeOpt = resumeRepository.findByUserId(userId);
        Optional<ResumeAnalysis> resumeAnalysisOpt = resumeOpt.flatMap(resumeAnalysisRepository::findByResume);
        int resumeScore = resumeAnalysisOpt.map(ResumeAnalysis::getAtsScore).orElse(
                recommendation.getAtsScore() != null ? recommendation.getAtsScore() : 0
        );

        // Fetch Live Interview evaluation
        List<InterviewEvaluation> latestEvaluations = interviewEvaluationRepository.findLatestEvaluationsByUserId(userId, PageRequest.of(0, 1));
        int interviewScore = latestEvaluations.isEmpty() ? 0 : latestEvaluations.get(0).getOverallScore();

        // Fetch skill score (from recommendation breakdown or fallback calculator)
        int skillScore = getSkillScore(recommendation, user, resumeAnalysisOpt);

        // 2. Career Confidence Score & Band
        int careerConfidenceScore = calculateConfidenceScore(matchScore, placementReadiness, resumeScore, interviewScore, properties);
        CareerConfidenceBand confidenceBand = calculateConfidenceBand(careerConfidenceScore);

        // 3. Derived Insights & Skill gaps
        List<CareerInsight> insights = new ArrayList<>();

        // Priority 1: Missing Required Skills (from Recommendation)
        List<String> missingSkills = recommendation.getMissingSkills() != null ? recommendation.getMissingSkills() : List.of();
        for (String skill : missingSkills) {
            if (skill != null && !skill.trim().isEmpty()) {
                insights.add(new CareerInsight(
                        skill.trim(),
                        "Required by the target role but missing from your profile.",
                        "VERY_HIGH",
                        "Job Matching"
                ));
            }
        }

        // Priority 2: Frequently Missed Interview Topics
        List<QuestionEvaluation> questionEvaluations = questionEvaluationRepository.findAllByUserId(userId);
        List<String> frequentlyMissedTopics = getFrequentlyMissedTopics(questionEvaluations);
        for (String topic : frequentlyMissedTopics) {
            if (topic != null && !topic.trim().isEmpty()) {
                insights.add(new CareerInsight(
                        topic.trim(),
                        "Identified as a recurring weakness in your mock interviews.",
                        "HIGH",
                        "Interview Analytics"
                ));
            }
        }

        // Priority 3: Resume Weakness (Missing skills or weaknesses from ResumeAnalysis)
        List<String> resumeMissingSkills = resumeAnalysisOpt.map(ResumeAnalysis::getMissingSkills).orElse(List.of());
        for (String rSkill : resumeMissingSkills) {
            if (rSkill != null && !rSkill.trim().isEmpty()) {
                insights.add(new CareerInsight(
                        rSkill.trim(),
                        "Flagged as a key missing skill in your latest resume analysis.",
                        "MEDIUM",
                        "Resume Analysis"
                ));
            }
        }
        List<String> resumeWeaknesses = resumeAnalysisOpt.map(ResumeAnalysis::getWeaknesses).orElse(List.of());
        for (String rWeakness : resumeWeaknesses) {
            if (rWeakness != null && !rWeakness.trim().isEmpty()) {
                insights.add(new CareerInsight(
                        rWeakness.trim(),
                        "Flagged as a key missing skill in your latest resume analysis.",
                        "MEDIUM",
                        "Resume Analysis"
                ));
            }
        }

        // Priority 4: Placement Skill Gap (Weakest skills from Skill performance)
        List<UserSkill> userSkills = userSkillRepository.findByUserId(userId);
        List<String> weakestSkills = getWeakestSkills(userSkills, resumeAnalysisOpt, questionEvaluations);
        for (String wSkill : weakestSkills) {
            if (wSkill != null && !wSkill.trim().isEmpty()) {
                insights.add(new CareerInsight(
                        wSkill.trim(),
                        "A skill gap identified in your overall placement readiness profile.",
                        "LOW",
                        "Placement Skill Gap"
                ));
            }
        }

        // Expose highest priority insight & skill
        CareerInsight highestPriorityInsight = insights.isEmpty() ? null : insights.get(0);
        String highestPrioritySkill = highestPriorityInsight != null ? highestPriorityInsight.title() : null;
        String highestPriorityReason = highestPriorityInsight != null ? highestPriorityInsight.description() : null;

        // 4. Preparation Difficulty & Weeks
        PreparationDifficulty preparationDifficulty = calculateDifficulty(missingSkills.size(), properties);
        int estimatedPreparationWeeks = calculatePreparationWeeks(preparationDifficulty, properties);

        // 5. Profile Health
        ProfileHealth profileHealth = calculateProfileHealth(resumeOpt.isPresent(), !latestEvaluations.isEmpty());

        // Return Profile
        return new CareerIntelligenceProfile(
                userId,
                recommendation.getId(),
                job.getId(),
                job.getCompany(),
                job.getTitle(),
                placementReadiness,
                matchScore,
                resumeScore,
                interviewScore,
                skillScore,
                careerConfidenceScore,
                confidenceBand,
                recommendation.getMatchedSkills() != null ? recommendation.getMatchedSkills() : List.of(),
                missingSkills,
                highestPrioritySkill,
                highestPriorityReason,
                highestPriorityInsight,
                insights,
                preparationDifficulty,
                estimatedPreparationWeeks,
                profileHealth,
                
                // Future Company Metadata placeholders
                null,
                null,
                null,
                
                // Metadata
                1,
                "1.0.0",
                "CareerIntelligenceEngine",
                LocalDateTime.now()
        );
    }

    private int getSkillScore(JobRecommendation recommendation, User user, Optional<ResumeAnalysis> resumeAnalysisOpt) {
        if (recommendation.getScoreBreakdown() != null && recommendation.getScoreBreakdown().skillScore() != null) {
            return recommendation.getScoreBreakdown().skillScore();
        }

        // Fallback calculation using repositories:
        List<UserSkill> userSkills = userSkillRepository.findByUserId(user.getId());
        List<String> userSkillNames = userSkills.stream()
                .map(us -> us.getSkill().getName().trim().toLowerCase())
                .collect(Collectors.toList());

        // Target company unique required skills
        Set<String> requiredSkillNames = new HashSet<>();
        List<UserTargetCompany> targetCompanies = userTargetCompanyRepository.findByUserId(user.getId());
        for (UserTargetCompany utc : targetCompanies) {
            TargetCompany tc = utc.getCompany();
            if (tc != null && tc.getRequiredSkills() != null) {
                for (Skill skill : tc.getRequiredSkills()) {
                    if (skill.getName() != null) {
                        requiredSkillNames.add(skill.getName().trim().toLowerCase());
                    }
                }
            }
        }

        if (!requiredSkillNames.isEmpty()) {
            long matched = requiredSkillNames.stream()
                    .filter(userSkillNames::contains)
                    .count();
            return (int) Math.round((matched / (double) requiredSkillNames.size()) * 100.0);
        }

        // Fallback 2: Check resume missing skills count
        int userSkillsSize = userSkillNames.size();
        int missingSkillsSize = resumeAnalysisOpt
                .map(ra -> ra.getMissingSkills() != null ? ra.getMissingSkills().size() : 0)
                .orElse(0);

        if (userSkillsSize + missingSkillsSize > 0) {
            return (int) Math.round((userSkillsSize / (double) (userSkillsSize + missingSkillsSize)) * 100.0);
        }

        return userSkillsSize > 0 ? 100 : 0;
    }

    int calculateConfidenceScore(
            int matchScore,
            int placementReadiness,
            int resumeScore,
            int interviewScore,
            CareerIntelligenceProperties properties
    ) {
        CareerIntelligenceProperties.Confidence confidence = properties.getConfidence();
        double matchWeight = confidence.getMatchWeight();
        double placementWeight = confidence.getPlacementWeight();
        double resumeWeight = confidence.getResumeWeight();
        double interviewWeight = confidence.getInterviewWeight();

        double totalWeight = matchWeight + placementWeight + resumeWeight + interviewWeight;
        if (totalWeight == 0) {
            return 0;
        }

        double weightedSum = (matchScore * matchWeight)
                + (placementReadiness * placementWeight)
                + (resumeScore * resumeWeight)
                + (interviewScore * interviewWeight);

        return (int) Math.round(weightedSum / totalWeight);
    }

    CareerConfidenceBand calculateConfidenceBand(int score) {
        if (score >= 85) {
            return CareerConfidenceBand.VERY_HIGH;
        } else if (score >= 70) {
            return CareerConfidenceBand.HIGH;
        } else if (score >= 50) {
            return CareerConfidenceBand.MEDIUM;
        } else {
            return CareerConfidenceBand.LOW;
        }
    }

    private List<String> getFrequentlyMissedTopics(List<QuestionEvaluation> evaluations) {
        Map<String, Long> missedTopicCounts = evaluations.stream()
                .filter(qe -> qe.getScore() != null && qe.getScore() < 60)
                .map(QuestionEvaluation::getInterviewSessionQuestion)
                .filter(q -> q != null && q.getTopic() != null)
                .collect(Collectors.groupingBy(q -> q.getTopic().trim(), Collectors.counting()));

        return missedTopicCounts.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .map(Map.Entry::getKey)
                .limit(3)
                .collect(Collectors.toList());
    }

    private List<String> getWeakestSkills(
            List<UserSkill> userSkills,
            Optional<ResumeAnalysis> resumeAnalysisOpt,
            List<QuestionEvaluation> evaluations
    ) {
        Map<String, SkillPerformanceData> performanceMap = new HashMap<>();

        // Add user skills (default to 80)
        for (UserSkill us : userSkills) {
            if (us.getSkill() != null && us.getSkill().getName() != null) {
                String name = us.getSkill().getName().trim();
                performanceMap.put(name.toLowerCase(), new SkillPerformanceData(name, 80));
            }
        }

        // Add resume missing skills (default to 0)
        if (resumeAnalysisOpt.isPresent()) {
            List<String> missing = resumeAnalysisOpt.get().getMissingSkills();
            if (missing != null) {
                for (String m : missing) {
                    if (m != null) {
                        String name = m.trim();
                        performanceMap.put(name.toLowerCase(), new SkillPerformanceData(name, 0));
                    }
                }
            }
        }

        // Add question evaluation average scores
        Map<String, List<QuestionEvaluation>> evalsByTopic = new HashMap<>();
        for (QuestionEvaluation qe : evaluations) {
            InterviewSessionQuestion question = qe.getInterviewSessionQuestion();
            if (question != null && question.getTopic() != null) {
                String topicKey = question.getTopic().trim().toLowerCase();
                evalsByTopic.computeIfAbsent(topicKey, k -> new ArrayList<>()).add(qe);
            }
        }

        for (Map.Entry<String, List<QuestionEvaluation>> entry : evalsByTopic.entrySet()) {
            String topicKey = entry.getKey();
            List<QuestionEvaluation> evals = entry.getValue();
            double avgScore = evals.stream()
                    .mapToInt(qe -> qe.getScore() != null ? qe.getScore() : 0)
                    .average()
                    .orElse(0.0);
            String topicName = evals.get(0).getInterviewSessionQuestion().getTopic();
            performanceMap.put(topicKey, new SkillPerformanceData(topicName, (int) Math.round(avgScore)));
        }

        return performanceMap.values().stream()
                .filter(p -> p.score < 60)
                .sorted(Comparator.comparingInt(p -> p.score))
                .map(p -> p.name)
                .collect(Collectors.toList());
    }

    PreparationDifficulty calculateDifficulty(int missingSkillsCount, CareerIntelligenceProperties properties) {
        CareerIntelligenceProperties.Difficulty difficulty = properties.getDifficulty();
        if (missingSkillsCount > difficulty.getVeryHighThreshold()) {
            return PreparationDifficulty.VERY_HIGH;
        } else if (missingSkillsCount >= difficulty.getHighThreshold()) {
            return PreparationDifficulty.HIGH;
        } else if (missingSkillsCount >= difficulty.getMediumThreshold()) {
            return PreparationDifficulty.MEDIUM;
        } else {
            return PreparationDifficulty.LOW;
        }
    }

    int calculatePreparationWeeks(PreparationDifficulty difficulty, CareerIntelligenceProperties properties) {
        CareerIntelligenceProperties.Preparation prep = properties.getPreparation();
        switch (difficulty) {
            case LOW:
                return prep.getWeeks().getLow();
            case MEDIUM:
                return prep.getWeeks().getMedium();
            case HIGH:
                return prep.getWeeks().getHigh();
            case VERY_HIGH:
                return prep.getWeeks().getVeryHigh();
            default:
                return 0;
        }
    }

    ProfileHealth calculateProfileHealth(boolean hasResume, boolean hasInterview) {
        if (hasResume && hasInterview) {
            return ProfileHealth.COMPLETE;
        } else if (hasResume || hasInterview) {
            return ProfileHealth.PARTIAL;
        } else {
            return ProfileHealth.INCOMPLETE;
        }
    }

    private static class SkillPerformanceData {
        String name;
        int score;

        SkillPerformanceData(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }
}
