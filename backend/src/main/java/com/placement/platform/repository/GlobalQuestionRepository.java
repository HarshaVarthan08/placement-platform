package com.placement.platform.repository;

import com.placement.platform.entity.GlobalQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalQuestionRepository extends JpaRepository<GlobalQuestion, Long> {
}
