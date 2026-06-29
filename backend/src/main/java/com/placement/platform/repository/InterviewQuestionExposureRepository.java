package com.placement.platform.repository;

import com.placement.platform.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewQuestionExposureRepository extends JpaRepository<InterviewQuestionExposure, Long> {
    Optional<InterviewQuestionExposure> findByUserAndInterviewQuestionAndMode(User user, InterviewQuestion interviewQuestion, InterviewMode mode);
    Optional<InterviewQuestionExposure> findByUserAndGlobalQuestionAndMode(User user, GlobalQuestion globalQuestion, InterviewMode mode);
    List<InterviewQuestionExposure> findByUserAndMode(User user, InterviewMode mode);
}
