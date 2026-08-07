package com.abtalks.interview.repository;

import com.abtalks.interview.model.profile.Candidate;
import com.abtalks.interview.model.profile.Candidates;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class JsonCandidateRepository implements CandidateRepository{

    private final ObjectMapper objectMapper;

    private Candidates candidates;

    public JsonCandidateRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadCandidates() {

        try (InputStream inputStream =
                     new ClassPathResource("data/candidates.json").getInputStream()) {

            candidates = objectMapper.readValue(inputStream, Candidates.class);

            System.out.println("Candidates Loaded : "
                    + candidates.getCandidates().size());

        } catch (IOException e) {

            throw new RuntimeException("Unable to load candidates.json", e);

        }

    }

    @Override
    public List<Candidate> findAll() {

        if (candidates == null) {
            return Collections.emptyList();
        }

        return candidates.getCandidates();

    }

    @Override
    public Optional<Candidate> findById(String id) {

        return findAll()
                .stream()
                .filter(candidate ->
                        candidate.getMember().getId().equalsIgnoreCase(id))
                .findFirst();

    }

}