package com.abtalks.interview.repository;

import com.abtalks.interview.model.curriculum.CurriculumDay;
import java.util.*;

public interface CurriculumRepository {
    List<CurriculumDay> findAllDays();

    Optional<CurriculumDay> findDay(Integer day);

}
