package com.placement.platform.service;

import com.placement.platform.entity.InterviewProfile;
import com.placement.platform.entity.InterviewQuestionPool;
import com.placement.platform.entity.QuestionPoolStatus;
import com.placement.platform.entity.User;
import com.placement.platform.repository.InterviewProfileRepository;
import com.placement.platform.repository.InterviewQuestionPoolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InterviewProfileServiceImpl implements InterviewProfileService {

    private final InterviewProfileRepository interviewProfileRepository;
    private final InterviewQuestionPoolRepository interviewQuestionPoolRepository;

    public InterviewProfileServiceImpl(
            InterviewProfileRepository interviewProfileRepository,
            InterviewQuestionPoolRepository interviewQuestionPoolRepository
    ) {
        this.interviewProfileRepository = interviewProfileRepository;
        this.interviewQuestionPoolRepository = interviewQuestionPoolRepository;
    }

    @Override
    @Transactional
    public InterviewProfile getOrCreateProfile(User user) {
        return interviewProfileRepository.findByUser(user)
                .orElseGet(() -> {
                    InterviewProfile profile = new InterviewProfile();
                    profile.setUser(user);
                    profile.setProfileVersion(1);
                    return interviewProfileRepository.save(profile);
                });
    }

    @Override
    @Transactional
    public void incrementProfileVersion(User user) {
        InterviewProfile profile = getOrCreateProfile(user);
        profile.setProfileVersion(profile.getProfileVersion() + 1);
        interviewProfileRepository.save(profile);

        // Mark current ACTIVE pool as OUTDATED
        Optional<InterviewQuestionPool> activePoolOpt = interviewQuestionPoolRepository
                .findByUserAndStatus(user, QuestionPoolStatus.ACTIVE);
        if (activePoolOpt.isPresent()) {
            InterviewQuestionPool activePool = activePoolOpt.get();
            activePool.setStatus(QuestionPoolStatus.OUTDATED);
            interviewQuestionPoolRepository.save(activePool);
        }
    }
}
