package com.abtalks.interview.prompt;

import com.abtalks.interview.domain.PlannerAction;
import com.abtalks.interview.domain.PlannerDecision;
import com.abtalks.interview.domain.InterviewSession;
import com.abtalks.interview.domain.ConversationTurn;
import com.abtalks.interview.model.profile.Candidate;
import com.abtalks.interview.model.profile.Member;
import com.abtalks.interview.model.curriculum.CurriculumDay;
import com.abtalks.interview.repository.JsonCurriculumRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PromptBuilder {

    private final JsonCurriculumRepository curriculumRepository;

    public PromptBuilder(JsonCurriculumRepository curriculumRepository) {
        this.curriculumRepository = curriculumRepository;
    }

    public String buildQuestionPrompt(
            InterviewSession session,
            PlannerDecision decision) {

        Candidate candidate = session.getCandidate();
        Member member = candidate.getMember();

        CurriculumDay curriculumDay =
                curriculumRepository
                        .findDay(decision.getCurriculumDay())
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Curriculum day not found: "
                                                + decision.getCurriculumDay()
                                ));

        String conversation =
                buildConversationContext(session);

        String previousAnswer =
                getPreviousAnswer(session);

        return """
                You are a senior technical interviewer conducting
                a realistic technical interview.

                Your job is to assess the candidate's actual technical
                understanding, not to teach them.

                CANDIDATE
                Name: %s
                Role: %s
                Experience: %d years

                INTERVIEW CONTEXT
                Curriculum Day: %d
                Topic: %s
                Difficulty: %s

                LEARNING OBJECTIVES
                %s

                PREVIOUS CONVERSATION
                %s

                PREVIOUS ANSWER
                %s

                INTERVIEWER RULES

                1. Ask exactly ONE question.
                2. Do not provide the answer.
                3. Do not ask multiple questions in one message.
                4. Keep the question relevant to the curriculum topic.
                5. Match the requested difficulty.
                6. Use the previous answer when generating a follow-up.
                7. Do not repeat a question already asked.
                8. Behave like a real technical interviewer.
                9. Avoid unnecessary greetings or explanations.
                10. Prefer practical and reasoning-based questions over
                    simple definition questions when appropriate.

                INTERVIEW ACTION
                %s

                Return only the question that should be shown
                to the candidate.
                """.formatted(
                member.getName(),
                member.getJobRole(),
                member.getYearsExperience(),
                curriculumDay.getDay(),
                curriculumDay.getTitle(),
                decision.getDifficulty(),
                String.join("\n- ", curriculumDay.getObjectives()),
                conversation,
                previousAnswer,
                decision.getAction()
        );
    }

    private String buildConversationContext(
            InterviewSession session) {

        if (session.getConversationHistory() == null
                || session.getConversationHistory().isEmpty()) {

            return "No previous conversation.";
        }

        return session.getConversationHistory()
                .stream()
                .map(this::formatTurn)
                .collect(Collectors.joining("\n\n"));
    }

    private String formatTurn(ConversationTurn turn) {

        return """
                Question: %s
                Candidate Answer: %s
                Topic: %s
                Difficulty: %s
                """.formatted(
                turn.getQuestion(),
                turn.getAnswer() == null
                        ? "(not answered yet)"
                        : turn.getAnswer(),
                turn.getTopic(),
                turn.getDifficulty()
        );
    }

    private String getPreviousAnswer(
            InterviewSession session) {

        if (session.getConversationHistory() == null
                || session.getConversationHistory().isEmpty()) {

            return "No previous answer.";
        }

        List<ConversationTurn> history =
                session.getConversationHistory();

        ConversationTurn last =
                history.get(history.size() - 1);

        return last.getAnswer() == null
                ? "No previous answer."
                : last.getAnswer();
    }
}