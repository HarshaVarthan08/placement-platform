package com.placement.platform.job.dto;

public class JobSkillDto {
    private Long id;
    private String skillName;
    private boolean isPreferred;

    public JobSkillDto() {
    }

    public JobSkillDto(Long id, String skillName, boolean isPreferred) {
        this.id = id;
        this.skillName = skillName;
        this.isPreferred = isPreferred;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
