package com.placement.platform.controller;

import com.placement.platform.dto.AddCompanyRequestDto;
import com.placement.platform.dto.CompanyDto;
import com.placement.platform.dto.CompanyDetailResponseDto;
import com.placement.platform.dto.CompanyHubResponseDto;
import com.placement.platform.dto.CreateCompanyRequestDto;
import com.placement.platform.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Companies", description = "Endpoints for managing user target companies and the company knowledge hub")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // --- User Profile Companies ---

    @GetMapping("/profile/companies")
    @Operation(summary = "Get user's target companies", description = "Retrieves the list of target companies the authenticated user is tracking.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of user companies")
    public ResponseEntity<List<CompanyDto>> getUserCompanies() {
        return ResponseEntity.ok(companyService.getUserCompanies());
    }

    @PostMapping("/profile/companies")
    @Operation(summary = "Add target company to profile", description = "Tracks a company from the company hub in the user's profile with personal tracking details (target date, status, etc.).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Company successfully added to user profile"),
        @ApiResponse(responseCode = "400", description = "Invalid request body or company already added")
    })
    public ResponseEntity<CompanyDto> addCompany(@Valid @RequestBody AddCompanyRequestDto request) {
        return ResponseEntity.ok(companyService.addCompany(request));
    }

    @DeleteMapping("/profile/companies/{companyId}")
    @Operation(summary = "Remove target company from profile", description = "Untracks/removes a company from the user's profile.")
    @ApiResponses({
        @ApiResponse(responseCode = "244", description = "Successfully removed company from profile (No Content)"),
        @ApiResponse(responseCode = "404", description = "Target company tracking entry not found")
    })
    public ResponseEntity<Void> deleteCompany(@PathVariable Long companyId) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.noContent().build();
    }

    // --- General Company Hub & Management ---

    @GetMapping("/companies")
    @Operation(summary = "Get all registered companies in the hub", description = "Returns a list of all company profiles in the system directory.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all companies")
    public ResponseEntity<List<CompanyHubResponseDto>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/company/{id}")
    @Operation(summary = "Get company details", description = "Retrieves detailed information (description, industry, website, interview details, open roles) for a specific company.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved company details"),
        @ApiResponse(responseCode = "404", description = "Company not found")
    })
    public ResponseEntity<CompanyDetailResponseDto> getCompanyDetails(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyDetails(id));
    }

    @PostMapping("/companies")
    @Operation(summary = "Create a new company profile", description = "Admin/system capability to add a new company to the general knowledge hub.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Company successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid company creation payload")
    })
    public ResponseEntity<CompanyDetailResponseDto> createCompany(@Valid @RequestBody CreateCompanyRequestDto request) {
        return ResponseEntity.ok(companyService.createCompany(request));
    }
}
