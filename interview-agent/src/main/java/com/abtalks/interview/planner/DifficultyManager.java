package com.abtalks.interview.planner;

import com.abtalks.interview.domain.Difficulty;
import com.abtalks.interview.domain.Evaluation;
import org.springframework.stereotype.Component;

@Component
public class DifficultyManager {

    public Difficulty determineNextDifficulty(
            Evaluation evaluation,
            Difficulty currentDifficulty) {

        if (evaluation == null || evaluation.getScore() == null) {
            return currentDifficulty;
        }

        double score = evaluation.getScore();

        if (score >= 8.0) {
            return increaseDifficulty(currentDifficulty);
        }

        if (score <= 4.0) {
            return decreaseDifficulty(currentDifficulty);
        }

        return currentDifficulty;
    }

    private Difficulty increaseDifficulty(
            Difficulty difficulty) {

        return switch (difficulty) {

            case EASY -> Difficulty.MEDIUM;

            case MEDIUM -> Difficulty.HARD;

            case HARD -> Difficulty.HARD;
        };
    }

    private Difficulty decreaseDifficulty(
            Difficulty difficulty) {

        return switch (difficulty) {

            case HARD -> Difficulty.MEDIUM;

            case MEDIUM -> Difficulty.EASY;

            case EASY -> Difficulty.EASY;
        };
    }
}