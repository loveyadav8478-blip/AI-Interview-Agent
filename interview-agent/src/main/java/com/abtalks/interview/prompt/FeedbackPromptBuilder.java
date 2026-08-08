package com.abtalks.interview.prompt;

import com.abtalks.interview.domain.ConversationTurn;
import com.abtalks.interview.domain.Evaluation;
import com.abtalks.interview.domain.InterviewSession;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FeedbackPromptBuilder {

    public String buildPrompt(
            InterviewSession session) {

        String evaluations =
                session.getEvaluations()
                        .stream()
                        .map(this::formatEvaluation)
                        .collect(Collectors.joining("\n\n"));

        String conversation =
                session.getConversationHistory()
                        .stream()
                        .map(this::formatTurn)
                        .collect(Collectors.joining("\n\n"));

        return """
                [OUTPUT_TYPE=FEEDBACK]

                You are generating final technical interview feedback.

                Your feedback must be based ONLY on the interview
                questions, candidate answers, and evaluations provided
                below.

                Do not invent strengths or weaknesses that were not
                demonstrated during the interview.

                INTERVIEW EVALUATIONS

                %s

                INTERVIEW CONVERSATION

                %s

                Generate concise, actionable feedback.

                The feedback must contain:

                1. summary
                2. strengths
                3. gaps
                4. next

                SUMMARY:
                Provide an overall assessment of the candidate's
                technical performance.

                STRENGTHS:
                Identify specific technical areas demonstrated well.

                GAPS:
                Identify specific concepts where the candidate
                demonstrated insufficient depth or incorrect reasoning.

                NEXT:
                Provide concrete learning or practice recommendations.

                Return ONLY valid JSON.

                Required structure:

                {
                  "summary": "",
                  "strengths": [],
                  "gaps": [],
                  "next": []
                }
                """.formatted(
                evaluations,
                conversation
        );
    }

    private String formatEvaluation(
            Evaluation evaluation) {

        return """
                Question %d
                Score: %s
                Strengths: %s
                Weaknesses: %s
                Reasoning: %s
                Follow-up Needed: %s
                """.formatted(
                evaluation.getQuestionNumber(),
                evaluation.getScore(),
                evaluation.getStrengths(),
                evaluation.getWeaknesses(),
                evaluation.getReasoning(),
                evaluation.getFollowUpNeeded()
        );
    }

    private String formatTurn(
            ConversationTurn turn) {

        return """
                Question %d
                Curriculum Day: %d
                Topic: %s
                Difficulty: %s

                Question:
                %s

                Candidate Answer:
                %s
                """.formatted(
                turn.getQuestionNumber(),
                turn.getCurriculumDay(),
                turn.getTopic(),
                turn.getDifficulty(),
                turn.getQuestion(),
                turn.getAnswer()
        );
    }
}