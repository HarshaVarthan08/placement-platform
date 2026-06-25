package com.placement.platform.dto;

import org.springframework.core.io.Resource;

public record ResumeDownloadWrapper(
    Resource resource,
    String originalFileName
) {}
