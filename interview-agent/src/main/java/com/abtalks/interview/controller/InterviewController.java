package com.abtalks.interview.controller;

import com.abtalks.interview.dto.InterviewRequest;
import com.abtalks.interview.dto.InterviewResponse;
import com.abtalks.interview.exception.InterviewCompleteException;
import com.abtalks.interview.service.InterviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
    public ResponseEntity<?> interview(
            @RequestBody InterviewRequest request) {

        try {
            return ResponseEntity.ok(
                    interviewService.handleRequest(request)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}