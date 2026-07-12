package com.placement.platform.controller;

import com.placement.platform.dto.AddSkillRequestDto;
import com.placement.platform.dto.SkillDto;
import com.placement.platform.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile/skills")
@Tag(name = "Skills", description = "Endpoints for managing user technical and professional skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    @Operation(summary = "Get user skills", description = "Retrieves the list of skills associated with the authenticated user's profile.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user skills list")
    public ResponseEntity<List<SkillDto>> getUserSkills() {
        return ResponseEntity.ok(skillService.getUserSkills());
    }

    @PostMapping
    @Operation(summary = "Add skill to profile", description = "Adds a new skill to the authenticated user's profile (increments profile version).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Skill successfully added"),
        @ApiResponse(responseCode = "400", description = "Invalid request payload or skill already added")
    })
    public ResponseEntity<SkillDto> addSkill(@Valid @RequestBody AddSkillRequestDto request) {
        return ResponseEntity.ok(skillService.addSkill(request));
    }

    @DeleteMapping("/{skillId}")
    @Operation(summary = "Delete skill from profile", description = "Removes a specific skill from the authenticated user's profile (increments profile version).")
    @ApiResponses({
        @ApiResponse(responseCode = "244", description = "Skill successfully deleted (No Content)"),
        @ApiResponse(responseCode = "404", description = "Skill not found in user profile")
    })
    public ResponseEntity<Void> deleteSkill(@PathVariable Long skillId) {
        skillService.deleteSkill(skillId);
        return ResponseEntity.noContent().build();
    }
}
