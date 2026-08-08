package com.abtalks.interview.prompt;

import com.abtalks.interview.domain.ConversationTurn;
import com.abtalks.interview.model.curriculum.CurriculumDay;
import com.abtalks.interview.repository.JsonCurriculumRepository;
import org.springframework.stereotype.Component;

@Component
public class EvaluationPromptBuilder {

    private final JsonCurriculumRepository curriculumRepository;

    public EvaluationPromptBuilder(
            JsonCurriculumRepository curriculumRepository) {

        this.curriculumRepository = curriculumRepository;
    }

    public String buildEvaluationPrompt(
            ConversationTurn turn) {

        CurriculumDay day =
                curriculumRepository
                        .findDay(turn.getCurriculumDay())
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Curriculum day not found"
                                ));

        return """
                You are evaluating a candidate during a technical interview.

                CURRICULUM TOPIC
                Day: %d
                Topic: %s

                LEARNING OBJECTIVES
                %s

                QUESTION
                %s

                CANDIDATE ANSWER
                %s

                Evaluate the answer based on:

                1. Technical correctness
                2. Understanding of the concept
                3. Depth of explanation
                4. Practical reasoning
                5. Alignment with the learning objectives

                Provide:

                - Score from 0 to 10
                - Strengths
                - Weaknesses
                - Whether a follow-up question is needed

                Do not judge grammar or writing style unless
                it affects technical clarity.

                Return the evaluation in structured JSON.
                """.formatted(
                day.getDay(),
                day.getTitle(),
                String.join("\n- ", day.getObjectives()),
                turn.getQuestion(),
                turn.getAnswer()
        );
    }
}