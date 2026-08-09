import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

/**
 * Starts a new interview for the given candidate id (e.g. "CAND-001").
 * The backend creates the session and returns the first question.
 */
export async function startInterview(candidateId) {
  const response = await api.post("/api/interview", {
    sessionId: null,
    candidate: candidateId,
    message: null,
  });

  return response.data;
}

/**
 * Sends the candidate's current answer for an in-progress session.
 * Only the sessionId + latest message are sent — the backend owns history.
 */
export async function submitAnswer(sessionId, message) {
  const response = await api.post("/api/interview", {
    sessionId,
    candidate: null,
    message,
  });

  return response.data;
}

export default api;
