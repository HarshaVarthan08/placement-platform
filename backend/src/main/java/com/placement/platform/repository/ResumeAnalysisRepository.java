package com.placement.platform.repository;

import com.placement.platform.entity.Resume;
import com.placement.platform.entity.ResumeAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeAnalysisRepository extends JpaRepository<ResumeAnalysis, Long> {
    Optional<ResumeAnalysis> findByResume(Resume resume);
    boolean existsByResume(Resume resume);
    void deleteByResume(Resume resume);
}
