package com.placement.platform.controller;

import com.placement.platform.dto.ResumeDownloadWrapper;
import com.placement.platform.dto.ResumeResponseDto;
import com.placement.platform.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
@Tag(name = "Resume", description = "Endpoints for resume uploading and AI analysis processing")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload resume file", description = "Uploads a new resume file (PDF, DOC, DOCX) and extracts its raw text content.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Resume successfully uploaded"),
        @ApiResponse(responseCode = "400", description = "Invalid file type or size, or resume already exists")
    })
    public ResponseEntity<ResumeResponseDto> uploadResume(@RequestParam("file") MultipartFile file) {
        ResumeResponseDto response = resumeService.uploadResume(file);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    @Operation(summary = "Get resume metadata", description = "Retrieves information details (file name, type, size, parsed state) for the current user's active resume.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved resume metadata"),
        @ApiResponse(responseCode = "404", description = "No resume uploaded yet")
    })
    public ResponseEntity<ResumeResponseDto> getResumeMetadata() {
        ResumeResponseDto response = resumeService.getResumeMetadata();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download")
    @Operation(summary = "Download active resume", description = "Downloads the physical resume document associated with the user.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully downloaded resume file"),
        @ApiResponse(responseCode = "404", description = "Resume file not found")
    })
    public ResponseEntity<Resource> downloadResume() {
        ResumeDownloadWrapper downloadWrapper = resumeService.downloadResume();
        String contentType = getContentType(downloadWrapper.originalFileName());
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadWrapper.originalFileName() + "\"")
                .body(downloadWrapper.resource());
    }

    @PutMapping(value = "/replace", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Replace active resume", description = "Deletes the existing resume file from storage and uploads a new one, resetting profile and analysis metadata.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resume successfully replaced"),
        @ApiResponse(responseCode = "400", description = "Invalid file upload payload"),
        @ApiResponse(responseCode = "404", description = "No resume found to replace")
    })
    public ResponseEntity<ResumeResponseDto> replaceResume(@RequestParam("file") MultipartFile file) {
        ResumeResponseDto response = resumeService.replaceResume(file);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @Operation(summary = "Delete active resume", description = "Removes the user's resume file from physical storage and metadata entries from the database.")
    @ApiResponses({
        @ApiResponse(responseCode = "244", description = "Resume successfully deleted (No Content)"),
        @ApiResponse(responseCode = "404", description = "No resume found to delete")
    })
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
