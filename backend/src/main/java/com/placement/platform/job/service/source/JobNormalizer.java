package com.placement.platform.job.service.source;

import com.placement.platform.job.dto.JobImportDto;
import com.placement.platform.job.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class JobNormalizer {

    private static final Logger log = LoggerFactory.getLogger(JobNormalizer.class);

    public JobImportResult processImports(JobSourceType source, List<JobImportDto> imports) {
        JobImportResult result = new JobImportResult();
        if (imports == null || imports.isEmpty()) {
            return result;
        }

        Set<String> seenExternalIds = new HashSet<>();
        Set<String> seenFingerprints = new HashSet<>();

        for (JobImportDto dto : imports) {
            // 1. Mandatory Fields Validation
            if (dto.getExternalId() == null || dto.getExternalId().trim().isEmpty()) {
                result.addInvalidJob(dto, "Missing External ID");
                continue;
            }
            if (dto.getCompany() == null || dto.getCompany().trim().isEmpty()) {
                result.addInvalidJob(dto, "Missing Company");
                continue;
            }
            if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
                result.addInvalidJob(dto, "Missing Title");
                continue;
            }
            if (dto.getEmploymentType() == null || dto.getEmploymentType().trim().isEmpty()) {
                result.addInvalidJob(dto, "Missing Employment Type");
                continue;
            }
            if (dto.getLocations() == null || dto.getLocations().isEmpty()) {
                result.addInvalidJob(dto, "Missing Location");
                continue;
            }
            if (dto.getRequiredSkills() == null || dto.getRequiredSkills().isEmpty()) {
                result.addInvalidJob(dto, "Missing Skills");
                continue;
            }

            // 2. Salary Fields Validation
            if (dto.getSalaryMin() == null || dto.getSalaryMax() == null || dto.getSalaryCurrency() == null) {
                result.addInvalidJob(dto, "Missing Salary fields (salaryMin, salaryMax, or salaryCurrency)");
                continue;
            }
            if (dto.getSalaryMin().compareTo(BigDecimal.ZERO) < 0 || dto.getSalaryMax().compareTo(BigDecimal.ZERO) < 0) {
                result.addInvalidJob(dto, "Invalid Salary: Salary values cannot be negative");
                continue;
            }
            if (dto.getSalaryMin().compareTo(dto.getSalaryMax()) > 0) {
                result.addInvalidJob(dto, "Invalid Salary: Minimum salary cannot exceed maximum salary");
                continue;
            }

            // 3. Batch-level Duplicate External ID check
            if (seenExternalIds.contains(dto.getExternalId())) {
                result.addInvalidJob(dto, "Duplicate External ID found in the import batch");
                continue;
            }

            // 4. Employment Type Mapping
            EmploymentType empType = mapEmploymentType(dto.getEmploymentType());
            if (empType == null) {
                result.addInvalidJob(dto, "Invalid Employment Type value: " + dto.getEmploymentType());
                continue;
            }

            // 5. Role Normalization
            String normalizedRole = normalizeRole(dto.getTitle());

            // 6. Location Normalization (trim, capitalize, de-duplicate)
            List<String> normalizedLocations = normalizeLocations(dto.getLocations());
            if (normalizedLocations.isEmpty()) {
                result.addInvalidJob(dto, "Missing Location after normalization");
                continue;
            }
            String primaryLocation = normalizedLocations.get(0);

            // 7. Fingerprint Generation
            String fingerprint = generateFingerprint(source, dto.getCompany(), normalizedRole, empType, primaryLocation);

            // 8. Batch-level Duplicate Fingerprint check
            if (seenFingerprints.contains(fingerprint)) {
                result.addDuplicateJob(dto);
                continue;
            }

            // Mark as seen in this batch
            seenExternalIds.add(dto.getExternalId());
            seenFingerprints.add(fingerprint);

            // 9. Build Job Entity
            Job job = new Job();
            job.setExternalId(dto.getExternalId().trim());
            job.setSource(source);
            job.setCompany(dto.getCompany().trim());
            job.setTitle(dto.getTitle().trim());
            job.setNormalizedRole(normalizedRole);
            job.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : "");
            job.setEmploymentType(empType);
            job.setMinimumExperience(dto.getMinimumExperience() != null ? dto.getMinimumExperience() : 0);
            job.setMinimumCGPA(dto.getMinimumCGPA() != null ? dto.getMinimumCGPA() : BigDecimal.ZERO);
            job.setSalaryMin(dto.getSalaryMin());
            job.setSalaryMax(dto.getSalaryMax());
            job.setSalaryCurrency(dto.getSalaryCurrency());
            job.setStatus(JobStatus.ACTIVE);
            job.setFingerprint(fingerprint);
            job.setVersion(1);

            LocalDateTime now = LocalDateTime.now();
            job.setCreatedAt(now);
            job.setUpdatedAt(now);
            job.setLastSyncedAt(now);

            // Normalize and associate skills
            Set<String> processedSkills = new HashSet<>();
            
            // Required skills (isPreferred = false)
            for (String skill : dto.getRequiredSkills()) {
                String normSkill = normalizeSkill(skill);
                if (normSkill != null && processedSkills.add(normSkill.toLowerCase())) {
                    JobSkill jobSkill = new JobSkill(job, normSkill, false);
                    job.addSkill(jobSkill);
                }
            }
            
            // Preferred skills (isPreferred = true)
            if (dto.getPreferredSkills() != null) {
                for (String skill : dto.getPreferredSkills()) {
                    String normSkill = normalizeSkill(skill);
                    if (normSkill != null && processedSkills.add(normSkill.toLowerCase())) {
                        JobSkill jobSkill = new JobSkill(job, normSkill, true);
                        job.addSkill(jobSkill);
                    }
                }
            }

            // Associate locations
            for (String loc : normalizedLocations) {
                JobLocation jobLoc = new JobLocation(job, loc);
                job.addLocation(jobLoc);
            }

            result.addValidJob(job);
        }

        return result;
    }

    private EmploymentType mapEmploymentType(String rawType) {
        if (rawType == null) return null;
        String clean = rawType.trim().toLowerCase().replace("-", "").replace("_", "");
        if (clean.contains("intern")) {
            return EmploymentType.INTERNSHIP;
        } else if (clean.contains("full")) {
            return EmploymentType.FULL_TIME;
        } else if (clean.contains("part")) {
            return EmploymentType.PART_TIME;
        } else if (clean.contains("contract")) {
            return EmploymentType.CONTRACT;
        }
        
        // Attempt direct enum value matching
        try {
            return EmploymentType.valueOf(rawType.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private String normalizeRole(String title) {
        if (title == null || title.trim().isEmpty()) {
            return "OTHER";
        }
        String clean = title.trim().toLowerCase();
        if (clean.contains("backend") || clean.contains("back-end")) {
            return "BACKEND_DEVELOPER";
        }
        if (clean.contains("frontend") || clean.contains("front-end") || clean.contains("ui") || clean.contains("ux")) {
            return "FRONTEND_DEVELOPER";
        }
        if (clean.contains("fullstack") || clean.contains("full stack") || clean.contains("full-stack")) {
            return "FULLSTACK_DEVELOPER";
        }
        if (clean.contains("devops") || clean.contains("reliability") || clean.contains("sre") || clean.contains("cloud")) {
            return "DEVOPS_ENGINEER";
        }
        if (clean.contains("data engineer") || clean.contains("data scientist") || clean.contains("data analyst") || clean.contains("analytics")) {
            return "DATA_ENGINEER";
        }
        if (clean.contains("qa") || clean.contains("quality assurance") || clean.contains("testing") || clean.contains("test")) {
            return "QA_ENGINEER";
        }
        if (clean.contains("software engineer") || clean.contains("software developer") || clean.contains("sde") || clean.contains("developer")) {
            return "SOFTWARE_ENGINEER";
        }
        
        // Custom default mapping for other roles
        String formatted = title.trim()
                .replaceAll("[^a-zA-Z0-9\\s]", " ")
                .replaceAll("\\s+", "_")
                .toUpperCase();
        return formatted.isEmpty() ? "OTHER" : formatted;
    }

    private List<String> normalizeLocations(List<String> locations) {
        if (locations == null) return Collections.emptyList();
        List<String> normalized = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (String loc : locations) {
            if (loc == null || loc.trim().isEmpty()) continue;
            String clean = capitalizeFully(loc.trim());
            if (seen.add(clean.toLowerCase())) {
                normalized.add(clean);
            }
        }
        return normalized;
    }

    private String capitalizeFully(String text) {
        String[] words = text.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) continue;
            sb.append(Character.toUpperCase(word.charAt(0)));
            if (word.length() > 1) {
                sb.append(word.substring(1).toLowerCase());
            }
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    private String normalizeSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) return null;
        String clean = skill.trim().toLowerCase();
        
        // Exact mappings
        if (clean.equals("spring")) return "Spring Framework";
        if (clean.equals("spring boot") || clean.equals("springboot")) return "Spring Boot";
        if (clean.equals("mysql") || clean.equals("my sql")) return "MySQL";
        if (clean.equals("sql")) return "SQL";
        if (clean.equals("mongodb") || clean.equals("mongo db")) return "MongoDB";
        if (clean.equals("postgresql") || clean.equals("postgres")) return "PostgreSQL";
        if (clean.equals("javascript") || clean.equals("js")) return "JavaScript";
        if (clean.equals("typescript") || clean.equals("ts")) return "TypeScript";
        if (clean.equals("aws")) return "AWS";
        if (clean.equals("azure")) return "Azure";
        if (clean.equals("docker")) return "Docker";
        if (clean.equals("kubernetes") || clean.equals("k8s")) return "Kubernetes";
        if (clean.equals("react") || clean.equals("reactjs") || clean.equals("react.js")) return "React";
        if (clean.equals("node") || clean.equals("nodejs") || clean.equals("node.js")) return "Node.js";
        if (clean.equals("java")) return "Java";
        if (clean.equals("python")) return "Python";
        if (clean.equals("c++")) return "C++";
        if (clean.equals("c#")) return "C#";
        if (clean.equals("html") || clean.equals("html5")) return "HTML";
        if (clean.equals("css") || clean.equals("css3")) return "CSS";
        if (clean.equals("git")) return "Git";
        
        return capitalizeFully(skill.trim());
    }

    public String generateFingerprint(JobSourceType source, String company, String normalizedRole, EmploymentType employmentType, String primaryLocation) {
        String input = String.format("%s|%s|%s|%s|%s",
                source.name(),
                company.trim().toLowerCase(),
                normalizedRole,
                employmentType.name(),
                primaryLocation.trim().toLowerCase()
        );
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            log.error("Failed to compute SHA-256 hash for fingerprint", e);
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
}
