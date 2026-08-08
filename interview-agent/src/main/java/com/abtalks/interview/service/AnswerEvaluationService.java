package com.abtalks.interview.service;

import com.abtalks.interview.domain.ConversationTurn;
import com.abtalks.interview.domain.Evaluation;
import com.abtalks.interview.prompt.EvaluationPromptBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class AnswerEvaluationService {

    private final EvaluationPromptBuilder promptBuilder;

    private final LLMService llmService;

    private final ObjectMapper objectMapper;

    public AnswerEvaluationService(
            EvaluationPromptBuilder promptBuilder,
            LLMService llmService,
            ObjectMapper objectMapper) {

        this.promptBuilder = promptBuilder;
        this.llmService = llmService;
        this.objectMapper = objectMapper;
    }

    public Evaluation evaluate(
            ConversationTurn turn) {

        String prompt =
                promptBuilder.buildEvaluationPrompt(turn);

        String response =
                llmService.generateJson(prompt);

        try {

            Evaluation evaluation =
                    objectMapper.readValue(
                            cleanJson(response),
                            Evaluation.class
                    );

            evaluation.setQuestionNumber(
                    turn.getQuestionNumber()
            );

            return evaluation;

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Unable to parse LLM evaluation",
                    e
            );
        }
    }

    private String cleanJson(String response) {

        if (response == null || response.isBlank()) {

            throw new IllegalStateException(
                    "LLM returned an empty evaluation"
            );
        }

        String cleaned = response.trim();

        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        }

        if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }

        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(
                    0,
                    cleaned.length() - 3
            );
        }

        return cleaned.trim();
    }
}