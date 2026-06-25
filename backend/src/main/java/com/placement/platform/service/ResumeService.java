package com.placement.platform.service;

import com.placement.platform.dto.ResumeDownloadWrapper;
import com.placement.platform.dto.ResumeResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface ResumeService {
    ResumeResponseDto uploadResume(MultipartFile file);
    ResumeResponseDto getResumeMetadata();
    ResumeDownloadWrapper downloadResume();
    ResumeResponseDto replaceResume(MultipartFile file);
    void deleteResume();
}
