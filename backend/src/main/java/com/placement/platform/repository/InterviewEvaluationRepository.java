package com.placement.platform.repository;

import com.placement.platform.entity.InterviewEvaluation;
import com.placement.platform.entity.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewEvaluationRepository extends JpaRepository<InterviewEvaluation, Long> {
    Optional<InterviewEvaluation> findByInterviewSession(InterviewSession interviewSession);
    boolean existsByInterviewSession(InterviewSession interviewSession);

    @Query("SELECT ie FROM InterviewEvaluation ie JOIN ie.interviewSession s WHERE s.user.id = :userId ORDER BY ie.evaluatedAt DESC")
    List<InterviewEvaluation> findLatestEvaluationsByUserId(@Param("userId") Long userId, Pageable pageable);
}
