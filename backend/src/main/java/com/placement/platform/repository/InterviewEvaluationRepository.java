package com.placement.platform.repository;

import com.placement.platform.entity.InterviewEvaluation;
import com.placement.platform.entity.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InterviewEvaluationRepository extends JpaRepository<InterviewEvaluation, Long> {
    Optional<InterviewEvaluation> findByInterviewSession(InterviewSession interviewSession);
    boolean existsByInterviewSession(InterviewSession interviewSession);
}
