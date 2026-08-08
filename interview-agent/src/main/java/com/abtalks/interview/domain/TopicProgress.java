package com.abtalks.interview.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopicProgress {

    private Integer day;

    private Integer questionsAsked;

    private Double averageScore;

    private Boolean mastered;

}