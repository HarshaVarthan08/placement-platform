package com.placement.platform.mapper;

import com.placement.platform.dto.CompanyDto;
import com.placement.platform.entity.TargetCompany;
import org.springframework.stereotype.Component;

@Component
public class TargetCompanyMapper {

    public CompanyDto toDto(TargetCompany company) {
        if (company == null) {
            return null;
        }
        return new CompanyDto(
                company.getId(),
                company.getName()
        );
    }
}
