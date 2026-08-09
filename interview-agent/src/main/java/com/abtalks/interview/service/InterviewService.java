package com.abtalks.interview.service;

import com.abtalks.interview.domain.*;
import com.abtalks.interview.dto.InterviewRequest;
import com.abtalks.interview.dto.InterviewResponse;
import com.abtalks.interview.exception.CandidateNotFoundException;
import com.abtalks.interview.exception.InterviewCompleteException;
import com.abtalks.interview.exception.SessionNotFoundException;
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

    private final FeedbackService feedbackService;

    private final JsonCandidateRepository candidateRepository;

    private final ProgressTracker progressTracker;

    private final AnswerEvaluationService answerEvaluationService;

    private final SessionManager sessionManager;

    private final InterviewPlanner interviewPlanner;

    private final QuestionGenerationService questionGenerationService;

    public InterviewService(
            JsonCandidateRepository candidateRepository,
            SessionManager sessionManager,
            InterviewPlanner interviewPlanner,
            QuestionGenerationService questionGenerationService,
            AnswerEvaluationService answerEvaluationService,
            ProgressTracker progressTracker,
            FeedbackService feedbackService) {

        this.candidateRepository = candidateRepository;
        this.sessionManager = sessionManager;
        this.interviewPlanner = interviewPlanner;
        this.questionGenerationService = questionGenerationService;
        this.answerEvaluationService = answerEvaluationService;
        this.progressTracker = progressTracker;
        this.feedbackService = feedbackService;
    }

//    public InterviewResponse handleRequest(
//            InterviewRequest request) {
//        Candidate candidate = candidateRepository
//                .findById(request.getCandidate())
//                .orElseThrow(() ->
//                        new CandidateNotFoundException(
//                                "Candidate not found: " + request.getCandidate()
//                        )
//                );
//
//        if (request.getSessionId() == null
//                || request.getSessionId().isBlank()) {
//
//            return startInterview(request);
//        }
//
//        return continueInterview(request);
//    }
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

//        session.setStatus(InterviewStatus.ONGOING);
//
//        sessionManager.updateSession(session);
        session.setStatus(InterviewStatus.ONGOING);

        sessionManager.updateSession(session);

        System.out.println(
                "========== SESSION CREATED =========="
        );

        System.out.println(
                "Session ID : " + session.getSessionId()
        );

        System.out.println(
                "Status     : " + session.getStatus()
        );

        System.out.println(
                "Questions  : " +
                        session.getProgress().getQuestionCount()
        );

        System.out.println(
                "======================================"
        );

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
                                new SessionNotFoundException("Interview session not found: " +
                                        request.getSessionId()));
        System.out.println(
                "========== SESSION RETRIEVED =========="
        );

        System.out.println(
                "Session ID : " + session.getSessionId()
        );

        System.out.println(
                "Status     : " + session.getStatus()
        );

        System.out.println(
                "Questions  : " +
                        session.getProgress().getQuestionCount()
        );

        System.out.println(
                "========================================"
        );

//        if (session.getStatus()
//                == InterviewStatus.COMPLETED) {
//
//            throw new InterviewCompleteException(
//                    "Interview is already completed"
//            );
//        }

        if (session.getStatus() == InterviewStatus.COMPLETED) {

            System.out.println(
                    "!!! SESSION IS ALREADY COMPLETED !!!"
            );

            throw new InterviewCompleteException(
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

            Feedback feedback =
                    feedbackService.generateFeedback(
                            session
                    );

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
                    .feedback(feedback)
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