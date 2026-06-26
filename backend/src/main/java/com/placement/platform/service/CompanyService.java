package com.placement.platform.service;

import com.placement.platform.dto.AddCompanyRequestDto;
import com.placement.platform.dto.CompanyDto;
import com.placement.platform.dto.CompanyDetailResponseDto;
import com.placement.platform.dto.CompanyHubResponseDto;
import com.placement.platform.dto.CreateCompanyRequestDto;

import java.util.List;

public interface CompanyService {
    List<CompanyDto> getUserCompanies();
    CompanyDto addCompany(AddCompanyRequestDto request);
    void deleteCompany(Long companyId);

    // General Company Hub APIs
    List<CompanyHubResponseDto> getAllCompanies();
    CompanyDetailResponseDto getCompanyDetails(Long companyId);
    CompanyDetailResponseDto createCompany(CreateCompanyRequestDto request);
}
