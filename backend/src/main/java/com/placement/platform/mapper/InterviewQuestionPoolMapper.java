package com.placement.platform.mapper;

import com.placement.platform.dto.QuestionPoolResponseDto;
import com.placement.platform.dto.QuestionResponseDto;
import com.placement.platform.entity.InterviewQuestion;
import com.placement.platform.entity.InterviewQuestionPool;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InterviewQuestionPoolMapper {

    public QuestionPoolResponseDto toDto(InterviewQuestionPool pool, List<InterviewQuestion> questions) {
        if (pool == null) {
            return null;
        }

        List<QuestionResponseDto> questionDtos = Collections.emptyList();
        if (questions != null) {
            questionDtos = questions.stream()
                    .map(this::toQuestionDto)
                    .collect(Collectors.toList());
        }

        return new QuestionPoolResponseDto(
                pool.getId(),
                pool.getProfileVersion(),
                pool.getStatus(),
                pool.getGeneratedAt(),
                pool.getUpdatedAt(),
                questionDtos
        );
    }

    public QuestionResponseDto toQuestionDto(InterviewQuestion question) {
        if (question == null) {
            return null;
        }

        return new QuestionResponseDto(
                question.getId(),
                question.getQuestion(),
                question.getIdealAnswer(),
                question.getKeyPoints(),
                question.getTopic(),
                question.getCategory(),
                question.getDifficulty(),
                question.getDisplayOrder()
        );
    }
}
