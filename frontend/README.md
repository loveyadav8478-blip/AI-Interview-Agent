# AI Interview Agent — Frontend

React + Vite + Tailwind CSS frontend for the **AI Interview Agent** built for the ABTalks interview-agent hackathon.

The frontend provides the complete candidate-to-interview-to-feedback experience and communicates with the Spring Boot backend through the `/api/interview` endpoint.

---

## Overview

The application follows a simple three-stage interview flow:

```text
Candidate Selection
        │
        ▼
Start Interview
        │
        ▼
Live AI Interview
        │
        ▼
Submit Answers
        │
        ▼
Interview Completion
        │
        ▼
Final Feedback Report
