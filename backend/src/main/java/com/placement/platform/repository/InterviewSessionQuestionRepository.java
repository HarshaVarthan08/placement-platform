package com.placement.platform.repository;

import com.placement.platform.entity.InterviewSession;
import com.placement.platform.entity.InterviewSessionQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.placement.platform.entity.InterviewMode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface InterviewSessionQuestionRepository extends JpaRepository<InterviewSessionQuestion, Long> {
    List<InterviewSessionQuestion> findByInterviewSessionOrderByDisplayOrderAsc(InterviewSession session);

    @Query("SELECT COUNT(isq) FROM InterviewSessionQuestion isq JOIN isq.interviewSession s " +
           "WHERE s.user.id = :userId AND s.mode = :mode AND isq.status = com.placement.platform.entity.SessionQuestionStatus.ANSWERED")
    long countAttemptedQuestionsByUserAndMode(@Param("userId") Long userId, @Param("mode") InterviewMode mode);

    @Query("SELECT isq FROM InterviewSessionQuestion isq JOIN isq.interviewSession s " +
           "WHERE s.user.id = :userId AND isq.status = com.placement.platform.entity.SessionQuestionStatus.ANSWERED")
    List<InterviewSessionQuestion> findAllAnsweredQuestionsByUserId(@Param("userId") Long userId);
}
