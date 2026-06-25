package com.placement.platform.controller;

import com.placement.platform.dto.AddSkillRequestDto;
import com.placement.platform.dto.SkillDto;
import com.placement.platform.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<List<SkillDto>> getUserSkills() {
        return ResponseEntity.ok(skillService.getUserSkills());
    }

    @PostMapping
    public ResponseEntity<SkillDto> addSkill(@Valid @RequestBody AddSkillRequestDto request) {
        return ResponseEntity.ok(skillService.addSkill(request));
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long skillId) {
        skillService.deleteSkill(skillId);
        return ResponseEntity.noContent().build();
    }
}
