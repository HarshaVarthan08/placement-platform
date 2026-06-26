package com.placement.platform.service;

import com.placement.platform.dto.ResumeDownloadWrapper;
import com.placement.platform.dto.ResumeResponseDto;
import com.placement.platform.entity.Resume;
import com.placement.platform.entity.User;
import com.placement.platform.exception.InvalidFileTypeException;
import com.placement.platform.exception.ResourceNotFoundException;
import com.placement.platform.exception.ResumeAlreadyExistsException;
import com.placement.platform.exception.UserNotFoundException;
import com.placement.platform.mapper.ResumeMapper;
import com.placement.platform.repository.ResumeAnalysisRepository;
import com.placement.platform.repository.ResumeRepository;
import com.placement.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final ResumeMapper resumeMapper;
    private final Path rootLocation;
    private final ResumeAnalysisRepository resumeAnalysisRepository;

    public ResumeServiceImpl(
            ResumeRepository resumeRepository,
            UserRepository userRepository,
            ResumeMapper resumeMapper,
            ResumeAnalysisRepository resumeAnalysisRepository,
            @Value("${application.resume.upload-dir:uploads/resumes}") String uploadDir
    ) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.resumeMapper = resumeMapper;
        this.resumeAnalysisRepository = resumeAnalysisRepository;
        this.rootLocation = Paths.get(uploadDir);
        try {
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory: " + uploadDir, e);
        }
    }

    @Override
    @Transactional
    public ResumeResponseDto uploadResume(MultipartFile file) {
        User user = getAuthenticatedUser();

        if (resumeRepository.existsByUser(user)) {
            throw new ResumeAlreadyExistsException("User already has a resume. Use PUT /api/resume/replace to update.");
        }

        validateFile(file);

        String originalFilename = file.getOriginalFilename();
        String storedFilename = "user_" + user.getId() + "_" + System.currentTimeMillis() + "_" + originalFilename;
        Path targetLocation = this.rootLocation.resolve(storedFilename);

        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store physical file " + originalFilename, e);
        }

        String contentType = file.getContentType();
        if (contentType == null || contentType.isEmpty()) {
            contentType = getContentTypeFromFileName(originalFilename);
        }

        Resume resume = new Resume(
                user,
                originalFilename,
                storedFilename,
                targetLocation.toAbsolutePath().toString(),
                contentType,
                file.getSize()
        );

        Resume savedResume = resumeRepository.save(resume);
        return resumeMapper.toDto(savedResume);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeResponseDto getResumeMetadata() {
        User user = getAuthenticatedUser();
        Resume resume = resumeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("No resume found for the current user."));
        return resumeMapper.toDto(resume);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeDownloadWrapper downloadResume() {
        User user = getAuthenticatedUser();
        Resume resume = resumeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("No resume found for the current user."));

        Path filePath = Paths.get(resume.getFilePath());
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return new ResumeDownloadWrapper(resource, resume.getOriginalFileName());
            } else {
                throw new ResourceNotFoundException("Physical file not found or not readable: " + resume.getOriginalFileName());
            }
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("Error occurred while reading physical file: " + resume.getOriginalFileName());
        }
    }

    @Override
    @Transactional
    public ResumeResponseDto replaceResume(MultipartFile file) {
        User user = getAuthenticatedUser();
        Resume existingResume = resumeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("No resume found for the current user. Please upload a resume first."));

        validateFile(file);

        // Delete existing analysis if it exists to invalidate cache
        if (resumeAnalysisRepository.existsByResume(existingResume)) {
            resumeAnalysisRepository.deleteByResume(existingResume);
        }

        // Delete the old file from disk
        Path oldPath = Paths.get(existingResume.getFilePath());
        try {
            Files.deleteIfExists(oldPath);
        } catch (IOException e) {
            // Log issue but proceed to store the new one
            System.err.println("Could not delete old resume file: " + oldPath.toString());
        }

        // Save the new file to disk
        String originalFilename = file.getOriginalFilename();
        String storedFilename = "user_" + user.getId() + "_" + System.currentTimeMillis() + "_" + originalFilename;
        Path targetLocation = this.rootLocation.resolve(storedFilename);

        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store physical file " + originalFilename, e);
        }

        String contentType = file.getContentType();
        if (contentType == null || contentType.isEmpty()) {
            contentType = getContentTypeFromFileName(originalFilename);
        }

        // Update metadata
        existingResume.setOriginalFileName(originalFilename);
        existingResume.setFileName(storedFilename);
        existingResume.setFilePath(targetLocation.toAbsolutePath().toString());
        existingResume.setFileType(contentType);
        existingResume.setFileSize(file.getSize());

        Resume updatedResume = resumeRepository.save(existingResume);
        return resumeMapper.toDto(updatedResume);
    }

    @Override
    @Transactional
    public void deleteResume() {
        User user = getAuthenticatedUser();
        Resume resume = resumeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("No resume found for the current user."));

        // Delete existing analysis if it exists
        if (resumeAnalysisRepository.existsByResume(resume)) {
            resumeAnalysisRepository.deleteByResume(resume);
        }

        // Delete from disk
        Path filePath = Paths.get(resume.getFilePath());
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("Could not delete physical resume file: " + filePath.toString());
        }

        // Delete from database
        resumeRepository.delete(resume);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("User is not authenticated");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileTypeException("Uploaded file cannot be empty");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new InvalidFileTypeException("File name cannot be empty");
        }

        String extension = "";
        int lastIndexOf = originalFilename.lastIndexOf('.');
        if (lastIndexOf >= 0) {
            extension = originalFilename.substring(lastIndexOf + 1).toLowerCase();
        }

        if (!extension.equals("pdf") && !extension.equals("doc") && !extension.equals("docx")) {
            throw new InvalidFileTypeException("Invalid file type. Only PDF, DOC, and DOCX are allowed.");
        }

        // Check file size (5 MB limit)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new MaxUploadSizeExceededException(5 * 1024 * 1024);
        }
    }

    private String getContentTypeFromFileName(String filename) {
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
