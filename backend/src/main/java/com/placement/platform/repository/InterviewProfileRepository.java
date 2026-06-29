package com.placement.platform.repository;

import com.placement.platform.entity.InterviewProfile;
import com.placement.platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InterviewProfileRepository extends JpaRepository<InterviewProfile, Long> {
    Optional<InterviewProfile> findByUser(User user);
}
