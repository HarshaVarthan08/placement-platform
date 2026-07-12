package com.placement.platform.career.service;

import com.placement.platform.career.intelligence.CareerIntelligenceEngine;
import com.placement.platform.career.intelligence.CareerIntelligenceProfile;
import com.placement.platform.entity.User;
import com.placement.platform.entity.Role;
import com.placement.platform.exception.ResourceNotFoundException;
import com.placement.platform.job.entity.Job;
import com.placement.platform.job.recommendation.JobRecommendation;
import com.placement.platform.job.recommendation.JobRecommendationRepository;
import com.placement.platform.job.repository.JobRepository;
import com.placement.platform.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CareerIntelligenceServiceImpl implements CareerIntelligenceService {

    private static final Logger log = LoggerFactory.getLogger(CareerIntelligenceServiceImpl.class);

    private final CareerIntelligenceEngine careerIntelligenceEngine;
    private final JobRecommendationRepository jobRecommendationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public CareerIntelligenceServiceImpl(
            CareerIntelligenceEngine careerIntelligenceEngine,
            JobRecommendationRepository jobRecommendationRepository,
            UserRepository userRepository,
            JobRepository jobRepository
    ) {
        this.careerIntelligenceEngine = careerIntelligenceEngine;
        this.jobRecommendationRepository = jobRecommendationRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public CareerIntelligenceProfile getProfile(Long recommendationId, User authenticatedUser) {
        JobRecommendation recommendation = jobRecommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new ResourceNotFoundException("Recommendation not found."));

        // Security validation
        if (authenticatedUser.getRole() != Role.ADMIN && !recommendation.getUserId().equals(authenticatedUser.getId())) {
            throw new AccessDeniedException("User does not own the recommendation.");
        }

        User candidate = userRepository.findById(recommendation.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for recommendation."));

        Job job = jobRepository.findById(recommendation.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found for recommendation."));

        CareerIntelligenceProfile profile = careerIntelligenceEngine.buildProfile(candidate, recommendation, job);

        log.info("Career Intelligence generated\nUser:{}\nRecommendation:{}\nConfidence:{}\nDifficulty:{}",
                profile.userId(),
                profile.recommendationId(),
                profile.careerConfidenceScore(),
                profile.preparationDifficulty()
        );

        return profile;
    }
}
