package com.placement.platform.mapper;

import com.placement.platform.dto.ProfileResponseDto;
import com.placement.platform.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public ProfileResponseDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return new ProfileResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCollege(),
                user.getDegree(),
                user.getBranch(),
                user.getCgpa(),
                user.getGraduationYear(),
                user.getTargetRole(),
                user.getProjects(),
                user.getInternship()
        );
    }
}
