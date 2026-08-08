package com.abtalks.interview.planner;

import com.abtalks.interview.domain.InterviewSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CoverageManager {

    private final int MIN_QUESTIONS;
    private final int MIN_CURRICULUM_DAYS;

    public CoverageManager(
            @Value("${interview.minimum-questions}") int MIN_QUESTIONS,
            @Value("${interview.minimum-curriculum-days}") int MIN_CURRICULUM_DAYS) {

        this.MIN_CURRICULUM_DAYS = MIN_CURRICULUM_DAYS;
        this.MIN_QUESTIONS = MIN_QUESTIONS;
    }

    public boolean hasMinimumQuestions(
            InterviewSession session) {

        return session.getProgress()
                .getQuestionCount() >= MIN_QUESTIONS;
    }

    public boolean hasMinimumCurriculumCoverage(
            InterviewSession session) {

        return getCoveredDays(session).size()
                >= MIN_CURRICULUM_DAYS;
    }

    public boolean canCompleteInterview(
            InterviewSession session) {

        return hasMinimumQuestions(session)
                && hasMinimumCurriculumCoverage(session);
    }

    public Set<Integer> getCoveredDays(
            InterviewSession session) {

        Set<Integer> days = new HashSet<>();

        if (session.getConversationHistory() == null) {
            return days;
        }

        session.getConversationHistory()
                .forEach(turn -> {

                    if (turn.getCurriculumDay() != null) {
                        days.add(turn.getCurriculumDay());
                    }
                });

        return days;
    }

    public int getMinimumQuestions() {
        return MIN_QUESTIONS;
    }

    public int getMinimumCurriculumDays() {
        return MIN_CURRICULUM_DAYS;
    }
}