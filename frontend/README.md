# AI Interview Agent — Frontend

A modern React + Vite + Tailwind CSS frontend for the **ABTalks AI Interview Agent**.

The frontend provides the complete candidate interview experience, including candidate selection, live AI-powered interviews, answer submission, interview progress, error handling, and the final feedback report.

The frontend communicates with the Spring Boot backend through a REST API. All interview intelligence remains on the backend, including question generation, answer evaluation, curriculum planning, interview completion, and feedback generation.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Application Flow](#application-flow)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Screens](#screens)
- [API Integration](#api-integration)
- [API Request and Response Flow](#api-request-and-response-flow)
- [Application State Management](#application-state-management)
- [Candidate Data](#candidate-data)
- [Error Handling](#error-handling)
- [Environment Configuration](#environment-configuration)
- [Local Development](#local-development)
- [Production Build](#production-build)
- [Deployment](#deployment)
- [CORS Configuration](#cors-configuration)
- [Security](#security)
- [Prompt Configuration](#prompt-configuration)
- [UI and Design](#ui-and-design)
- [Frontend Architecture Principles](#frontend-architecture-principles)
- [Development Checklist](#development-checklist)
- [Available Scripts](#available-scripts)
- [Backend Integration](#backend-integration)
- [Repository Structure](#repository-structure)
- [Conclusion](#conclusion)

---

# Overview

The AI Interview Agent frontend is a responsive web application that allows an interviewer or evaluator to select a candidate and conduct an AI-driven technical interview.

The application follows a simple three-stage workflow:

```text
┌──────────────────────────┐
│   Candidate Selection    │
└────────────┬─────────────┘
             │
             │ Start Interview
             ▼
┌──────────────────────────┐
│      Live Interview      │
│                          │
│ AI Question              │
│ Candidate Answer         │
│ AI Next Question         │
│ Progress                 │
└────────────┬─────────────┘
             │
             │ Interview Complete
             ▼
┌──────────────────────────┐
│    Final Feedback Report │
│                          │
│ Score                    │
│ Strengths                │
│ Gaps                     │
│ Next Steps               │
└──────────────────────────┘
