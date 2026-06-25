package com.placement.platform.service;

import com.placement.platform.dto.AddSkillRequestDto;
import com.placement.platform.dto.SkillDto;

import java.util.List;

public interface SkillService {
    List<SkillDto> getUserSkills();
    SkillDto addSkill(AddSkillRequestDto request);
    void deleteSkill(Long skillId);
}
