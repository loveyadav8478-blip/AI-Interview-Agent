package com.abtalks.interview.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewRequest {

    private String sessionId;

    private String candidate;

    private String message;
}