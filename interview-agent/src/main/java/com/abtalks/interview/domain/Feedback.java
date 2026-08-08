package com.abtalks.interview.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    private String summary;

    private List<String> strengths;

    private List<String> gaps;

    private List<String> next;

}