package com.abtalks.interview.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewProgress {

    private Integer questionCount;

    private Integer currentDay;

    private Difficulty currentDifficulty;

    private TopicProgress topicProgress;
}