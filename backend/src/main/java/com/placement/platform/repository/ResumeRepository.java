package com.placement.platform.repository;

import com.placement.platform.entity.Resume;
import com.placement.platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Optional<Resume> findByUser(User user);
    Optional<Resume> findByUserId(Long userId);
    boolean existsByUser(User user);
    boolean existsByUserId(Long userId);
}
