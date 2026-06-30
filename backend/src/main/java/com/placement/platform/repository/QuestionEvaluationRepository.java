package com.placement.platform.repository;

import com.placement.platform.entity.QuestionEvaluation;
import com.placement.platform.entity.InterviewSessionQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

@Repository
public interface QuestionEvaluationRepository extends JpaRepository<QuestionEvaluation, Long> {
    Optional<QuestionEvaluation> findByInterviewSessionQuestion(InterviewSessionQuestion question);
    List<QuestionEvaluation> findByInterviewSessionQuestionIn(List<InterviewSessionQuestion> questions);

    @Query("SELECT qe FROM QuestionEvaluation qe JOIN qe.interviewSessionQuestion isq JOIN isq.interviewSession s WHERE s.user.id = :userId")
    List<QuestionEvaluation> findAllByUserId(@Param("userId") Long userId);
}
