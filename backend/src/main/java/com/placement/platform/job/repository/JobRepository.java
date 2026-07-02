package com.placement.platform.job.repository;

import com.placement.platform.job.entity.Job;
import com.placement.platform.job.entity.JobSourceType;
import com.placement.platform.job.entity.JobStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Optional<Job> findBySourceAndExternalId(JobSourceType source, String externalId);

    @EntityGraph(attributePaths = {"skills", "locations"})
    Optional<Job> findByFingerprint(String fingerprint);

    @EntityGraph(attributePaths = {"skills", "locations"})
    List<Job> findByStatus(JobStatus status);

    List<Job> findBySource(JobSourceType source);

    @EntityGraph(attributePaths = {"skills", "locations"})
    @Query("SELECT j FROM Job j WHERE j.status = com.placement.platform.job.entity.JobStatus.ACTIVE")
    List<Job> findActiveJobs();
}
