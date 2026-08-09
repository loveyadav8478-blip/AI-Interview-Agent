# AI Interview Agent

> **An adaptive, AI-powered technical interview platform that conducts structured interviews, evaluates candidate responses, tracks curriculum coverage, and generates actionable feedback.**

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Backend-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-Frontend-61DAFB?logo=react)](https://react.dev/)
[![Vite](https://img.shields.io/badge/Vite-Build%20Tool-646CFF?logo=vite)](https://vitejs.dev/)
[![Gemini](https://img.shields.io/badge/Gemini-AI%20Engine-4285F4?logo=google)](https://ai.google.dev/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind-CSS-06B6D4?logo=tailwindcss)](https://tailwindcss.com/)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?logo=apachemaven)](https://maven.apache.org/)

---

## Table of Contents

- [Project Overview](#-project-overview)
- [Why This Project](#-why-this-project)
- [Key Highlights](#-key-highlights)
- [How the AI Interview Works](#-how-the-ai-interview-works)
- [System Architecture](#-system-architecture)
- [End-to-End Data Flow](#-end-to-end-data-flow)
- [Technology Stack](#-technology-stack)
- [Repository Structure](#-repository-structure)
- [Frontend](#-frontend)
- [Backend](#-backend)
- [AI Intelligence Layer](#-ai-intelligence-layer)
- [Interview Lifecycle](#-interview-lifecycle)
- [API Documentation](#-api-documentation)
- [Candidate & Curriculum Data](#-candidate--curriculum-data)
- [Error Handling](#-error-handling)
- [Security](#-security)
- [Local Setup](#-local-setup)
- [Running the Project](#-running-the-project)
- [Production Build](#-production-build)
- [Deployment Architecture](#-deployment-architecture)
- [Design Decisions](#-design-decisions)
- [Current Limitations](#-current-limitations)
- [Future Improvements](#-future-improvements)
- [Judge-Friendly Project Walkthrough](#-judge-friendly-project-walkthrough)
- [Conclusion](#-conclusion)

---

# Project Overview

**AI Interview Agent** is an adaptive technical interview platform designed to simulate a structured interview rather than simply presenting a fixed list of questions.

A candidate is selected, an interview session is created, and the backend dynamically manages the interview using:

- Candidate profile
- Curriculum coverage
- Previous questions
- Previous answers
- Answer evaluations
- Topics already covered
- Difficulty
- Interview progress
- Completion requirements

The system uses **Gemini** as the AI intelligence layer for question generation, answer evaluation, interview planning, and final feedback generation.

The architecture intentionally separates responsibilities:

```text
┌─────────────────────────────────────┐
│            React Frontend           │
│                                     │
│  Candidate Selection                │
│  Live Interview                     │
│  Progress / Status                  │
│  Final Feedback                     │
└──────────────────┬──────────────────┘
                   │ REST / JSON
                   ▼
┌─────────────────────────────────────┐
│         Spring Boot Backend         │
│                                     │
│  Session Management                 │
│  Interview Planning                 │
│  Progress Tracking                  │
│  Question Generation                │
│  Answer Evaluation                  │
│  Completion                         │
│  Feedback Generation                │
└──────────────────┬──────────────────┘
                   │ AI Requests
                   ▼
┌─────────────────────────────────────┐
│               Gemini                │
│                                     │
│  Question Generation                │
│  Answer Evaluation                  │
│  Planning                           │
│  Feedback Generation                │
└─────────────────────────────────────┘
```

The **backend remains the source of truth** for interview state and intelligence, while the frontend provides the user-facing experience.

---

# Why This Project?

Traditional interview systems often follow a static sequence:

```text
Question 1
   ↓
Question 2
   ↓
Question 3
   ↓
...
```

That approach does not adapt to the candidate.

This project instead follows:

```text
Candidate
   ↓
Interview Context
   ↓
AI Planner
   ↓
Question
   ↓
Candidate Answer
   ↓
AI Evaluation
   ↓
Updated Interview Context
   ↓
AI Planner
   ↓
Next Question
   ↓
...
```

This makes the interview **session-aware and adaptive**.

The goal is not simply to ask AI-generated questions. The goal is to build an **interview orchestration system** where AI decisions are controlled by deterministic application state, curriculum requirements, and interview rules.

---

# Key Highlights

## 1. Adaptive Interviews

The next question is not selected by the frontend from a hardcoded list.

The backend considers the current interview state and planner decision before generating the next question.

## 2. AI-Powered Evaluation

Candidate answers are evaluated using Gemini.

The evaluation becomes part of the interview session and can influence future question selection.

## 3. Curriculum-Aware Interviewing

The system tracks curriculum days and topics so the interview can achieve meaningful coverage rather than repeatedly asking questions from the same area.

The current project includes:

- **20 synthetic candidates**
- **31 curriculum days**
- A configured minimum interview coverage of at least **8 questions across at least 4 curriculum days**

## 4. Structured Interview State

Each interview has a unique `sessionId`.

The backend maintains:

- Candidate
- Interview status
- Progress
- Conversation history
- Evaluations
- Feedback

## 5. Centralized Completion Logic

The frontend does not decide when an interview is complete.

The backend planner determines whether to:

```text
ASK_QUESTION
```

or:

```text
COMPLETE_INTERVIEW
```

## 6. Actionable Final Feedback

At completion, the system produces:

- Overall score
- Questions asked
- Curriculum days covered
- Overall assessment
- Strengths
- Gaps
- Recommended next steps

## 7. Clean Frontend / Backend Separation

The React application is intentionally lightweight from a business-logic perspective.

The backend owns:

```text
Interview Intelligence
Session State
Planning
Evaluation
Completion
Feedback
```

The frontend owns:

```text
Presentation
User Interaction
API Communication
Loading / Error UI
Feedback Visualization
```

---

# How the AI Interview Works

A complete interview can be represented as:

```text
1. Select Candidate
        │
        ▼
2. Create Interview Session
        │
        ▼
3. Analyze Interview Context
        │
        ▼
4. Planner Chooses Topic + Difficulty
        │
        ▼
5. Gemini Generates Question
        │
        ▼
6. Candidate Answers
        │
        ▼
7. Gemini Evaluates Answer
        │
        ▼
8. Progress / Coverage Updated
        │
        ▼
9. Planner Decides Next Action
        │
        ├───────────────┐
        │               │
        ▼               ▼
   ASK QUESTION   COMPLETE INTERVIEW
        │               │
        ▼               ▼
 Generate Next      Generate Feedback
 Question               │
        │               ▼
        │          Final Result
        │
        └─────── Repeat ───────┘
```

---

# System Architecture

```text
                         ┌──────────────────┐
                         │      Browser     │
                         │   React + Vite   │
                         └────────┬─────────┘
                                  │
                                  │ HTTP / JSON
                                  ▼
                         ┌──────────────────┐
                         │ Spring Boot API  │
                         │                  │
                         │ Interview        │
                         │ Controller       │
                         └────────┬─────────┘
                                  │
                                  ▼
                         ┌──────────────────┐
                         │ InterviewService │
                         └────────┬─────────┘
                                  │
                ┌─────────────────┼─────────────────┐
                │                 │                 │
                ▼                 ▼                 ▼
        ┌─────────────┐   ┌─────────────┐   ┌───────────────┐
        │   Session   │   │   Planner   │   │   Evaluation  │
        │   Manager   │   │             │   │    Service    │
        └─────────────┘   └──────┬──────┘   └───────┬───────┘
                                 │                  │
                                 └────────┬─────────┘
                                          ▼
                               ┌────────────────────┐
                               │ Question Generation│
                               │      Service       │
                               └─────────┬──────────┘
                                         │
                                         ▼
                               ┌────────────────────┐
                               │       Gemini       │
                               │     AI / LLM       │
                               └─────────┬──────────┘
                                         │
                                         ▼
                               ┌────────────────────┐
                               │ Feedback Service   │
                               └────────────────────┘
```

---

# End-to-End Data Flow

## Starting an Interview

```text
Frontend
   │
   │ candidate = CAND-002
   ▼
POST /api/interview
   │
   ▼
InterviewController
   │
   ▼
InterviewService
   │
   ├── Validate Candidate
   ├── Create Session
   ├── Track Progress
   ├── Ask Planner
   └── Generate First Question
             │
             ▼
           Gemini
             │
             ▼
       InterviewResponse
             │
             ▼
          Frontend
```

## Submitting an Answer

```text
Frontend
   │
   │ sessionId + answer
   ▼
POST /api/interview
   │
   ▼
InterviewService
   │
   ├── Load Session
   ├── Validate Status
   ├── Store Answer
   ├── Evaluate Answer
   ├── Update Progress
   ├── Ask Planner
   │
   ├───────────────┐
   │               │
   ▼               ▼
Next Question   Complete
   │               │
   ▼               ▼
Gemini        FeedbackService
   │               │
   ▼               ▼
Question       Final Feedback
```

---

# Technology Stack

| Layer | Technology | Purpose |
|---|---|---|
| Frontend | React | User interface |
| Frontend Build | Vite | Development and production build |
| Styling | Tailwind CSS | Responsive UI |
| HTTP Client | Axios | REST API communication |
| Animation | GSAP | UI transitions and animations |
| Backend | Java 21 | Application logic |
| Framework | Spring Boot | REST backend |
| AI Integration | Spring AI | AI model integration |
| AI Model | Gemini | Planning, generation, evaluation, feedback |
| Build | Maven | Backend dependency/build management |
| JSON | Jackson | Serialization/deserialization |
| Session Storage | ConcurrentHashMap | Thread-safe in-memory sessions |
| Data | JSON | Synthetic candidates and curriculum |

---

# Repository Structure

```text
AI-Interview-Agent/
│
├── interview-agent/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/abtalks/interview/
│   │   │   │       ├── config/
│   │   │   │       ├── controller/
│   │   │   │       ├── service/
│   │   │   │       ├── planner/
│   │   │   │       ├── session/
│   │   │   │       ├── domain/
│   │   │   │       ├── dto/
│   │   │   │       ├── exception/
│   │   │   │       ├── model/
│   │   │   │       └── repository/
│   │   │   │
│   │   │   └── resources/
│   │   │       ├── application.yaml
│   │   │       ├── candidates.json
│   │   │       └── curriculum.json
│   │   │
│   │   └── test/
│   │
│   ├── pom.xml
│   └── README.md
│
├── frontend/
│   ├── src/
│   │   ├── api/
│   │   │   └── interviewApi.js
│   │   ├── components/
│   │   │   ├── Button.jsx
│   │   │   ├── ChatTurn.jsx
│   │   │   ├── CoverageBars.jsx
│   │   │   ├── DonutChart.jsx
│   │   │   ├── ErrorBanner.jsx
│   │   │   ├── MissionStrip.jsx
│   │   │   ├── OnAirDot.jsx
│   │   │   ├── PageFade.jsx
│   │   │   ├── RadialProgress.jsx
│   │   │   ├── StatCard.jsx
│   │   │   └── TopBar.jsx
│   │   ├── data/
│   │   │   ├── candidates.js
│   │   │   └── candidates.json
│   │   ├── screens/
│   │   │   ├── Landing.jsx
│   │   │   ├── Interview.jsx
│   │   │   └── Result.jsx
│   │   ├── App.jsx
│   │   ├── index.css
│   │   └── main.jsx
│   │
│   ├── package.json
│   ├── tailwind.config.js
│   ├── postcss.config.js
│   ├── PROMPTS.md
│   └── README.md
│
├── PROMPTS.md
└── README.md
```

---

# Frontend

The frontend provides the complete candidate-facing interview experience.

## Main Screens

### 1. Landing Screen

Responsibilities:

- Display candidate roster
- Show candidate information
- Allow candidate selection
- Start an interview

Candidate data is currently bundled locally because the backend does not expose a candidate-list endpoint.

### 2. Live Interview Screen

Displays:

- Candidate information
- Session ID
- AI questions
- Candidate answers
- Question count
- Mission progress
- Curriculum coverage
- Loading state
- Errors

The interface behaves like a real-time interview chat.

Input behavior:

```text
Enter       → Submit answer
Shift+Enter → New line
```

### 3. Result Screen

Displayed when:

```json
{
  "done": true
}
```

It presents:

- Overall score
- Questions asked
- Curriculum days covered
- Overall assessment
- Strengths
- Areas to improve
- Recommended next steps
- Coverage visualization
- Feedback composition

---

# Frontend Architecture

The application uses a simple three-stage state machine:

```text
LANDING
   │
   │ Start Interview
   ▼
INTERVIEW
   │
   ├── Answer submitted
   │       │
   │       ▼
   │   Backend processes
   │       │
   │       ├── done=false → INTERVIEW
   │       │
   │       └── done=true  → RESULT
   │
   ▼
RESULT
```

Main application state includes:

```javascript
stage
candidate
sessionId
messages
feedback
starting
startError
loading
error
```

The main state machine is implemented in:

```text
frontend/src/App.jsx
```

---

# Backend

The backend is the core orchestration layer.

## Main Responsibilities

```text
Candidate Validation
        ↓
Session Creation
        ↓
Interview Planning
        ↓
Question Generation
        ↓
Answer Evaluation
        ↓
Progress Tracking
        ↓
Adaptive Question Selection
        ↓
Completion Decision
        ↓
Feedback Generation
```

---

# Backend Components

## InterviewController

Exposes:

```text
POST /api/interview
```

The controller is intentionally thin and delegates business logic to `InterviewService`.

## InterviewService

Coordinates the entire interview lifecycle:

- Candidate validation
- Session creation
- Initial question generation
- Answer processing
- Answer evaluation
- Next-question planning
- Completion handling
- Feedback generation

## SessionManager

Current implementation:

```java
ConcurrentHashMap<String, InterviewSession>
```

This provides fast, thread-safe in-memory access to active sessions.

## InterviewPlanner

Determines what should happen next.

Possible actions include:

```text
ASK_QUESTION
COMPLETE_INTERVIEW
```

Planner decisions can include:

```text
Action
Curriculum Day
Topic
Difficulty
```

## ProgressTracker

Tracks:

- Question count
- Curriculum coverage
- Topics covered

## CoverageManager

Helps enforce interview coverage requirements.

The current project is designed around:

```text
Minimum Questions: 8
Minimum Curriculum Days: 4
```

## QuestionGenerationService

Converts planner decisions into actual interview questions using Gemini.

## AnswerEvaluationService

Evaluates candidate responses and stores the resulting evaluations in the interview session.

## FeedbackService

Generates the final structured assessment when the interview is complete.

---

# AI Intelligence Layer

Gemini is not treated as the entire application.

Instead:

```text
Application Rules + Interview State
               │
               ▼
         AI Decision Task
               │
               ▼
             Gemini
               │
               ▼
      Structured AI Result
               │
               ▼
Application Validates / Stores Result
```

This is important because the application controls how AI output affects the interview.

Gemini is used for:

### Question Generation

```text
Candidate Context
+
Planner Decision
+
Conversation History
        ↓
      Gemini
        ↓
Interview Question
```

### Answer Evaluation

```text
Question
+
Candidate Answer
        ↓
      Gemini
        ↓
Evaluation
```

### Interview Planning

```text
Current Progress
+
Coverage
+
Previous Evaluations
+
Interview Context
        ↓
      Planner / Gemini
        ↓
Next Action
```

### Feedback Generation

```text
Candidate
+
Questions
+
Answers
+
Evaluations
+
Progress
        ↓
      Gemini
        ↓
Final Feedback
```

---

# Interview Lifecycle

A session progresses through:

```text
START
  │
  ▼
STARTED
  │
  │ First Question
  ▼
ONGOING
  │
  ├───────────────┐
  │ More Questions│
  │               │
  ▼               ▼
ONGOING        COMPLETED
                  │
                  ▼
              FEEDBACK
```

Once a session reaches:

```text
COMPLETED
```

it should not accept additional answers.

---

# API Documentation

## Base URL

```text
http://localhost:8080
```

## Main Endpoint

```http
POST /api/interview
```

The same endpoint is used to:

1. Start a new interview
2. Continue an existing interview

---

## 1. Start Interview

### Request

```json
{
  "candidate": "CAND-002",
  "sessionId": null,
  "message": null
}
```

### Backend Process

```text
Candidate Validation
        ↓
Session Creation
        ↓
Initial Planner Decision
        ↓
Question Generation
        ↓
Conversation Update
        ↓
Response
```

### Response

```json
{
  "sessionId": "9d4a7e4d-3b07-40c5-91cc-fa4d94dc5445",
  "reply": "Let's start with...",
  "done": false
}
```

---

# 2. Continue Interview

The frontend sends the session ID and the candidate's latest answer.

### Request

```json
{
  "candidate": null,
  "sessionId": "9d4a7e4d-3b07-40c5-91cc-fa4d94dc5445",
  "message": "My answer to the question..."
}
```

### Backend Process

```text
Load Session
     ↓
Check Status
     ↓
Find Current Question
     ↓
Store Answer
     ↓
Evaluate Answer
     ↓
Update Progress
     ↓
Plan Next Action
     ↓
┌──────────────────────┐
│                      │
▼                      ▼
ASK_QUESTION       COMPLETE
│                      │
▼                      ▼
Generate Question   Generate Feedback
```

---

# 3. Ongoing Response

```json
{
  "sessionId": "SESSION_ID",
  "reply": "Next interview question...",
  "done": false
}
```

The frontend displays the returned question and continues the interview.

---

# 4. Completed Response

```json
{
  "sessionId": "SESSION_ID",
  "reply": "Thank you. The interview is complete.",
  "done": true,
  "feedback": {
    "overallScore": 8.0,
    "questionsAsked": 8,
    "curriculumDaysCovered": 4,
    "summary": "The candidate demonstrates strong technical capabilities.",
    "strengths": [
      "Strong understanding of vector databases",
      "Good architectural reasoning"
    ],
    "gaps": [
      "Needs deeper understanding of advanced retrieval architectures"
    ],
    "next": [
      "Practice advanced retrieval architectures"
    ]
  }
}
```

---

# Candidate & Curriculum Data

## Candidates

The backend currently uses a synthetic candidate dataset containing:

```text
20 candidates
```

Candidate data is loaded from:

```text
interview-agent/src/main/resources/candidates.json
```

The frontend also has a local representation for candidate selection.

The backend validates candidates before creating an interview session.

## Curriculum

The backend uses:

```text
31 curriculum days
```

The curriculum is used by:

- InterviewPlanner
- ProgressTracker
- CoverageManager
- QuestionGenerationService

This gives the AI interview a structured learning/interview scope instead of asking unrelated questions.

---

# Session Management

The current implementation uses:

```java
ConcurrentHashMap<String, InterviewSession>
```

Conceptually:

```text
sessionId
   │
   ▼
ConcurrentHashMap
   │
   ▼
InterviewSession
   ├── Candidate
   ├── Status
   ├── Progress
   ├── Conversation History
   ├── Evaluations
   └── Feedback
```

### Why In-Memory?

For the current hackathon implementation, this provides:

- Simple architecture
- Fast access
- Thread-safe map operations
- No external database requirement

### Trade-off

Sessions disappear when the backend restarts.

For production, Redis or another persistent session store would be appropriate.

---

# Error Handling

The backend uses centralized exception handling through:

```text
GlobalExceptionHandler
```

implemented with:

```java
@RestControllerAdvice
```

Supported business errors include:

| Exception | Meaning | HTTP |
|---|---|---|
| `CandidateNotFoundException` | Candidate does not exist | 404 |
| `SessionNotFoundException` | Session does not exist | 404 |
| `InvalidInterviewRequestException` | Invalid request | 400 |
| `InterviewCompleteException` | Interview already completed | Business-state error |

## Standard Error Format

```json
{
  "timestamp": "...",
  "status": 404,
  "error": "Candidate Not Found",
  "message": "Candidate not found: CAND-999",
  "path": "/api/interview"
}
```

This gives the frontend a consistent error contract.

---

# Security

## API Keys

The Gemini API key must never be committed to GitHub.

Use:

```text
GEMINI_API_KEY
```

as an environment variable.

Example:

### Windows CMD

```bash
set GEMINI_API_KEY=your-api-key
```

### PowerShell

```powershell
$env:GEMINI_API_KEY="your-api-key"
```

### Linux / macOS

```bash
export GEMINI_API_KEY="your-api-key"
```

## Correct AI Architecture

```text
Browser
   │
   │ Interview Request
   ▼
Spring Boot
   │
   │ Private API Key
   ▼
Gemini
```

The browser should **never** directly receive the Gemini API key.

---

# CORS

During local development:

```text
Frontend → http://localhost:5173
Backend  → http://localhost:8080
```

Since these are different origins, the backend configures CORS through:

```text
WebConfig.java
```

The API path is:

```text
/api/**
```

For production, CORS should be restricted to the actual deployed frontend domain.

---

# Configuration

Backend configuration:

```text
interview-agent/src/main/resources/application.yaml
```

The application uses Gemini through Spring AI.

A representative configuration is:

```yaml
spring:
  application:
    name: interview-agent

  ai:
    model:
      chat: google-genai

google:
  genai:
    api-key: ${GEMINI_API_KEY}

    chat:
      model: ${GEMINI_MODEL}
      temperature: 0.3
```

The exact property structure should match the Spring AI / Gemini version used by the project.

---

# Local Setup

## Prerequisites

Install:

- Java 21
- Maven
- Node.js / npm
- Git

Verify:

```bash
java -version
mvn -version
node -v
npm -v
```

---

# Clone the Repository

```bash
git clone https://github.com/loveyadav8478-blip/AI-Interview-Agent.git
cd AI-Interview-Agent
```

---

# Configure Gemini

Set the Gemini API key before starting the backend.

### Windows CMD

```bash
set GEMINI_API_KEY=your-api-key
```

### PowerShell

```powershell
$env:GEMINI_API_KEY="your-api-key"
```

### Linux / macOS

```bash
export GEMINI_API_KEY="your-api-key"
```

---

# ▶ Run Backend

```bash
cd interview-agent
mvn spring-boot:run
```

Backend:

```text
http://localhost:8080
```

---

# ▶ Run Frontend

Open a second terminal:

```bash
cd frontend
npm install
npm run dev
```

Frontend:

```text
http://localhost:5173
```

Create:

```text
frontend/.env
```

with:

```env
VITE_API_BASE_URL=http://localhost:8080
```

---

# Test the Backend

## Start Interview

```bash
curl -X POST http://localhost:8080/api/interview \
  -H "Content-Type: application/json" \
  -d "{\"candidate\":\"CAND-002\",\"sessionId\":null,\"message\":null}"
```

## Continue Interview

Replace `SESSION_ID`:

```bash
curl -X POST http://localhost:8080/api/interview \
  -H "Content-Type: application/json" \
  -d "{\"sessionId\":\"SESSION_ID\",\"message\":\"My technical answer\"}"
```

---

# Build for Production

## Backend

```bash
cd interview-agent
mvn clean package
```

JAR output:

```text
target/
```

Run:

```bash
java -jar target/interview-agent-0.0.1-SNAPSHOT.jar
```

## Frontend

```bash
cd frontend
npm run build
```

Production files are generated in:

```text
frontend/dist/
```

Preview:

```bash
npm run preview
```

---

# Deployment Architecture

A typical deployment can be:

```text
                         Internet
                            │
                            ▼
                   ┌────────────────┐
                   │    Frontend    │
                   │ Vercel / Static│
                   │     Hosting    │
                   └───────┬────────┘
                           │
                           │ HTTPS
                           ▼
                   ┌────────────────┐
                   │ Spring Boot    │
                   │    Backend     │
                   └───────┬────────┘
                           │
                           │ HTTPS
                           ▼
                   ┌────────────────┐
                   │     Gemini     │
                   │      API       │
                   └────────────────┘
```

For a more scalable deployment:

```text
                    Load Balancer
                         │
                 ┌───────┴───────┐
                 ▼               ▼
             Backend 1       Backend 2
                 │               │
                 └───────┬───────┘
                         ▼
                       Redis
                         │
                         ▼
                  Interview Sessions
```

---

# Important Design Decisions

## Backend Is the Source of Truth

The frontend does not independently calculate:

- Question selection
- Difficulty
- Answer score
- Curriculum coverage
- Completion
- Final feedback

The backend owns these decisions.

## Session ID Is the Interview Identity

Every interview receives a unique ID:

```text
9d4a7e4d-3b07-40c5-91cc-fa4d94dc5445
```

The frontend sends this ID with subsequent answers.

## Conversation History Is Server-Side

The backend retains:

```text
Question
Answer
Topic
Curriculum Day
Difficulty
Question Number
```

This allows future AI decisions to use the complete interview context.

## Frontend Does Not Fabricate Progress

The frontend displays values supplied by the backend.

It does not assume:

```javascript
if (questionCount >= 8) {
    finishInterview();
}
```

Instead it reacts to:

```json
{
  "done": true
}
```

---

# Frontend Responsibility vs Backend Responsibility

| Responsibility | Frontend | Backend |
|---|:---:|:---:|
| Candidate selection UI |  | |
| Candidate display |  | |
| Candidate validation | |  |
| Candidate data loading | |  |
| Start interview request |  | |
| Session creation | |  |
| Session storage | |  |
| Session ID handling |  |  |
| Question generation | |  |
| Question display |  | |
| Answer input |  | |
| Answer submission |  | |
| Answer evaluation | |  |
| Curriculum planning | |  |
| Difficulty selection | |  |
| Interview completion | |  |
| Feedback generation | |  |
| Feedback display |  | |
| Gemini communication | |  |
| CORS | |  |
| UI state |  | |

---

# Current Limitations

## In-Memory Sessions

Current sessions use `ConcurrentHashMap`.

Therefore:

```text
Server Restart
      ↓
Active Sessions Lost
```

## Single Backend Instance

In-memory sessions make horizontal scaling difficult without a shared session store.

## Local Candidate Dataset

Candidates are currently loaded from JSON rather than a database.

These choices are intentional simplifications for the current hackathon implementation.

---

# Future Improvements

## 1. Persistent Sessions

Replace:

```text
ConcurrentHashMap
```

with:

```text
Redis
```

or a database.

## 2. Database-Backed Candidates

Move candidate data into:

```text
PostgreSQL
MongoDB
```

or another persistent store.

## 3. Authentication

Add:

```text
Spring Security
JWT
OAuth2
```

## 4. Observability

Add:

```text
Spring Boot Actuator
Micrometer
Prometheus
Grafana
```

## 5. Rate Limiting

Protect the interview endpoint with:

```text
Bucket4j
Redis-based rate limiting
API Gateway
```

## 6. Distributed Sessions

Use Redis so multiple backend instances can share active interview sessions.

## 7. Streaming / Async AI

For longer AI operations, consider:

- Streaming responses
- Async processing
- Better perceived latency

## 8. AI Provider Fallback

Support a secondary model/provider when the primary AI provider is unavailable.

---

# Judge-Friendly Project Walkthrough

If you are presenting this project to judges, the easiest flow is:

## Step 1 — Explain the Problem

> Traditional technical interviews often rely on static question lists and provide limited personalized feedback.

## Step 2 — Show the Solution

> This project creates an adaptive AI interviewer that maintains interview context, evaluates every response, tracks curriculum coverage, and dynamically decides what should happen next.

## Step 3 — Show the Architecture

```text
React
  ↓
Spring Boot
  ↓
Interview Engine
  ↓
Gemini
```

Explain that **Gemini provides intelligence while Spring Boot controls application state and business rules**.

## Step 4 — Demonstrate Candidate Selection

Select a candidate from the landing page.

## Step 5 — Start the Interview

Show the first AI-generated question.

Point out the generated `sessionId`.

## Step 6 — Answer a Question

Submit an answer.

Explain:

```text
Answer
  ↓
Evaluation
  ↓
Progress Update
  ↓
Planner
  ↓
Next Question
```

## Step 7 — Demonstrate Adaptation

Show that the next question is generated from the current interview context rather than simply taking the next item from a static list.

## Step 8 — Complete the Interview

After sufficient coverage and signal, the backend decides:

```text
COMPLETE_INTERVIEW
```

## Step 9 — Show Final Feedback

Demonstrate:

- Score
- Strengths
- Gaps
- Curriculum coverage
- Recommended next steps

## Step 10 — Explain the Key Engineering Decision

The strongest architectural point to highlight:

> **The frontend never decides what the candidate should be asked next or when the interview should finish. The backend owns the interview state and intelligence.**

---

# Development Checklist

Before demonstrating the project:

### Backend

- [ ] Java 21 configured
- [ ] Maven build succeeds
- [ ] Gemini API key configured
- [ ] Candidate JSON loads
- [ ] Curriculum JSON loads
- [ ] Spring Boot starts on port 8080
- [ ] `POST /api/interview` works
- [ ] Candidate validation works
- [ ] Session creation works
- [ ] Question generation works
- [ ] Answer evaluation works
- [ ] Next-question planning works
- [ ] Curriculum coverage works
- [ ] Interview completion works
- [ ] Feedback generation works
- [ ] Completed sessions cannot be continued
- [ ] CORS works

### Frontend

- [ ] `npm install` succeeds
- [ ] `npm run dev` starts
- [ ] API URL is correct
- [ ] Candidate selection works
- [ ] Interview starts
- [ ] Questions appear
- [ ] Answers can be submitted
- [ ] Loading state works
- [ ] Error banner works
- [ ] Interview completion works
- [ ] Feedback appears
- [ ] Restart works
- [ ] `npm run build` succeeds

---

# Useful Commands

## Backend

```bash
mvn clean
mvn compile
mvn test
mvn clean package
mvn spring-boot:run
```

## Frontend

```bash
npm install
npm run dev
npm run build
npm run preview
```

---

# Complete Interview Example

```text
Candidate: CAND-002
       │
       ▼
Start Interview
       │
       ▼
Session Created
       │
       ▼
Question 1
       │
       ▼
Candidate Answer
       │
       ▼
Answer Evaluation
       │
       ▼
Planner Decision
       │
       ▼
Question 2
       │
       ▼
Candidate Answer
       │
       ▼
Answer Evaluation
       │
       ▼
Planner Decision
       │
       ▼
...
       │
       ▼
Question 8+
       │
       ▼
Final Evaluation
       │
       ▼
Planner:
COMPLETE_INTERVIEW
       │
       ▼
FeedbackService
       │
       ▼
Gemini
       │
       ▼
Final Feedback
       │
       ▼
Frontend Result Screen
```

---

# API Contract Summary

## Start

```json
{
  "candidate": "CAND-002",
  "sessionId": null,
  "message": null
}
```

## Continue

```json
{
  "candidate": null,
  "sessionId": "SESSION_ID",
  "message": "Candidate answer"
}
```

## Ongoing

```json
{
  "sessionId": "SESSION_ID",
  "reply": "Next question...",
  "done": false
}
```

## Completed

```json
{
  "sessionId": "SESSION_ID",
  "reply": "Thank you. The interview is complete.",
  "done": true,
  "feedback": {
    "overallScore": 8.0,
    "questionsAsked": 8,
    "curriculumDaysCovered": 4,
    "summary": "...",
    "strengths": [],
    "gaps": [],
    "next": []
  }
}
```

---

# What Makes This Project Different?

The project is more than:

```text
React + Chatbot + Gemini
```

Its core value is the **interview orchestration layer**:

```text
             Candidate Context
                    │
                    ▼
            ┌───────────────┐
            │ Interview     │
            │ State         │
            └───────┬───────┘
                    │
          ┌─────────┴─────────┐
          ▼                   ▼
     Curriculum          Evaluations
      Coverage                │
          │                   │
          └─────────┬─────────┘
                    ▼
              AI Planner
                    │
                    ▼
             Next Question
                    │
                    ▼
              Candidate
                    │
                    ▼
              AI Evaluation
                    │
                    └──────► Loop
```

This creates a controlled, stateful, curriculum-aware AI interview instead of an uncontrolled chatbot.

---

# Repository

GitHub:

```text
https://github.com/loveyadav8478-blip/AI-Interview-Agent
```

Project directories:

```text
interview-agent/   → Spring Boot backend
frontend/          → React frontend
```

---

# Conclusion

**AI Interview Agent** combines a modern React frontend, a Java/Spring Boot interview engine, structured interview state, curriculum-aware planning, and Gemini-powered intelligence into one complete interview platform.

The complete system follows:

```text
Candidate Selection
        ↓
Interview Session
        ↓
AI Question Generation
        ↓
Candidate Answer
        ↓
AI Answer Evaluation
        ↓
Progress + Curriculum Tracking
        ↓
Adaptive Planning
        ↓
Next Question
        ↓
Interview Completion
        ↓
AI Feedback Generation
        ↓
Actionable Candidate Report
```

The most important architectural principle is:

> **AI provides intelligence, while the backend controls state, rules, orchestration, and the interview lifecycle.**

This separation makes the system modular and allows the frontend and backend to evolve independently while keeping interview state and business logic centralized.

---

## Project Stack at a Glance

```text
┌───────────────────────────────────────────────┐
│                 AI INTERVIEW AGENT            │
├───────────────────────────────────────────────┤
│                                               │
│  Frontend                                     │
│  React + Vite + Tailwind + Axios + GSAP       │
│                                               │
│                     │                         │
│                     │ REST / JSON             │
│                     ▼                         │
│  Backend                                      │
│  Java 21 + Spring Boot + Spring AI            │
│                                               │
│                     │                         │
│                     │ AI Requests              │
│                     ▼                         │
│  Intelligence                                  │
│  Gemini                                       │
│                                               │
│  Planning • Questions • Evaluation • Feedback │
│                                               │
└───────────────────────────────────────────────┘
```

**Built as a complete adaptive AI interview system with a clear separation between UI, application orchestration, and AI intelligence.**
