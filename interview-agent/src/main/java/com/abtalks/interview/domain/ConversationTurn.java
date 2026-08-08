package com.abtalks.interview.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationTurn {

    private Integer questionNumber;

    private Integer curriculumDay;

    private String topic;

    private Difficulty difficulty;

    private String question;

    private String answer;

}