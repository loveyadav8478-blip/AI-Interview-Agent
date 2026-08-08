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

    @Override
    public String generateJson(String prompt) {

        if (prompt.contains("[OUTPUT_TYPE=FEEDBACK]")) {

            return """
            {
              "summary": "The candidate demonstrated a solid foundational understanding of the covered AI engineering topics, with stronger performance on conceptual questions than on detailed system design.",
              "strengths": [
                "Good understanding of semantic search and vector databases.",
                "Able to explain practical differences between local and managed vector database solutions."
              ],
              "gaps": [
                "Needs more depth in query routing architecture.",
                "Could explain implementation-level trade-offs more clearly."
              ],
              "next": [
                "Practice designing hybrid SQL and vector retrieval systems.",
                "Implement a query-routing prototype.",
                "Study retrieval architecture trade-offs and evaluation."
              ]
            }
            """;
        }

        return """
            {
              "score": 7.0,
              "strengths": [
                "Candidate demonstrates a basic understanding of the concept."
              ],
              "weaknesses": [
                "Candidate could provide more implementation-level detail."
              ],
              "reasoning": "The candidate demonstrates reasonable conceptual understanding but lacks deeper technical explanation.",
              "followUpNeeded": true
            }
            """;
    }
}