package com.placement.platform.job.recommendation;

import com.placement.platform.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RecommendationLifecycleServiceImpl implements RecommendationLifecycleService {

    private final JobRecommendationRepository repository;

    public RecommendationLifecycleServiceImpl(JobRecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public JobRecommendation markViewed(Long id) {
        JobRecommendation rec = getRecommendation(id);
        RecommendationStatus current = rec.getRecommendationStatus();

        if (current == RecommendationStatus.NEW) {
            validateTransition(current, RecommendationStatus.VIEWED);
            rec.setRecommendationStatus(RecommendationStatus.VIEWED);
            rec.setViewedAt(LocalDateTime.now());
        }

        rec.setViewCount(rec.getViewCount() + 1);
        return repository.save(rec);
    }

    @Override
    @Transactional
    public JobRecommendation saveRecommendation(Long id) {
        JobRecommendation rec = getRecommendation(id);
        RecommendationStatus current = rec.getRecommendationStatus();

        validateTransition(current, RecommendationStatus.SAVED);
        rec.setRecommendationStatus(RecommendationStatus.SAVED);
        rec.setSavedAt(LocalDateTime.now());

        return repository.save(rec);
    }

    @Override
    @Transactional
    public JobRecommendation unsaveRecommendation(Long id) {
        JobRecommendation rec = getRecommendation(id);
        RecommendationStatus current = rec.getRecommendationStatus();

        validateTransition(current, RecommendationStatus.VIEWED);
        rec.setRecommendationStatus(RecommendationStatus.VIEWED);
        rec.setSavedAt(null);

        return repository.save(rec);
    }

    @Override
    @Transactional
    public JobRecommendation hideRecommendation(Long id) {
        JobRecommendation rec = getRecommendation(id);
        RecommendationStatus current = rec.getRecommendationStatus();

        validateTransition(current, RecommendationStatus.HIDDEN);
        rec.setRecommendationStatus(RecommendationStatus.HIDDEN);
        rec.setHidden(true);
        rec.setHiddenAt(LocalDateTime.now());

        return repository.save(rec);
    }

    @Override
    @Transactional
    public JobRecommendation archiveRecommendation(Long id) {
        JobRecommendation rec = getRecommendation(id);
        RecommendationStatus current = rec.getRecommendationStatus();

        validateTransition(current, RecommendationStatus.ARCHIVED);
        rec.setRecommendationStatus(RecommendationStatus.ARCHIVED);
        rec.setArchivedAt(LocalDateTime.now());

        return repository.save(rec);
    }

    @Override
    @Transactional
    public JobRecommendation expireRecommendation(Long id) {
        JobRecommendation rec = getRecommendation(id);
        RecommendationStatus current = rec.getRecommendationStatus();

        validateTransition(current, RecommendationStatus.EXPIRED);
        rec.setRecommendationStatus(RecommendationStatus.EXPIRED);

        return repository.save(rec);
    }

    @Override
    @Transactional
    public JobRecommendation submitFeedback(Long id, RecommendationFeedbackType feedbackType, String feedbackNotes) {
        JobRecommendation rec = getRecommendation(id);
        rec.setFeedbackType(feedbackType);
        rec.setFeedbackNotes(feedbackNotes);
        return repository.save(rec);
    }

    private JobRecommendation getRecommendation(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job recommendation not found with ID: " + id));
    }

    private void validateTransition(RecommendationStatus from, RecommendationStatus to) {
        if (from == to) {
            return;
        }
        boolean valid = false;
        switch (from) {
            case NEW:
                valid = (to == RecommendationStatus.VIEWED || to == RecommendationStatus.SAVED 
                        || to == RecommendationStatus.HIDDEN || to == RecommendationStatus.ARCHIVED 
                        || to == RecommendationStatus.EXPIRED);
                break;
            case VIEWED:
                valid = (to == RecommendationStatus.SAVED || to == RecommendationStatus.APPLIED 
                        || to == RecommendationStatus.HIDDEN || to == RecommendationStatus.ARCHIVED 
                        || to == RecommendationStatus.EXPIRED);
                break;
            case SAVED:
                valid = (to == RecommendationStatus.VIEWED || to == RecommendationStatus.APPLIED 
                        || to == RecommendationStatus.HIDDEN || to == RecommendationStatus.ARCHIVED 
                        || to == RecommendationStatus.EXPIRED);
                break;
            case APPLIED:
                valid = (to == RecommendationStatus.ARCHIVED);
                break;
            case HIDDEN:
                valid = (to == RecommendationStatus.VIEWED || to == RecommendationStatus.ARCHIVED);
                break;
            case ARCHIVED:
                valid = (to == RecommendationStatus.VIEWED);
                break;
            case EXPIRED:
                valid = (to == RecommendationStatus.ARCHIVED);
                break;
        }
        if (!valid) {
            throw new IllegalStateException("Invalid state transition from " + from + " to " + to);
        }
    }
}
