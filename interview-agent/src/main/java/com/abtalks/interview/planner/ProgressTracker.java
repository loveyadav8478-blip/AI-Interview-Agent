package com.abtalks.interview.planner;

import com.abtalks.interview.domain.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProgressTracker {

    public void recordEvaluation(
            InterviewSession session,
            ConversationTurn turn,
            Evaluation evaluation) {

        InterviewProgress progress =
                session.getProgress();

        if (progress.getTopicProgress() == null) {
            progress.setTopicProgress(new HashMap<>());
        }

        Map<Integer, TopicProgress> topicProgress =
                progress.getTopicProgress();

        Integer day = turn.getCurriculumDay();

        TopicProgress current =
                topicProgress.computeIfAbsent(
                        day,
                        key -> new TopicProgress(
                                key,
                                0,
                                0.0,
                                0,
                                false
                        )
                );

        int previousQuestions =
                current.getQuestionsAsked();

        double previousAverage =
                current.getAverageScore();

        double newScore =
                evaluation.getScore() == null
                        ? 0.0
                        : evaluation.getScore();

        double newAverage =
                ((previousAverage * previousQuestions)
                        + newScore)
                        / (previousQuestions + 1);

        current.setQuestionsAsked(
                previousQuestions + 1
        );

        current.setAverageScore(newAverage);

        if (Boolean.TRUE.equals(
                evaluation.getFollowUpNeeded())) {

            current.setFollowUpsAsked(
                    current.getFollowUpsAsked() + 1
            );
        }

        current.setMastered(
                current.getAverageScore() >= 8.0
                        && current.getQuestionsAsked() >= 1
                        && !Boolean.TRUE.equals(
                        evaluation.getFollowUpNeeded())
        );
    }
}