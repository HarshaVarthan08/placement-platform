package com.placement.platform.repository;

import com.placement.platform.entity.InterviewSession;
import com.placement.platform.entity.InterviewSessionQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InterviewSessionQuestionRepository extends JpaRepository<InterviewSessionQuestion, Long> {
    List<InterviewSessionQuestion> findByInterviewSessionOrderByDisplayOrderAsc(InterviewSession session);
}
