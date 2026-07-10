package com.placement.platform.job.recommendation;

import com.placement.platform.entity.User;
import com.placement.platform.job.dto.RecommendationAnalyticsResponseDto;
import com.placement.platform.job.entity.Job;
import com.placement.platform.job.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationAnalyticsServiceImpl implements RecommendationAnalyticsService {

    private final JobRecommendationRepository jobRecommendationRepository;
    private final JobRepository jobRepository;

    public RecommendationAnalyticsServiceImpl(
            JobRecommendationRepository jobRecommendationRepository,
            JobRepository jobRepository
    ) {
        this.jobRecommendationRepository = jobRecommendationRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public RecommendationAnalyticsResponseDto getAnalytics(User user, boolean includeHistory) {
        List<JobRecommendation> recs;

        if (includeHistory) {
            recs = jobRecommendationRepository.findByUserId(user.getId());
        } else {
            Optional<String> latestGenOpt = jobRecommendationRepository.findLatestGenerationId(user.getId());
            if (latestGenOpt.isEmpty()) {
                return createEmptyAnalytics();
            }
            recs = jobRecommendationRepository.findByUserIdAndGenerationId(user.getId(), latestGenOpt.get());
        }

        if (recs.isEmpty()) {
            return createEmptyAnalytics();
        }

        RecommendationAnalyticsResponseDto dto = new RecommendationAnalyticsResponseDto();

        long total = recs.size();
        dto.setTotalRecommendations(total);

        long viewed = recs.stream().filter(r -> r.getViewCount() > 0 || r.getRecommendationStatus() != RecommendationStatus.NEW).count();
        dto.setViewedCount(viewed);

        long saved = recs.stream().filter(r -> r.getRecommendationStatus() == RecommendationStatus.SAVED).count();
        dto.setSavedCount(saved);

        long applied = recs.stream().filter(r -> r.getApplicationStatus() != null).count();
        dto.setAppliedCount(applied);

        long hidden = recs.stream().filter(r -> r.getHidden() || r.getRecommendationStatus() == RecommendationStatus.HIDDEN).count();
        dto.setHiddenCount(hidden);

        long archived = recs.stream().filter(r -> r.getRecommendationStatus() == RecommendationStatus.ARCHIVED).count();
        dto.setArchivedCount(archived);

        long interviews = recs.stream().filter(r -> {
            ApplicationStatus s = r.getApplicationStatus();
            return s == ApplicationStatus.HR_SCREENING || s == ApplicationStatus.TECHNICAL || s == ApplicationStatus.MANAGER;
        }).count();
        dto.setInterviewCount(interviews);

        long offers = recs.stream().filter(r -> r.getApplicationStatus() == ApplicationStatus.OFFER).count();
        dto.setOfferCount(offers);

        long joined = recs.stream().filter(r -> r.getApplicationStatus() == ApplicationStatus.JOINED).count();
        dto.setJoinedCount(joined);

        long rejected = recs.stream().filter(r -> r.getApplicationStatus() == ApplicationStatus.REJECTED).count();
        dto.setRejectedCount(rejected);

        double avgScore = recs.stream().mapToInt(JobRecommendation::getMatchScore).average().orElse(0.0);
        dto.setAverageMatchScore(avgScore);

        double avgConf = recs.stream().mapToInt(JobRecommendation::getConfidenceScore).average().orElse(0.0);
        dto.setAverageConfidence(avgConf);

        // Fetch jobs to group by company
        List<Long> jobIds = recs.stream().map(JobRecommendation::getJobId).distinct().collect(Collectors.toList());
        List<Job> jobs = jobRepository.findAllById(jobIds);
        Map<Long, String> jobCompanyMap = jobs.stream().collect(Collectors.toMap(Job::getId, Job::getCompany, (a, b) -> a));

        Map<String, Long> companyCounts = recs.stream()
                .map(r -> jobCompanyMap.get(r.getJobId()))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        List<String> topCompanies = companyCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        dto.setTopCompanies(topCompanies);

        // Group missing skills
        Map<String, Long> skillCounts = recs.stream()
                .flatMap(r -> r.getMissingSkills().stream())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        List<String> mostMissingSkills = skillCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        dto.setMostMissingSkills(mostMissingSkills);

        // Conversion Rates
        double appRate = total > 0 ? (double) applied / total : 0.0;
        dto.setApplicationRate(appRate);

        double saveRate = total > 0 ? (double) saved / total : 0.0;
        dto.setSaveRate(saveRate);

        // Interview rate (interviews/applied) - note: anyone who reached interview stages, offer, or joined
        long interviewStageOrBeyond = recs.stream().filter(r -> {
            ApplicationStatus s = r.getApplicationStatus();
            return s == ApplicationStatus.HR_SCREENING || s == ApplicationStatus.TECHNICAL 
                || s == ApplicationStatus.MANAGER || s == ApplicationStatus.OFFER 
                || s == ApplicationStatus.JOINED;
        }).count();
        double interviewConv = applied > 0 ? (double) interviewStageOrBeyond / applied : 0.0;
        dto.setInterviewConversionRate(interviewConv);

        // Offer rate (offers/applied) - note: anyone who got an offer or joined
        long offerOrBeyond = recs.stream().filter(r -> {
            ApplicationStatus s = r.getApplicationStatus();
            return s == ApplicationStatus.OFFER || s == ApplicationStatus.JOINED;
        }).count();
        double offerConv = applied > 0 ? (double) offerOrBeyond / applied : 0.0;
        dto.setOfferConversionRate(offerConv);

        return dto;
    }

    private RecommendationAnalyticsResponseDto createEmptyAnalytics() {
        RecommendationAnalyticsResponseDto dto = new RecommendationAnalyticsResponseDto();
        dto.setTotalRecommendations(0L);
        dto.setViewedCount(0L);
        dto.setSavedCount(0L);
        dto.setAppliedCount(0L);
        dto.setHiddenCount(0L);
        dto.setArchivedCount(0L);
        dto.setInterviewCount(0L);
        dto.setOfferCount(0L);
        dto.setJoinedCount(0L);
        dto.setRejectedCount(0L);
        dto.setAverageMatchScore(0.0);
        dto.setAverageConfidence(0.0);
        dto.setTopCompanies(List.of());
        dto.setMostMissingSkills(List.of());
        dto.setApplicationRate(0.0);
        dto.setSaveRate(0.0);
        dto.setInterviewConversionRate(0.0);
        dto.setOfferConversionRate(0.0);
        return dto;
    }
}
