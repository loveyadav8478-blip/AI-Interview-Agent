import { useEffect, useRef, useState } from "react";
import ChatTurn from "../components/ChatTurn.jsx";
import Button from "../components/Button.jsx";
import OnAirDot from "../components/OnAirDot.jsx";
import ErrorBanner from "../components/ErrorBanner.jsx";
import MissionStrip from "../components/MissionStrip.jsx";
import RadialProgress from "../components/RadialProgress.jsx";
import TopBar from "../components/TopBar.jsx";
import PageFade from "../components/PageFade.jsx";

export default function Interview({ candidate, sessionId, messages, onSubmit, loading, error, onDismissError }) {
  const [answer, setAnswer] = useState("");
  const scrollRef = useRef(null);

  const questionCount = messages.filter((m) => m.role === "assistant").length;
  const cohortRate = candidate?.total ? (candidate.completed / candidate.total) * 10 : 0;

  useEffect(() => {
    scrollRef.current?.scrollTo({ top: scrollRef.current.scrollHeight, behavior: "smooth" });
  }, [messages, loading]);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!answer.trim() || loading) return;
    const value = answer;
    setAnswer("");
    onSubmit(value);
  };

  return (
    <PageFade className="mx-auto flex h-screen max-w-6xl flex-col px-6 py-6 md:py-8">
      <TopBar
        eyebrow="Live Session"
        title={candidate?.name || "Candidate"}
        subtitle={candidate?.jobRole}
        accent="green"
        right={
          <div className="flex items-center gap-2 rounded-lg border border-base-hair bg-base-surface px-3.5 py-2">
            <OnAirDot active={loading} />
            <span className="font-mono text-sm text-blue-glow">{questionCount}</span>
            <span className="font-mono text-xs text-text-faint">/ 8+ questions</span>
          </div>
        }
      />

      <div className="mt-6 grid min-h-0 flex-1 grid-cols-1 gap-6 md:grid-cols-[1fr_280px]">
        <div className="flex min-h-0 flex-col overflow-hidden rounded-xl2 border border-base-hair bg-base-surface shadow-panel">
          <div ref={scrollRef} className="flex-1 space-y-5 overflow-y-auto px-5 py-5">
            {messages.map((m, i) => (
              <ChatTurn
                key={i}
                role={m.role}
                content={m.content}
                questionNumber={m.role === "assistant" ? messages.slice(0, i + 1).filter((x) => x.role === "assistant").length : null}
              />
            ))}
            {loading && (
              <div className="flex items-center gap-2 px-0.5">
                <OnAirDot active />
                <span className="eyebrow">Interviewer is thinking…</span>
              </div>
            )}
          </div>

          <form onSubmit={handleSubmit} className="hairline flex gap-3 p-4">
            <textarea
              value={answer}
              onChange={(e) => setAnswer(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === "Enter" && !e.shiftKey) {
                  handleSubmit(e);
                }
              }}
              disabled={loading}
              placeholder="Type your technical answer… (Enter to send, Shift+Enter for a new line)"
              rows={2}
              className="flex-1 resize-none rounded-lg border border-base-hair bg-base px-3.5 py-2.5 text-[15px] text-text placeholder:text-text-faint focus:border-blue focus:outline-none disabled:opacity-50"
            />
            <Button type="submit" disabled={!answer.trim() || loading}>
              Send
            </Button>
          </form>
        </div>

        <aside className="hidden min-h-0 flex-col gap-4 overflow-y-auto pr-1 md:flex">
          <div className="rounded-xl2 border border-base-hair bg-base-surface p-5 shadow-panel">
            <p className="eyebrow mb-3">Cohort record</p>
            <div className="flex items-center gap-4">
              <RadialProgress
                value={cohortRate}
                max={10}
                size={72}
                accent="green"
                centerValue={`${candidate?.completed ?? 0}/${candidate?.total ?? 0}`}
              />
              <div>
                <p className="font-display text-sm font-medium text-text">{candidate?.name}</p>
                <p className="text-xs text-text-dim">{candidate?.jobRole}</p>
                {candidate?.skipped > 0 && (
                  <p className="mt-1 font-mono text-[11px] text-orange-glow">{candidate.skipped} skipped</p>
                )}
              </div>
            </div>
            {candidate?.missions && (
              <div className="mt-4">
                <MissionStrip missions={candidate.missions} size="lg" />
              </div>
            )}
          </div>

          <div className="rounded-xl2 border border-base-hair bg-base-surface p-5 shadow-panel">
            <p className="eyebrow mb-2">Session</p>
            <p className="break-all font-mono text-xs text-text-faint">{sessionId}</p>
          </div>

          <div className="rounded-xl2 border border-base-hair bg-base-surface p-5 shadow-panel">
            <p className="eyebrow mb-2">Bar for completion</p>
            <p className="text-sm text-text-dim">
              At least 8 questions across 4+ curriculum days. The agent
              decides when it has enough signal — this screen won't guess.
            </p>
          </div>
        </aside>
      </div>

      {error && (
        <div className="mt-4">
          <ErrorBanner message={error} onDismiss={onDismissError} />
        </div>
      )}
    </PageFade>
  );
}
