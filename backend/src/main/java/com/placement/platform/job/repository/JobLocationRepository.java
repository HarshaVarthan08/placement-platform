package com.placement.platform.job.repository;

import com.placement.platform.job.entity.JobLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobLocationRepository extends JpaRepository<JobLocation, Long> {
}
