package com.abtalks.interview.planner;

import com.abtalks.interview.domain.*;
import com.abtalks.interview.model.curriculum.CurriculumDay;
import org.springframework.stereotype.Component;

@Component
public class InterviewPlanner {

    private final TopicSelector topicSelector;

    private final CoverageManager coverageManager;

    private final DifficultyManager difficultyManager;

    private final FollowUpDecisionEngine followUpDecisionEngine;

    public InterviewPlanner(
            TopicSelector topicSelector,
            CoverageManager coverageManager,
            DifficultyManager difficultyManager,
            FollowUpDecisionEngine followUpDecisionEngine) {

        this.topicSelector = topicSelector;
        this.coverageManager = coverageManager;
        this.difficultyManager = difficultyManager;
        this.followUpDecisionEngine = followUpDecisionEngine;
    }

    public PlannerDecision planInitialQuestion(
            InterviewSession session) {

        CurriculumDay day =
                topicSelector.selectNextTopic(session);

        session.getProgress()
                .setCurrentDay(day.getDay());

        return PlannerDecision.builder()
                .action(PlannerAction.ASK_NEW_TOPIC)
                .curriculumDay(day.getDay())
                .topic(day.getTitle())
                .difficulty(
                        session.getProgress()
                                .getCurrentDifficulty()
                )
                .reason(
                        "Initial topic selected based on candidate progress"
                )
                .build();
    }

    public PlannerDecision planNextQuestion(
            InterviewSession session,
            Evaluation evaluation) {

        /*
         * First update difficulty based on the
         * latest evaluation.
         */
        Difficulty nextDifficulty =
                difficultyManager.determineNextDifficulty(
                        evaluation,
                        session.getProgress()
                                .getCurrentDifficulty()
                );

        session.getProgress()
                .setCurrentDifficulty(nextDifficulty);

        /*
         * Requirement #1:
         * minimum 8 questions
         * AND
         * minimum 4 curriculum days.
         */
        if (coverageManager.canCompleteInterview(session)) {

            return PlannerDecision.builder()
                    .action(PlannerAction.COMPLETE_INTERVIEW)
                    .reason(
                            "Minimum interview requirements satisfied"
                    )
                    .build();
        }

        /*
         * Requirement #2:
         * We still need curriculum coverage.
         */
        if (!coverageManager.hasMinimumCurriculumCoverage(
                session)) {

            CurriculumDay nextDay =
                    topicSelector.selectNextTopic(session);

            session.getProgress()
                    .setCurrentDay(nextDay.getDay());

            return PlannerDecision.builder()
                    .action(PlannerAction.ASK_NEW_TOPIC)
                    .curriculumDay(nextDay.getDay())
                    .topic(nextDay.getTitle())
                    .difficulty(nextDifficulty)
                    .reason(
                            "Additional curriculum coverage required"
                    )
                    .build();
        }

        /*
         * Requirement #3:
         * Coverage is sufficient, so use
         * evaluation to determine whether
         * deeper probing is useful.
         */
        if (followUpDecisionEngine
                .shouldAskFollowUp(evaluation)) {

            return PlannerDecision.builder()
                    .action(PlannerAction.ASK_FOLLOW_UP)
                    .curriculumDay(
                            session.getProgress()
                                    .getCurrentDay()
                    )
                    .topic(
                            getCurrentTopic(session)
                    )
                    .difficulty(nextDifficulty)
                    .reason(
                            "Candidate answer requires deeper probing"
                    )
                    .build();
        }

        /*
         * Otherwise move to a new topic.
         */
        CurriculumDay nextDay =
                topicSelector.selectNextTopic(session);

        session.getProgress()
                .setCurrentDay(nextDay.getDay());

        return PlannerDecision.builder()
                .action(PlannerAction.ASK_NEW_TOPIC)
                .curriculumDay(nextDay.getDay())
                .topic(nextDay.getTitle())
                .difficulty(nextDifficulty)
                .reason(
                        "Moving to another curriculum topic"
                )
                .build();
    }

    private String getCurrentTopic(
            InterviewSession session) {

        if (session.getConversationHistory() == null
                || session.getConversationHistory().isEmpty()) {

            return "Current topic";
        }

        ConversationTurn lastTurn =
                session.getConversationHistory()
                        .get(session.getConversationHistory().size() - 1);

        return lastTurn.getTopic();
    }
}