package com.abtalks.interview.prompt;

import com.abtalks.interview.domain.Difficulty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewPromptContext {

    private String candidateName;

    private String jobRole;

    private Integer yearsExperience;

    private String topic;

    private Integer curriculumDay;

    private String objectives;

    private Difficulty difficulty;

    private String previousConversation;

    private String previousAnswer;

}