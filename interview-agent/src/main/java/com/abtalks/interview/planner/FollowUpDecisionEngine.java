package com.abtalks.interview.planner;

import com.abtalks.interview.domain.Evaluation;
import org.springframework.stereotype.Component;

@Component
public class FollowUpDecisionEngine {

    public boolean shouldAskFollowUp(
            Evaluation evaluation) {

        if (evaluation == null) {
            return false;
        }

        if (Boolean.TRUE.equals(evaluation.getFollowUpNeeded())) {
            return true;
        }

        return evaluation.getScore() != null
                && evaluation.getScore() < 6.0;
    }
}