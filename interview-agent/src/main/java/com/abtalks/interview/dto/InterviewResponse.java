package com.abtalks.interview.dto;

import com.abtalks.interview.domain.Feedback;
import com.abtalks.interview.domain.InterviewProgressResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewResponse {

    private String sessionId;

    private String reply;

    private boolean done;

    private Feedback feedback;

    private InterviewProgressResponse progress;
}