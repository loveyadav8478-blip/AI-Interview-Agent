# Interview Agent — Frontend

React + Vite + Tailwind frontend for the ABTalks AI Interview Agent
hackathon backend (Spring Boot).

## What this is

Three screens, exactly as the handoff doc specifies:

1. **Candidate Selection** — roster of the 20 synthetic candidates, built
   from the hackathon-supplied `candidates.json` (bundled locally, since
   the backend has no "list candidates" endpoint). Each card shows a
   31-cell strip representing that candidate's real mission record for
   the cohort (teal = passed, coral = skipped, dim = not assigned).
2. **Interview** — live chat with the AI interviewer. Sends only
   `{ sessionId, message }` per turn; the backend owns all interview
   intelligence (question count, curriculum coverage, difficulty,
   completion). The frontend never fabricates progress numbers the API
   doesn't return.
3. **Final Feedback Report** — score dial, questions asked, days
   covered, summary, strengths, gaps, and next steps — all straight from
   `response.feedback`.

## Setup

```bash
npm install
cp .env.example .env   # set VITE_API_BASE_URL if the backend isn't on :8080
npm run dev
```

The app runs at `http://localhost:5173` and talks to the Spring Boot
backend at `VITE_API_BASE_URL` (defaults to `http://localhost:8080`).

## Backend note

The backend repo didn't have a CORS config, so the browser would be
blocked from calling it from `localhost:5173`. I added
`interview-agent/src/main/java/com/abtalks/interview/config/WebConfig.java`
to the backend to allow `/api/**` from any origin — needed for local dev
and for a deployed frontend to reach a deployed backend. Drop that file
into the backend repo (already done if you're using the same working
copy) and restart it.

## Build

```bash
npm run build
```

Outputs to `dist/` — deployable to Vercel, Netlify, or any static host.
Set `VITE_API_BASE_URL` as an environment variable on the host to point
at your deployed backend.

## Structure

```
src/
  api/interviewApi.js       # POST /api/interview wrapper (start + continue)
  data/candidates.js        # bundled candidate roster + derived stats
  components/                # Button, ChatTurn, MissionStrip, OnAirDot, ErrorBanner
  screens/
    CandidateSelect.jsx
    Interview.jsx
    FeedbackReport.jsx
  App.jsx                    # 3-stage state machine: select → interview → feedback
```
