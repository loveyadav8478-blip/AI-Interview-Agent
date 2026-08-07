package com.abtalks.interview.model.profile;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {

    private Member member;

    private List<Mission> missions;

    private LearningSignal signals;

}