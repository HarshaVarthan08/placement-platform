package com.placement.platform.mapper;

import com.placement.platform.dto.InterviewEvaluationResponseDto;
import com.placement.platform.dto.QuestionEvaluationResponseDto;
import com.placement.platform.entity.InterviewEvaluation;
import com.placement.platform.entity.QuestionEvaluation;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InterviewEvaluationMapper {

    public InterviewEvaluationResponseDto toDto(InterviewEvaluation evaluation, List<QuestionEvaluation> questionEvaluations) {
        if (evaluation == null) {
            return null;
        }

        List<QuestionEvaluationResponseDto> questionDtos = questionEvaluations.stream()
                .map(this::toQuestionDto)
                .collect(Collectors.toList());

        return new InterviewEvaluationResponseDto(
                evaluation.getId(),
                evaluation.getInterviewSession().getId(),
                evaluation.getOverallScore(),
                evaluation.getTechnicalScore(),
                evaluation.getCommunicationScore(),
                evaluation.getProblemSolvingScore(),
                evaluation.getConfidenceScore(),
                evaluation.getProfileMatchScore(),
                evaluation.getPerformanceBand() != null ? evaluation.getPerformanceBand().name() : null,
                evaluation.getVerdict() != null ? evaluation.getVerdict().name() : null,
                evaluation.getVerdictJustification(),
                evaluation.getSummary(),
                evaluation.getOverallFeedback(),
                evaluation.getStrengths(),
                evaluation.getWeaknesses(),
                evaluation.getRecommendedTopics(),
                evaluation.getLearningPlan(),
                evaluation.getModelUsed(),
                evaluation.getStatus() != null ? evaluation.getStatus().name() : null,
                evaluation.getEvaluatedAt(),
                questionDtos
        );
    }

    public QuestionEvaluationResponseDto toQuestionDto(QuestionEvaluation questionEvaluation) {
        if (questionEvaluation == null) {
            return null;
        }
        return new QuestionEvaluationResponseDto(
                questionEvaluation.getId(),
                questionEvaluation.getInterviewSessionQuestion().getId(),
                questionEvaluation.getInterviewSessionQuestion().getQuestionText(),
                questionEvaluation.getScore(),
                questionEvaluation.getStrengths(),
                questionEvaluation.getWeaknesses(),
                questionEvaluation.getFeedback(),
                questionEvaluation.getImprovement(),
                questionEvaluation.getIdealAnswer(),
                questionEvaluation.getInterviewSessionQuestion().getTextAnswer()
        );
    }
}
