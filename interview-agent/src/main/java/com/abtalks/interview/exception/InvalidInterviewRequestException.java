package com.abtalks.interview.exception;

public class InvalidInterviewRequestException
        extends RuntimeException {

    public InvalidInterviewRequestException(
            String message) {

        super(message);
    }
}