package com.placement.platform.mapper;

import com.placement.platform.dto.ResumeResponseDto;
import com.placement.platform.entity.Resume;
import org.springframework.stereotype.Component;

@Component
public class ResumeMapper {

    public ResumeResponseDto toDto(Resume resume) {
        if (resume == null) {
            return null;
        }
        return new ResumeResponseDto(
                resume.getId(),
                resume.getOriginalFileName(),
                resume.getFileType(),
                resume.getFileSize(),
                resume.getUploadedAt(),
                resume.getUpdatedAt()
        );
    }
}
