package com.abtalks.interview.controller;

import com.abtalks.interview.dto.InterviewRequest;
import com.abtalks.interview.dto.InterviewResponse;
import com.abtalks.interview.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interview")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping
    public ResponseEntity<InterviewResponse> interview(
            @RequestBody InterviewRequest request) {

        return ResponseEntity.ok(
                interviewService.handleRequest(request)
        );
    }
}