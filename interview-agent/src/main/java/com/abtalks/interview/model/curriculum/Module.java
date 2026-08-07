package com.abtalks.interview.model.curriculum;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Module {

    private Integer n;

    private String title;

    private List<Integer> days;

}