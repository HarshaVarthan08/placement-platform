package com.placement.platform.premium.entity;

import com.placement.platform.entity.User;
import com.placement.platform.premium.enums.PremiumFeature;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "premium_waitlist", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "feature"})
})
public class PremiumWaitlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PremiumFeature feature;

    @Column(length = 100)
    private String source;

    @CreationTimestamp
    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt;

    public PremiumWaitlist() {
    }

    public PremiumWaitlist(User user, PremiumFeature feature, String source) {
        this.user = user;
        this.feature = feature;
        this.source = source;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PremiumFeature getFeature() {
        return feature;
    }

    public void setFeature(PremiumFeature feature) {
        this.feature = feature;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }
}
