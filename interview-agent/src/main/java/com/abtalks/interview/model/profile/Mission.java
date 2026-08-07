package com.abtalks.interview.model.profile;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mission {

    private Integer day;

    private String title;

    private Boolean passed;

    private Boolean skipped;

    private Integer attempts;

}