package com.placement.platform.service;

import com.placement.platform.dto.AddCompanyRequestDto;
import com.placement.platform.dto.CompanyDto;

import java.util.List;

public interface CompanyService {
    List<CompanyDto> getUserCompanies();
    CompanyDto addCompany(AddCompanyRequestDto request);
    void deleteCompany(Long companyId);
}
