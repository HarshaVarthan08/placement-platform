package com.placement.platform.job.repository;

import com.placement.platform.job.entity.JobSyncHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobSyncHistoryRepository extends JpaRepository<JobSyncHistory, Long> {
}
