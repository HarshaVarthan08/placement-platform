package com.placement.platform.repository;

import com.placement.platform.entity.InterviewMode;
import com.placement.platform.entity.InterviewSession;
import com.placement.platform.entity.User;
import com.placement.platform.entity.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

@Repository
public interface InterviewSessionRepository extends JpaRepository<InterviewSession, Long> {
    Optional<InterviewSession> findFirstByUserAndModeOrderByStartedAtDesc(User user, InterviewMode mode);
    Optional<InterviewSession> findFirstByUserAndStatusOrderByCompletedAtDesc(User user, InterviewStatus status);
    boolean existsByUserAndStatus(User user, InterviewStatus status);
    Optional<InterviewSession> findByUserAndStatus(User user, InterviewStatus status);

    @Query("SELECT COUNT(s) FROM InterviewSession s WHERE s.user.id = :userId AND s.mode = :mode AND s.status = com.placement.platform.entity.InterviewStatus.COMPLETED")
    long countByUserAndModeAndStatusCompleted(@Param("userId") Long userId, @Param("mode") InterviewMode mode);
}

