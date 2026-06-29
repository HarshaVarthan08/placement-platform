package com.placement.platform.repository;

import com.placement.platform.entity.InterviewQuestion;
import com.placement.platform.entity.InterviewQuestionPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {
    List<InterviewQuestion> findByQuestionPool(InterviewQuestionPool questionPool);
}
