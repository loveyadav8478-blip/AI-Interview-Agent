import { useState } from "react";
import { startInterview, submitAnswer } from "./api/interviewApi.js";
import { getCandidateById } from "./data/candidates.js";
import Landing from "./screens/Landing.jsx";
import Interview from "./screens/Interview.jsx";
import Result from "./screens/Result.jsx";

const STAGE = {
  LANDING: "landing",
  INTERVIEW: "interview",
  RESULT: "result",
};

function friendlyError(err, fallback) {
  if (!err?.response) {
    return "Unable to connect to the interview server. Please try again.";
  }
  if (err.response.status === 404 || err.response.status === 400) {
    return "This interview session is no longer available. Please start a new interview.";
  }
  return fallback;
}

export default function App() {
  const [stage, setStage] = useState(STAGE.LANDING);
  const [candidate, setCandidate] = useState(null);
  const [sessionId, setSessionId] = useState(null);
  const [messages, setMessages] = useState([]);
  const [feedback, setFeedback] = useState(null);

  const [starting, setStarting] = useState(false);
  const [startError, setStartError] = useState(null);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleBegin = async (candidateId) => {
    setStarting(true);
    setStartError(null);

    try {
      const data = await startInterview(candidateId);
      setCandidate(getCandidateById(candidateId));
      setSessionId(data.sessionId);
      setMessages([{ role: "assistant", content: data.reply }]);
      setStage(STAGE.INTERVIEW);
    } catch (err) {
      setStartError(friendlyError(err, "Unable to start the interview. Please try again."));
    } finally {
      setStarting(false);
    }
  };

  const handleSubmit = async (answer) => {
    if (!answer.trim()) {
      setError("Please provide an answer before submitting.");
      return;
    }

    setMessages((prev) => [...prev, { role: "user", content: answer }]);
    setLoading(true);
    setError(null);

    try {
      const data = await submitAnswer(sessionId, answer);

      if (data.reply) {
        setMessages((prev) => [...prev, { role: "assistant", content: data.reply }]);
      }

      if (data.done) {
        setFeedback(data.feedback);
        setTimeout(() => setStage(STAGE.RESULT), 500);
      }
    } catch (err) {
      setError(friendlyError(err, "Unable to submit your answer. Please try again."));
    } finally {
      setLoading(false);
    }
  };

  const handleRestart = () => {
    setStage(STAGE.LANDING);
    setCandidate(null);
    setSessionId(null);
    setMessages([]);
    setFeedback(null);
    setStartError(null);
    setError(null);
  };

  if (stage === STAGE.RESULT) {
    return <Result candidate={candidate} feedback={feedback} onRestart={handleRestart} />;
  }

  if (stage === STAGE.INTERVIEW) {
    return (
      <Interview
        candidate={candidate}
        sessionId={sessionId}
        messages={messages}
        onSubmit={handleSubmit}
        loading={loading}
        error={error}
        onDismissError={() => setError(null)}
      />
    );
  }

  return (
    <Landing onBegin={handleBegin} starting={starting} startError={startError} />
  );
}
