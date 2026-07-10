package com.placement.platform.job.recommendation;

import com.placement.platform.entity.User;
import com.placement.platform.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ApplicationTrackingServiceImpl implements ApplicationTrackingService {

    private final JobRecommendationRepository repository;

    public ApplicationTrackingServiceImpl(JobRecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public JobRecommendation applyForRecommendation(Long id, String url, String reference, String notes) {
        JobRecommendation rec = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job recommendation not found with ID: " + id));

        RecommendationStatus currentStatus = rec.getRecommendationStatus();
        validateRecommendationTransition(currentStatus, RecommendationStatus.APPLIED);

        rec.setRecommendationStatus(RecommendationStatus.APPLIED);
        rec.setAppliedAt(LocalDateTime.now());
        if (rec.getViewedAt() == null) {
            rec.setViewedAt(LocalDateTime.now());
        }

        rec.setApplicationStatus(ApplicationStatus.APPLIED);
        rec.setApplicationUrl(url);
        rec.setApplicationReference(reference);
        rec.setApplicationNotes(notes);

        return repository.save(rec);
    }

    @Override
    @Transactional
    public JobRecommendation updateApplicationStage(Long id, ApplicationStatus status, String notes) {
        JobRecommendation rec = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job recommendation not found with ID: " + id));

        ApplicationStatus currentAppStatus = rec.getApplicationStatus();
        if (currentAppStatus == null) {
            throw new IllegalStateException("Cannot update stage: No active application exists for this recommendation.");
        }

        validateApplicationStageTransition(currentAppStatus, status);

        rec.setApplicationStatus(status);
        if (notes != null && !notes.isBlank()) {
            rec.setApplicationNotes(notes);
        }

        return repository.save(rec);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobRecommendation> getAppliedRecommendations(User user, Pageable pageable) {
        return repository.findByUserIdAndApplicationStatusNotNullAndHiddenFalse(user.getId(), pageable);
    }

    private void validateRecommendationTransition(RecommendationStatus from, RecommendationStatus to) {
        if (from == to) {
            return;
        }
        boolean valid = false;
        switch (from) {
            case NEW:
            case VIEWED:
            case SAVED:
                valid = (to == RecommendationStatus.APPLIED);
                break;
            default:
                valid = false;
                break;
        }
        if (!valid) {
            throw new IllegalStateException("Invalid lifecycle state transition from " + from + " to " + to);
        }
    }

    private void validateApplicationStageTransition(ApplicationStatus from, ApplicationStatus to) {
        if (from == to) {
            return;
        }
        if (from == ApplicationStatus.JOINED || from == ApplicationStatus.REJECTED) {
            throw new IllegalStateException("Cannot transition application stage from terminal state: " + from);
        }
    }
}
