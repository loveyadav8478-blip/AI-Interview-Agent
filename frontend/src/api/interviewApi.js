import axios from "axios";

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8080",
    headers: {
        "Content-Type": "application/json",
    },
});

export async function startInterview(candidateId) {
    const response = await api.post("/api/interview", {
        sessionId: null,
        candidate: candidateId,
        message: null,
    });

    return response.data;
}

export async function submitAnswer(sessionId, message) {
    const response = await api.post("/api/interview", {
        sessionId,
        candidate: null,
        message,
    });

    return response.data;
}

export default api;