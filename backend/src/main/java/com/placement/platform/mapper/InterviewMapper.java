package com.placement.platform.mapper;

import com.placement.platform.dto.InterviewSessionResponseDto;
import com.placement.platform.entity.InterviewSession;
import org.springframework.stereotype.Component;

@Component
public class InterviewMapper {

    public InterviewSessionResponseDto toDto(InterviewSession session) {
        if (session == null) {
            return null;
        }
        return new InterviewSessionResponseDto(
                session.getId(),
                session.getMode(),
                session.getStatus(),
                session.getStartedAt()
        );
    }
}
