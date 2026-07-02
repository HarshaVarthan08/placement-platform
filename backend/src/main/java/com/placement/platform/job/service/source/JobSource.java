package com.placement.platform.job.service.source;

import com.placement.platform.job.dto.JobImportDto;
import com.placement.platform.job.entity.JobSourceType;
import java.util.List;

public interface JobSource {
    List<JobImportDto> fetchJobs();
    JobSourceType getSourceType();
    boolean supportsIncrementalSync();
}
