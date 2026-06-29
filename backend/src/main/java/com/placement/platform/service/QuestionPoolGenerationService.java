package com.placement.platform.service;

import com.placement.platform.entity.InterviewDifficulty;
import com.placement.platform.entity.InterviewQuestionPool;
import com.placement.platform.entity.User;

public interface QuestionPoolGenerationService {
    InterviewQuestionPool generateQuestionPool(User user, InterviewDifficulty difficulty);
}
