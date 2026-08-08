package com.abtalks.interview.service;

public interface LLMService {

    String generate(String prompt);

    String generateJson(String prompt);

}