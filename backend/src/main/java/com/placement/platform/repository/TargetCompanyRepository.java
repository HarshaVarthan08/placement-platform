package com.placement.platform.repository;

import com.placement.platform.entity.TargetCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TargetCompanyRepository extends JpaRepository<TargetCompany, Long> {
    Optional<TargetCompany> findByNameIgnoreCase(String name);
}
