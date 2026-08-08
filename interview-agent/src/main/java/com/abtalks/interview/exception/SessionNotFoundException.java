package com.abtalks.interview.exception;

public class SessionNotFoundException
        extends RuntimeException {

    public SessionNotFoundException(
            String message) {

        super(message);
    }
}