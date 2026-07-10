package com.placement.platform.job.recommendation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRecommendationRepository extends JpaRepository<JobRecommendation, Long> {

    Page<JobRecommendation> findByUserId(Long userId, Pageable pageable);

    List<JobRecommendation> findByUserId(Long userId);

    Optional<JobRecommendation> findByUserIdAndJobId(Long userId, Long jobId);

    List<JobRecommendation> findByUserIdAndJobIdOrderByRecommendationVersionDesc(Long userId, Long jobId);

    Page<JobRecommendation> findByUserIdAndGenerationIdAndHiddenFalse(Long userId, String generationId, Pageable pageable);

    Page<JobRecommendation> findByUserIdAndGenerationId(Long userId, String generationId, Pageable pageable);

    List<JobRecommendation> findByUserIdAndGenerationId(Long userId, String generationId);

    Page<JobRecommendation> findByUserIdAndRecommendationStatusAndHiddenFalse(Long userId, RecommendationStatus status, Pageable pageable);

    List<JobRecommendation> findByUserIdAndRecommendationStatusAndHiddenFalse(Long userId, RecommendationStatus status);

    Page<JobRecommendation> findByUserIdAndApplicationStatusNotNullAndHiddenFalse(Long userId, Pageable pageable);

    Page<JobRecommendation> findByUserIdAndHidden(Long userId, Boolean hidden, Pageable pageable);

    @Query("SELECT r.generationId FROM JobRecommendation r WHERE r.userId = :userId ORDER BY r.generatedAt DESC")
    List<String> findGenerationIdsByUserIdOrderByGeneratedAtDesc(@Param("userId") Long userId, Pageable pageable);

    default Optional<String> findLatestGenerationId(Long userId) {
        List<String> ids = findGenerationIdsByUserIdOrderByGeneratedAtDesc(userId, org.springframework.data.domain.PageRequest.of(0, 1));
        return ids.isEmpty() || ids.get(0) == null ? Optional.empty() : Optional.of(ids.get(0));
    }

    @Modifying
    @Query("DELETE FROM JobRecommendation r WHERE r.userId = :userId AND r.jobId NOT IN :jobIds")
    void deleteByUserIdAndJobIdNotIn(Long userId, List<Long> jobIds);

    @Modifying
    @Query("DELETE FROM JobRecommendation r WHERE r.userId = :userId")
    void deleteByUserId(Long userId);
}
