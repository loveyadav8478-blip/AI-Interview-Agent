package com.abtalks.interview.domain;

import com.abtalks.interview.model.profile.Candidate;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSession {

    private String sessionId;

    private Candidate candidate;

    private InterviewProgress progress;

    private List<ConversationTurn> conversationHistory;

    private List<Evaluation> evaluations;

    private Feedback feedback;

}