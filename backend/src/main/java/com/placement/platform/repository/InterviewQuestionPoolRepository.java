package com.placement.platform.repository;

import com.placement.platform.entity.InterviewQuestionPool;
import com.placement.platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.placement.platform.entity.QuestionPoolStatus;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewQuestionPoolRepository extends JpaRepository<InterviewQuestionPool, Long> {
    List<InterviewQuestionPool> findByUser(User user);
    Optional<InterviewQuestionPool> findByUserAndStatus(User user, QuestionPoolStatus status);
}
