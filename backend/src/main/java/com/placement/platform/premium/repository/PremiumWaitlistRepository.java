package com.placement.platform.premium.repository;

import com.placement.platform.premium.entity.PremiumWaitlist;
import com.placement.platform.premium.enums.PremiumFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PremiumWaitlistRepository extends JpaRepository<PremiumWaitlist, Long> {
    Optional<PremiumWaitlist> findByUserIdAndFeature(Long userId, PremiumFeature feature);
    boolean existsByUserIdAndFeature(Long userId, PremiumFeature feature);
}
