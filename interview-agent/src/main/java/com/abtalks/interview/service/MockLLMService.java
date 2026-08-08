package com.abtalks.interview.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("mock")
public class MockLLMService implements LLMService {

    @Override
    public String generate(String prompt) {

        return "Explain the core concept behind this topic and "
                + "describe how you would apply it in a real system.";
    }
}