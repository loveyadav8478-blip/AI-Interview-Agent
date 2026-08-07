package com.abtalks.interview.model.curriculum;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Curriculum {

    private String cohort;

    private List<Module> modules;

    private List<CurriculumDay> days;

}