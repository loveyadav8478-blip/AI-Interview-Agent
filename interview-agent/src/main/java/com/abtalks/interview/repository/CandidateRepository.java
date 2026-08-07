package com.abtalks.interview.repository;

import com.abtalks.interview.model.profile.Candidate;
import java.util.*;

public interface CandidateRepository {
    List<Candidate> findAll();

    Optional<Candidate> findById(String id);
}
