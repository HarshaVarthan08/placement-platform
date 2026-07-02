package com.placement.platform.job.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "job_skills")
public class JobSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    @JsonIgnore
    private Job job;

    @Column(name = "skill_name", nullable = false)
    private String skillName;

    @Column(name = "is_preferred", nullable = false)
    private boolean isPreferred;

    public JobSkill() {
    }

    public JobSkill(Job job, String skillName, boolean isPreferred) {
        this.job = job;
        this.skillName = skillName;
        this.isPreferred = isPreferred;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public boolean isPreferred() {
        return isPreferred;
    }

    public void setPreferred(boolean preferred) {
        isPreferred = preferred;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobSkill jobSkill = (JobSkill) o;
        return isPreferred == jobSkill.isPreferred &&
               java.util.Objects.equals(skillName != null ? skillName.toLowerCase() : null,
                                       jobSkill.skillName != null ? jobSkill.skillName.toLowerCase() : null);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(skillName != null ? skillName.toLowerCase() : null, isPreferred);
    }
}
