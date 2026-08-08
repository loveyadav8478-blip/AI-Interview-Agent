package com.abtalks.interview.service;

import com.abtalks.interview.domain.*;
import com.abtalks.interview.dto.InterviewRequest;
import com.abtalks.interview.dto.InterviewResponse;
import com.abtalks.interview.model.profile.Candidate;
import com.abtalks.interview.planner.InterviewPlanner;
import com.abtalks.interview.planner.ProgressTracker;
import com.abtalks.interview.repository.JsonCandidateRepository;
import com.abtalks.interview.session.SessionManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class InterviewService {

    private final JsonCandidateRepository candidateRepository;

    private final ProgressTracker progressTracker;

    private final AnswerEvaluationService answerEvaluationService;

    private final SessionManager sessionManager;

    private final InterviewPlanner interviewPlanner;

    private final QuestionGenerationService questionGenerationService;

    public InterviewService(
            JsonCandidateRepository candidateRepository, ProgressTracker progressTracker,
            SessionManager sessionManager,
            InterviewPlanner interviewPlanner,
            QuestionGenerationService questionGenerationService,
            AnswerEvaluationService answerEvaluationService) {

        this.candidateRepository = candidateRepository;
        this.progressTracker = progressTracker;
        this.sessionManager = sessionManager;
        this.interviewPlanner = interviewPlanner;
        this.questionGenerationService =
                questionGenerationService;
        this.answerEvaluationService =
                answerEvaluationService;
    }

    public InterviewResponse handleRequest(
            InterviewRequest request) {

        if (request.getSessionId() == null
                || request.getSessionId().isBlank()) {

            return startInterview(request);
        }

        return continueInterview(request);
    }

    private InterviewResponse startInterview(InterviewRequest request) {

        Candidate candidate = candidateRepository.findById(request.getCandidate())
                                .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Candidate not found: "
                                                + request.getCandidate()
                                ));

        String sessionId = UUID.randomUUID().toString();

        InterviewProgress progress = new InterviewProgress();

        InterviewSession session = new InterviewSession();

        session.setSessionId(sessionId);
        session.setCandidate(candidate);
        session.setStatus(InterviewStatus.STARTED);
        session.setConversationHistory(
                new ArrayList<>()
        );
        session.setEvaluations(
                new ArrayList<>()
        );
        session.setProgress(progress);

        sessionManager.createSession(session);

        PlannerDecision decision = interviewPlanner.planInitialQuestion(session);

        String question = questionGenerationService.generateQuestion(session, decision);

        addQuestionToHistory(
                session,
                decision,
                question
        );

        session.getProgress()
                .setQuestionCount(1);

        session.setStatus(InterviewStatus.ONGOING);

        sessionManager.updateSession(session);

        return InterviewResponse.builder()
                .sessionId(sessionId)
                .reply(question)
                .done(false)
                .build();
    }

    private InterviewResponse continueInterview(
            InterviewRequest request) {

        InterviewSession session =
                sessionManager
                        .getSession(request.getSessionId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Interview session not found: "
                                                + request.getSessionId()
                                ));

        if (session.getStatus()
                == InterviewStatus.COMPLETED) {

            throw new IllegalStateException(
                    "Interview is already completed"
            );
        }

        ConversationTurn currentTurn =
                getCurrentTurn(session);

        currentTurn.setAnswer(request.getMessage());

        Evaluation evaluation =
                answerEvaluationService.evaluate(currentTurn);

        session.getEvaluations()
                .add(evaluation);

        PlannerDecision decision =
                interviewPlanner.planNextQuestion(
                        session,
                        evaluation
                );

        if (decision.getAction()
                == PlannerAction.COMPLETE_INTERVIEW) {

            session.setStatus(
                    InterviewStatus.COMPLETED
            );

            sessionManager.updateSession(session);

            return InterviewResponse.builder()
                    .sessionId(session.getSessionId())
                    .reply(
                            "Thank you. The interview is complete."
                    )
                    .done(true)
                    .feedback(null)
                    .build();
        }

        String nextQuestion =
                questionGenerationService.generateQuestion(
                        session,
                        decision
                );

        addQuestionToHistory(
                session,
                decision,
                nextQuestion
        );

        session.getProgress()
                .setQuestionCount(
                        session.getProgress()
                                .getQuestionCount() + 1
                );

        sessionManager.updateSession(session);

        return InterviewResponse.builder()
                .sessionId(session.getSessionId())
                .reply(nextQuestion)
                .done(false)
                .build();
    }

    private ConversationTurn getCurrentTurn(
            InterviewSession session) {

        if (session.getConversationHistory() == null
                || session.getConversationHistory().isEmpty()) {

            throw new IllegalStateException(
                    "No interview question exists"
            );
        }

        return session.getConversationHistory()
                .get(
                        session.getConversationHistory().size() - 1
                );
    }

    private void addQuestionToHistory(
            InterviewSession session,
            PlannerDecision decision,
            String question) {

        ConversationTurn turn =
                new ConversationTurn();

        turn.setQuestionNumber(
                session.getProgress()
                        .getQuestionCount() + 1
        );

        turn.setCurriculumDay(
                decision.getCurriculumDay()
        );

        turn.setTopic(
                decision.getTopic()
        );

        turn.setDifficulty(
                decision.getDifficulty()
        );

        turn.setQuestion(question);

        turn.setAnswer(null);

        session.getConversationHistory()
                .add(turn);
    }

//    private Evaluation evaluateAnswer(
//            ConversationTurn turn) {
//
//        /*
//         * Temporary implementation.
//         *
//         * We will replace this with the real
//         * EvaluationService + LLM structured output.
//         */
//
//        Evaluation evaluation =
//                new Evaluation();
//
//        evaluation.setQuestionNumber(
//                turn.getQuestionNumber()
//        );
//
//        evaluation.setScore(7.0);
//
//        evaluation.setStrengths(
//                java.util.List.of(
//                        "Candidate provided a relevant response"
//                )
//        );
//
//        evaluation.setWeaknesses(
//                java.util.List.of(
//                        "Further technical depth can be explored"
//                )
//        );
//
//        evaluation.setReasoning(
//                "Temporary mock evaluation"
//        );
//
//        evaluation.setFollowUpNeeded(true);
//
//        return evaluation;
//    }
}