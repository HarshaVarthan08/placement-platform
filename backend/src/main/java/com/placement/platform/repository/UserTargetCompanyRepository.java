package com.placement.platform.repository;

import com.placement.platform.entity.UserTargetCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTargetCompanyRepository extends JpaRepository<UserTargetCompany, Long> {
    List<UserTargetCompany> findByUserId(Long userId);
    Optional<UserTargetCompany> findByUserIdAndCompanyId(Long userId, Long companyId);
}
