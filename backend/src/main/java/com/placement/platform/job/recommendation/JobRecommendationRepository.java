package com.placement.platform.job.recommendation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRecommendationRepository extends JpaRepository<JobRecommendation, Long> {

    Page<JobRecommendation> findByUserId(Long userId, Pageable pageable);

    List<JobRecommendation> findByUserId(Long userId);

    Optional<JobRecommendation> findByUserIdAndJobId(Long userId, Long jobId);

    @Modifying
    @Query("DELETE FROM JobRecommendation r WHERE r.userId = :userId AND r.jobId NOT IN :jobIds")
    void deleteByUserIdAndJobIdNotIn(Long userId, List<Long> jobIds);

    @Modifying
    @Query("DELETE FROM JobRecommendation r WHERE r.userId = :userId")
    void deleteByUserId(Long userId);
}
