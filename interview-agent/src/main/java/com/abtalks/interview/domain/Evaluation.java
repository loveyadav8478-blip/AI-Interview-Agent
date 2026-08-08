package com.abtalks.interview.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {
    private String reasoning;

    private Integer questionNumber;

    private Double score;

    private List<String> strengths;

    private List<String> weaknesses;

    private Boolean followUpNeeded;

}