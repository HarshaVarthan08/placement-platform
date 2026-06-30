package com.placement.platform.repository;

import com.placement.platform.entity.QuestionEvaluation;
import com.placement.platform.entity.InterviewSessionQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface QuestionEvaluationRepository extends JpaRepository<QuestionEvaluation, Long> {
    Optional<QuestionEvaluation> findByInterviewSessionQuestion(InterviewSessionQuestion question);
    List<QuestionEvaluation> findByInterviewSessionQuestionIn(List<InterviewSessionQuestion> questions);
}
