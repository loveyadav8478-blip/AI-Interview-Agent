package com.abtalks.interview.model.profile;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    private String id;

    private String name;

    private String jobRole;

    private Integer yearsExperience;

    private String education;

    private String status;

}