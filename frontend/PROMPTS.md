# PROMPTS.md — Interview Agent Frontend

This is the real prompt log for how this frontend was built, in order, using
Claude. The backend (Spring Boot + Gemini) was already complete before this
log starts — this covers only the React frontend built on top of it.

---

### 1. Initial build request

> "want to build the leftover part can u help me build the frontend for this
> backend... ive already build the backend"

Provided: the backend repo (zip) and the ABTalks frontend handoff doc.

**Result:** Read the Spring Boot backend's controller/DTOs
(`InterviewController`, `InterviewRequest`, `InterviewResponse`, `Feedback`)
to confirm the exact API contract, cross-checked against the handoff doc's
recommended 3-screen flow (Candidate Selection → Interview → Final Feedback
Report). Scaffolded a Vite + React + Tailwind project, bundled
`candidates.json` client-side (no list-candidates endpoint exists on the
backend), and built all three screens plus an `interviewApi.js` wrapper
matching `POST /api/interview` exactly. Also added `WebConfig.java` to the
backend, since no CORS config existed and the browser would otherwise block
every request from the Vite dev server.

---

### 2. Verifying backend understanding

> "hey lets discuss first then we will procced first didu understand my
> backend or not ?"
>
> "do u have complete access to my backend or not"
>
> "what should i provide u"
>
> "what do u need to see right now"

**Result:** Walked through the actual planner/service code file-by-file
(`InterviewPlanner`, `CoverageManager`, `FollowUpDecisionEngine`,
`QuestionGenerationService`, `AnswerEvaluationService`, `FeedbackService`,
`SessionManager`) rather than inferring from filenames. Found and reported a
real bug: `ProgressTracker.recordEvaluation()` is wired into
`InterviewService` but never called, so per-topic mastery tracking silently
never activates. Confirmed in-memory `ConcurrentHashMap` session storage,
confirmed `FeedbackService` computes `overallScore` /
`curriculumDaysCovered` deterministically rather than trusting the LLM for
those numbers.

---

### 3. Rebuilding with GSAP, three named pages

> "now i want build its frontend of three pages first landing page then
> interview page then result page using react gsap can u build it"

**Result:** Renamed the three screens to `Landing.jsx`, `Interview.jsx`,
`Result.jsx` and added `gsap` as a dependency. Built a shared `PageFade`
entrance wrapper, per-message GSAP entrance animation on `ChatTurn` (AI
slides from the left, candidate from the right), a real GSAP number-tween
for the live question counter, and a GSAP-driven SVG score dial with
count-up on the Result page. All motion respects
`prefers-reduced-motion`.

---

### 4. Confirming backend state after user's own edits

> uploaded the backend zip again, with their own `CorsConfig.java` and a
> `spring.profiles.active: gemini` addition to `application.yaml`

**Result:** Diffed the new upload against the previous copy to confirm what
actually changed (their CORS config replacing mine, the Gemini profile
flag), verified `@Profile("gemini")` on `GeminiLLMService` actually matches
that yaml setting, confirmed the frontend's `package.json` was untouched.
Installed and built the user's exact uploaded frontend copy to verify it
compiles clean. Was upfront that the sandbox has no Maven Central access, so
the Java side can't actually be booted or tested end-to-end from this
environment — only read and reasoned about statically.

---

### 5. Visual redesign to a reference dashboard

> uploaded a screenshot of an "Aura Store" admin dashboard (dark navy,
> gradient stat cards, sidebar, data table)
>
> "yes 1 use react gsap to do this" (restyle the Interview Agent frontend to
> this look)

**Result:** Rebuilt the design system: new Tailwind color tokens (navy
base + blue/orange/purple/green accent gradients), switched to Inter for
type. Built `TopBar`, `StatCard`, and `RadialProgress` as reusable
components matching the reference's visual language. Rebuilt Landing as a
proper dashboard — live search, four real aggregate stat cards computed
from `candidates.json`, roster as a data table with status pills instead of
a card grid. Did not copy the reference's literal sidebar nav
(Dashboard/Sales/Products/etc.), since those destinations don't exist in
this app and adding them would imply functionality that isn't there.

---

### 6. Bug report: frontend not updating

> pasted a screenshot showing the old (pre-redesign) UI still rendering

**Result:** Diagnosed as a stale local copy / cached dev server, not a code
issue — walked through a clean reinstall (`rm -rf node_modules dist`,
reinstall, hard refresh).

> "PS E:\...> npm run dev ... 'vite' is not recognized..."

**Result:** Diagnosed as `npm run dev` having been run before `npm install`
completed — pointed out the fix (run `npm install` first) and flagged that
`npm audit fix --force` shouldn't be run since it can pull in breaking major
version bumps right before a deadline.

---

### 7. More charts on the Result page

> "hey result page need to contains more charts or visuals"

**Result:** Added `CoverageBars.jsx` (animated horizontal bars comparing
actual questions-asked / curriculum-days-covered against the hackathon's
stated minimums of 8 and 4) and `DonutChart.jsx` (a generic multi-segment
donut, used here to show the real proportion of strengths / gaps /
next-steps counts from the feedback object). Deliberately did not add a
per-question score chart or per-day radar, since the backend's API contract
(`sessionId`, `reply`, `done`, `feedback`) doesn't return per-turn scores or
which specific days were covered — a chart like that would have had to
invent numbers the backend never sends.

---

### 8. Layout bug: content pushed off-screen

> uploaded a screenshot showing the Interview page's Send button cut off at
> the bottom of the viewport
>
> "at the bottom it is not showing full screen"

**Result:** Diagnosed as the classic flex/grid `min-height: auto` bug — the
right-hand rail's stacked cards were growing past the intended height and
dragging the whole page taller than the viewport, since `overflow-hidden` on
a flex/grid parent doesn't stop a child from refusing to shrink by default.
Fixed by adding `min-h-0` to the grid row and chat panel, and giving the
right rail its own independent `overflow-y-auto` so it scrolls internally
instead of pushing the page.

---

### 9. This file

> "hey make prompt file also of frontend pls"

**Result:** This document.
