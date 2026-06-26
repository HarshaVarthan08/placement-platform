package com.placement.platform.controller;

import com.placement.platform.dto.AddCompanyRequestDto;
import com.placement.platform.dto.CompanyDto;
import com.placement.platform.dto.CompanyDetailResponseDto;
import com.placement.platform.dto.CompanyHubResponseDto;
import com.placement.platform.dto.CreateCompanyRequestDto;
import com.placement.platform.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // --- User Profile Companies ---

    @GetMapping("/profile/companies")
    public ResponseEntity<List<CompanyDto>> getUserCompanies() {
        return ResponseEntity.ok(companyService.getUserCompanies());
    }

    @PostMapping("/profile/companies")
    public ResponseEntity<CompanyDto> addCompany(@Valid @RequestBody AddCompanyRequestDto request) {
        return ResponseEntity.ok(companyService.addCompany(request));
    }

    @DeleteMapping("/profile/companies/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long companyId) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.noContent().build();
    }

    // --- General Company Hub & Management ---

    @GetMapping("/companies")
    public ResponseEntity<List<CompanyHubResponseDto>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<CompanyDetailResponseDto> getCompanyDetails(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyDetails(id));
    }

    @PostMapping("/companies")
    public ResponseEntity<CompanyDetailResponseDto> createCompany(@Valid @RequestBody CreateCompanyRequestDto request) {
        return ResponseEntity.ok(companyService.createCompany(request));
    }
}
