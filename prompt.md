# AI Prompt Engineering Log

This document records the prompts and AI-assisted engineering
decisions used while building the ABTalks AI Interview Agent.

The prompts are versioned during development to document how
AI behavior was designed, tested, and refined.

---

## Prompt 001 — Interview Question Generation

### Purpose

Generate one realistic technical interview question based on
the candidate's profile, the selected curriculum topic,
previous conversation, and the deterministic planner decision.

### Context Provided

- Candidate name
- Job role
- Years of experience
- Curriculum day
- Curriculum topic
- Learning objectives
- Interview difficulty
- Previous conversation
- Previous answer
- Planner action

### Important Constraints

- Generate exactly one question.
- Do not provide the answer.
- Avoid repeating previous questions.
- Match the selected difficulty.
- Remain grounded in the curriculum topic.
- Use previous answers when generating follow-ups.

### Engineering Decision

The application determines the interview strategy before
calling the LLM. The LLM is responsible for natural-language
question generation rather than controlling interview state.

---

## Prompt 002 — Answer Evaluation

### Purpose

Evaluate a candidate's answer against the relevant curriculum
objectives.

### Output

The evaluator produces:

- Score
- Strengths
- Weaknesses
- Reasoning
- Follow-up requirement

### Engineering Decision

Evaluation is structured so that the deterministic planner
can use the result to decide whether to continue probing,
change difficulty, or move to another topic.