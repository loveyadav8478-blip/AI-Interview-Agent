package com.abtalks.interview.planner;

import com.abtalks.interview.domain.InterviewSession;
import com.abtalks.interview.domain.TopicProgress;
import com.abtalks.interview.model.profile.Candidate;
import com.abtalks.interview.model.profile.Mission;
import com.abtalks.interview.model.curriculum.CurriculumDay;
import com.abtalks.interview.repository.JsonCurriculumRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class TopicSelector {

    private final JsonCurriculumRepository curriculumRepository;

    private final CoverageManager coverageManager;

    public TopicSelector(
            JsonCurriculumRepository curriculumRepository,
            CoverageManager coverageManager) {

        this.curriculumRepository =
                curriculumRepository;

        this.coverageManager =
                coverageManager;
    }

    public CurriculumDay selectNextTopic(
            InterviewSession session) {

        Candidate candidate =
                session.getCandidate();

        Set<Integer> completedDays =
                getCompletedDays(candidate);

        Set<Integer> skippedDays =
                getSkippedDays(candidate);

        Set<Integer> coveredDays =
                coverageManager.getCoveredDays(session);

        Map<Integer, TopicProgress> progress =
                session.getProgress()
                        .getTopicProgress();

        List<CurriculumDay> eligibleDays =
                curriculumRepository
                        .findAllDays()
                        .stream()
                        .filter(day ->
                                completedDays.contains(
                                        day.getDay()))
                        .filter(day ->
                                !skippedDays.contains(
                                        day.getDay()))
                        .filter(day ->
                                !isMastered(
                                        progress,
                                        day.getDay()))
                        .collect(Collectors.toList());

        /*
         * Until four curriculum days have been covered,
         * prioritize completely new days.
         */
        if (coveredDays.size() < 4) {

            List<CurriculumDay> uncovered =
                    eligibleDays.stream()
                            .filter(day ->
                                    !coveredDays.contains(
                                            day.getDay()))
                            .toList();

            if (!uncovered.isEmpty()) {
                return selectHighestPriority(
                        uncovered,
                        progress
                );
            }
        }

        /*
         * Once minimum coverage is satisfied,
         * select the topic where the candidate
         * needs the most probing.
         */
        if (!eligibleDays.isEmpty()) {

            return selectHighestPriority(
                    eligibleDays,
                    progress
            );
        }

        /*
         * Fallback:
         * If all completed topics have been mastered,
         * choose another curriculum topic.
         */
        return curriculumRepository
                .findAllDays()
                .stream()
                .filter(day ->
                        !skippedDays.contains(
                                day.getDay()))
                .filter(day ->
                        !coveredDays.contains(
                                day.getDay()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No suitable curriculum topic available"
                        ));
    }

    private CurriculumDay selectHighestPriority(
            List<CurriculumDay> days,
            Map<Integer, TopicProgress> progress) {

        return days.stream()
                .min(
                        Comparator.comparingDouble(
                                day -> priorityScore(
                                        day,
                                        progress
                                )
                        )
                )
                .orElseThrow();
    }

    private double priorityScore(
            CurriculumDay day,
            Map<Integer, TopicProgress> progress) {

        TopicProgress topic =
                progress.get(day.getDay());

        if (topic == null) {
            return 0.0;
        }

        /*
         * Lower score = higher priority.
         *
         * Weak topics should be selected first.
         */
        return topic.getAverageScore();
    }

    private boolean isMastered(
            Map<Integer, TopicProgress> progress,
            Integer day) {

        TopicProgress topic =
                progress.get(day);

        return topic != null
                && Boolean.TRUE.equals(
                topic.getMastered());
    }

    private Set<Integer> getCompletedDays(
            Candidate candidate) {

        Set<Integer> days = new HashSet<>();

        if (candidate == null
                || candidate.getMissions() == null) {

            return days;
        }

        for (Mission mission :
                candidate.getMissions()) {

            if (Boolean.TRUE.equals(
                    mission.getPassed())) {

                days.add(mission.getDay());
            }
        }

        return days;
    }

    private Set<Integer> getSkippedDays(
            Candidate candidate) {

        Set<Integer> days = new HashSet<>();

        if (candidate == null
                || candidate.getMissions() == null) {

            return days;
        }

        for (Mission mission :
                candidate.getMissions()) {

            if (Boolean.TRUE.equals(
                    mission.getSkipped())) {

                days.add(mission.getDay());
            }
        }

        return days;
    }
}