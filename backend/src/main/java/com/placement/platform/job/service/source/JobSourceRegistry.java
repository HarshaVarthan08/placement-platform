package com.placement.platform.job.service.source;

import com.placement.platform.job.entity.JobSourceType;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JobSourceRegistry {

    private final Map<JobSourceType, JobSource> sources = new EnumMap<>(JobSourceType.class);

    public JobSourceRegistry(List<JobSource> jobSources) {
        for (JobSource source : jobSources) {
            sources.put(source.getSourceType(), source);
        }
    }

    public Optional<JobSource> getSource(JobSourceType type) {
        return Optional.ofNullable(sources.get(type));
    }

    public Collection<JobSource> getAllSources() {
        return sources.values();
    }
}
