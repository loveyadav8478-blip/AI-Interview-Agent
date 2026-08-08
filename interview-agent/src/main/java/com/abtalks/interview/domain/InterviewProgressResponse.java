package com.abtalks.interview.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewProgressResponse {

    private Integer questionsAsked;

    private Integer minimumQuestions;

    private Integer curriculumDaysCovered;

    private Integer minimumCurriculumDays;

    private Integer currentDay;

    private Difficulty currentDifficulty;
}