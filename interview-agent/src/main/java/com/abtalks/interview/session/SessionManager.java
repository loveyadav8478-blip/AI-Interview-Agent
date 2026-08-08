package com.abtalks.interview.session;

import com.abtalks.interview.domain.InterviewSession;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    private final ConcurrentHashMap<String, InterviewSession> sessions =
            new ConcurrentHashMap<>();

    public void createSession(InterviewSession session){

        sessions.put(session.getSessionId(), session);

    }

    public Optional<InterviewSession> getSession(String sessionId){

        return Optional.ofNullable(
                sessions.get(sessionId)
        );

    }

    public void updateSession(InterviewSession session){

        sessions.put(
                session.getSessionId(),
                session
        );

    }

    public void removeSession(String sessionId){

        sessions.remove(sessionId);

    }

}