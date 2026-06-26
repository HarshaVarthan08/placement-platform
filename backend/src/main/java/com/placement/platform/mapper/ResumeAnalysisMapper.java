package com.placement.platform.mapper;

import com.placement.platform.dto.ResumeAnalysisResponseDto;
import com.placement.platform.entity.ResumeAnalysis;
import org.springframework.stereotype.Component;

@Component
public class ResumeAnalysisMapper {

    public ResumeAnalysisResponseDto toDto(ResumeAnalysis resumeAnalysis) {
        if (resumeAnalysis == null) {
            return null;
        }
        return new ResumeAnalysisResponseDto(
                resumeAnalysis.getAtsScore(),
                resumeAnalysis.getSummary(),
                resumeAnalysis.getStrengths(),
                resumeAnalysis.getWeaknesses(),
                resumeAnalysis.getMissingSkills(),
                resumeAnalysis.getSuggestions(),
                resumeAnalysis.getCreatedAt()
        );
    }
}
