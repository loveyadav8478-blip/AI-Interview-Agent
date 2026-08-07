package com.abtalks.interview.repository;

import com.abtalks.interview.model.curriculum.Curriculum;
import com.abtalks.interview.model.curriculum.CurriculumDay;
import com.abtalks.interview.model.curriculum.Module;
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
public class JsonCurriculumRepository implements CurriculumRepository {

    private final ObjectMapper objectMapper;

    private Curriculum curriculum;

    public JsonCurriculumRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadCurriculum() {

        try (InputStream inputStream =
                     new ClassPathResource("data/curriculum.json").getInputStream()) {

            curriculum = objectMapper.readValue(inputStream, Curriculum.class);

            System.out.println("Curriculum Loaded : "
                    + curriculum.getDays().size() + " Days");

        } catch (IOException e) {

            throw new RuntimeException("Unable to load curriculum.json", e);

        }

    }

    public Curriculum getCurriculum() {
        return curriculum;
    }

    @Override
    public List<CurriculumDay> findAllDays() {

        if (curriculum == null) {
            return Collections.emptyList();
        }

        return curriculum.getDays();

    }

    public List<Module> findAllModules() {

        if (curriculum == null) {
            return Collections.emptyList();
        }

        return curriculum.getModules();

    }

    @Override
    public Optional<CurriculumDay> findDay(Integer day) {

        return findAllDays()
                .stream()
                .filter(d -> d.getDay().equals(day))
                .findFirst();

    }

    public Optional<Module> findModule(Integer moduleNumber) {

        return findAllModules()
                .stream()
                .filter(module -> module.getN().equals(moduleNumber))
                .findFirst();

    }

}