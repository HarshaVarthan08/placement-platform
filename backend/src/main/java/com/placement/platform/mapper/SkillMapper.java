package com.placement.platform.mapper;

import com.placement.platform.dto.SkillDto;
import com.placement.platform.entity.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper {

    public SkillDto toDto(Skill skill) {
        if (skill == null) {
            return null;
        }
        return new SkillDto(
                skill.getId(),
                skill.getName()
        );
    }
}
