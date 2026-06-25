package com.placement.platform.controller;

import com.placement.platform.dto.ResumeDownloadWrapper;
import com.placement.platform.dto.ResumeResponseDto;
import com.placement.platform.service.ResumeService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResumeResponseDto> uploadResume(@RequestParam("file") MultipartFile file) {
        ResumeResponseDto response = resumeService.uploadResume(file);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<ResumeResponseDto> getResumeMetadata() {
        ResumeResponseDto response = resumeService.getResumeMetadata();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadResume() {
        ResumeDownloadWrapper downloadWrapper = resumeService.downloadResume();
        String contentType = getContentType(downloadWrapper.originalFileName());
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadWrapper.originalFileName() + "\"")
                .body(downloadWrapper.resource());
    }

    @PutMapping(value = "/replace", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResumeResponseDto> replaceResume(@RequestParam("file") MultipartFile file) {
        ResumeResponseDto response = resumeService.replaceResume(file);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteResume() {
        resumeService.deleteResume();
        return ResponseEntity.noContent().build();
    }

    private String getContentType(String filename) {
        if (filename.endsWith(".pdf")) {
            return "application/pdf";
        } else if (filename.endsWith(".doc")) {
            return "application/msword";
        } else if (filename.endsWith(".docx")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }
        return "application/octet-stream";
    }
}
