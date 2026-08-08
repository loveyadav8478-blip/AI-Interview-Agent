package com.abtalks.interview.service;

import com.abtalks.interview.domain.InterviewSession;
import com.abtalks.interview.domain.PlannerDecision;
import com.abtalks.interview.prompt.PromptBuilder;
import org.springframework.stereotype.Service;

@Service
public class QuestionGenerationService {

    private final PromptBuilder promptBuilder;

    private final LLMService llmService;

    public QuestionGenerationService(
            PromptBuilder promptBuilder,
            LLMService llmService) {

        this.promptBuilder = promptBuilder;
        this.llmService = llmService;
    }

    public String generateQuestion(
            InterviewSession session,
            PlannerDecision decision) {

        String prompt =
                promptBuilder.buildQuestionPrompt(
                        session,
                        decision
                );

        return llmService.generate(prompt);
    }
}