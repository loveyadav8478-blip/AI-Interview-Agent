package com.abtalks.interview.domain;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewProgress {

    private Integer questionCount = 0;

    private Integer currentDay;

    private Difficulty currentDifficulty = Difficulty.MEDIUM;

    private Map<Integer, TopicProgress> topicProgress = new HashMap<>();

}