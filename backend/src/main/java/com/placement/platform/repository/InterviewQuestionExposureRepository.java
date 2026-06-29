package com.placement.platform.repository;

import com.placement.platform.entity.InterviewQuestionExposure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewQuestionExposureRepository extends JpaRepository<InterviewQuestionExposure, Long> {
}
