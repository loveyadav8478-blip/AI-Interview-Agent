package com.abtalks.interview.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Primary
@Profile("gemini")
public class GeminiLLMService implements LLMService {

    private final ChatModel chatModel;

    public GeminiLLMService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public String generate(String prompt) {

        Prompt request = new Prompt(
                prompt,
                GoogleGenAiChatOptions.builder()
                        .model("gemini-3.5-flash")
                        .temperature(0.3)
                        .build()
        );

        return chatModel
                .call(request)
                .getResult()
                .getOutput()
                .getText();
    }

    @Override
    public String generateJson(String prompt) {

        Prompt request = new Prompt(
                prompt,
                GoogleGenAiChatOptions.builder()
                        .model("gemini-3.5-flash")
                        .temperature(0.1)
                        .responseMimeType("application/json")
                        .build()
        );

        return chatModel
                .call(request)
                .getResult()
                .getOutput()
                .getText();
    }
}