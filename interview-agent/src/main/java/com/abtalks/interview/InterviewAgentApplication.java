package com.abtalks.interview;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InterviewAgentApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterviewAgentApplication.class, args);
	}


    @Bean
    public ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }
}
