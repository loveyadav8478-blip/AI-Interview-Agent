package com.abtalks.interview.model.curriculum;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurriculumDay {

    private Integer day;

    private String title;

    private String type;

    private List<String> tools;

    private List<String> objectives;

}