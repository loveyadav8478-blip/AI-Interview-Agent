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
        [OUTPUT_TYPE=EVALUATION]

        You are evaluating a candidate during a technical interview.

        Evaluate ONLY the candidate's technical response.

        Use the curriculum objectives as the evaluation criteria.

        Do not penalize:
        - grammar
        - spelling
        - writing style
        - brevity by itself

        Do penalize:
        - factual errors
        - missing important concepts
        - incorrect reasoning
        - inability to explain implementation details
        - contradictions

        Score from 0 to 10.

        Score guidance:

        0-2:
        Fundamentally incorrect or no meaningful understanding.

        3-4:
        Limited understanding with major gaps.

        5-6:
        Basic understanding but lacks depth.

        7-8:
        Strong understanding with minor gaps.

        9-10:
        Excellent technical depth and practical reasoning.

        CURRICULUM TOPIC
        Day: %d
        Topic: %s

        LEARNING OBJECTIVES
        %s

        QUESTION
        %s

        CANDIDATE ANSWER
        %s

        Return ONLY valid JSON.

        Required JSON structure:

        {
          "questionNumber": %d,
          "score": 0.0,
          "strengths": [],
          "weaknesses": [],
          "reasoning": "",
          "followUpNeeded": true
        }
        """.formatted(
                day.getDay(),
                day.getTitle(),
                String.join("\n- ", day.getObjectives()),
                turn.getQuestion(),
                turn.getAnswer(),
                turn.getQuestionNumber()
        );
    }
}