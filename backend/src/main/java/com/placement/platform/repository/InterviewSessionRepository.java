package com.placement.platform.repository;

import com.placement.platform.entity.InterviewMode;
import com.placement.platform.entity.InterviewSession;
import com.placement.platform.entity.User;
import com.placement.platform.entity.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InterviewSessionRepository extends JpaRepository<InterviewSession, Long> {
    Optional<InterviewSession> findFirstByUserAndModeOrderByStartedAtDesc(User user, InterviewMode mode);
    boolean existsByUserAndStatus(User user, InterviewStatus status);
}

