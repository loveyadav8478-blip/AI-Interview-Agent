package com.abtalks.interview.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlannerDecision {

    private PlannerAction action;

    private Integer curriculumDay;

    private String topic;

    private Difficulty difficulty;

    private String reason;

}