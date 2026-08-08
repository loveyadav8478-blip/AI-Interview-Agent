package com.abtalks.interview.planner;

import com.abtalks.interview.domain.InterviewSession;
import com.abtalks.interview.model.profile.Candidate;
import com.abtalks.interview.model.profile.Mission;
import com.abtalks.interview.model.curriculum.CurriculumDay;
import com.abtalks.interview.repository.JsonCurriculumRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TopicSelector {

    private final JsonCurriculumRepository curriculumRepository;

    public TopicSelector(JsonCurriculumRepository curriculumRepository) {
        this.curriculumRepository = curriculumRepository;
    }

    public CurriculumDay selectInitialTopic(InterviewSession session) {

        Candidate candidate = session.getCandidate();

        Set<Integer> completedDays = getCompletedDays(candidate);

        Set<Integer> skippedDays = getSkippedDays(candidate);

        return curriculumRepository.findAllDays()
                .stream()
                .filter(day -> completedDays.contains(day.getDay()))
                .filter(day -> !skippedDays.contains(day.getDay()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No suitable curriculum topic found for candidate"
                        ));
    }

    private Set<Integer> getCompletedDays(Candidate candidate) {

        Set<Integer> completedDays = new HashSet<>();

        if (candidate == null || candidate.getMissions() == null) {
            return completedDays;
        }

        for (Mission mission : candidate.getMissions()) {

            if (Boolean.TRUE.equals(mission.getPassed())) {
                completedDays.add(mission.getDay());
            }
        }

        return completedDays;
    }

    private Set<Integer> getSkippedDays(Candidate candidate) {

        Set<Integer> skippedDays = new HashSet<>();

        if (candidate == null || candidate.getMissions() == null) {
            return skippedDays;
        }

        for (Mission mission : candidate.getMissions()) {

            if (Boolean.TRUE.equals(mission.getSkipped())) {
                skippedDays.add(mission.getDay());
            }
        }

        return skippedDays;
    }
}