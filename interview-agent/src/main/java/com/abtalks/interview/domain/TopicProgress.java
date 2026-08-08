package com.abtalks.interview.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopicProgress {

    private Integer day;

    private Integer questionsAsked = 0;

    private Double averageScore = 0.0;

    private Integer followUpsAsked = 0;

    private Boolean mastered = false;

}