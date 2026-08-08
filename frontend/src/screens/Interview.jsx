import { useEffect, useLayoutEffect, useRef, useState } from "react";
import gsap from "gsap";
import ChatTurn from "../components/ChatTurn.jsx";
import Button from "../components/Button.jsx";
import OnAirDot from "../components/OnAirDot.jsx";
import ErrorBanner from "../components/ErrorBanner.jsx";
import MissionStrip from "../components/MissionStrip.jsx";
import PageFade from "../components/PageFade.jsx";

export default function Interview({ candidate, sessionId, messages, onSubmit, loading, error, onDismissError }) {
  const [answer, setAnswer] = useState("");
  const scrollRef = useRef(null);
  const counterRef = useRef(null);
  const counterValue = useRef(0);

  const questionCount = messages.filter((m) => m.role === "assistant").length;

  useEffect(() => {
    scrollRef.current?.scrollTo({ top: scrollRef.current.scrollHeight, behavior: "smooth" });
  }, [messages, loading]);

  useLayoutEffect(() => {
    const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    if (!counterRef.current) return;

    if (reduceMotion) {
      counterRef.current.textContent = questionCount;
      counterValue.current = questionCount;
      return;
    }

    const obj = { val: counterValue.current };
    gsap.to(obj, {
      val: questionCount,
      duration: 0.5,
      ease: "power2.out",
      onUpdate: () => {
        counterRef.current.textContent = Math.round(obj.val);
      },
      onComplete: () => {
        counterValue.current = questionCount;
      },
    });
  }, [questionCount]);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!answer.trim() || loading) return;
    const value = answer;
    setAnswer("");
    onSubmit(value);
  };

  return (
    <PageFade className="mx-auto flex h-screen max-w-6xl flex-col px-6 py-6 md:py-8">
      <header className="mb-5 flex items-center justify-between border-b border-ink-hair pb-4">
        <div>
          <p className="eyebrow mb-1">The Interview Agent</p>
          <div className="flex items-center gap-2">
            <OnAirDot active={loading} />
            <h1 className="font-display text-xl text-paper">
              {candidate?.name || "Candidate"}
            </h1>
            <span className="font-mono text-xs text-paper-faint">{candidate?.id}</span>
          </div>
        </div>
        <div className="text-right">
          <p className="eyebrow mb-1">Question</p>
          <p className="font-mono text-lg text-signal">
            <span ref={counterRef}>0</span>
            <span className="text-paper-faint text-sm"> / 8+</span>
          </p>
        </div>
      </header>

      <div className="grid flex-1 grid-cols-1 gap-6 overflow-hidden md:grid-cols-[1fr_260px]">
        <div className="flex flex-col overflow-hidden rounded-sm border border-ink-hair bg-ink-raised">
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
              className="flex-1 resize-none rounded-sm border border-ink-hair bg-ink px-3.5 py-2.5 text-[15px] text-paper placeholder:text-paper-faint focus:border-signal focus:outline-none disabled:opacity-50"
            />
            <Button type="submit" disabled={!answer.trim() || loading}>
              Send
            </Button>
          </form>
        </div>

        <aside className="hidden flex-col gap-4 md:flex">
          <div className="rounded-sm border border-ink-hair bg-ink-raised p-4">
            <p className="eyebrow mb-3">Candidate dossier</p>
            <p className="font-display text-base text-paper">{candidate?.name}</p>
            <p className="mb-3 text-sm text-paper-dim">{candidate?.jobRole}</p>
            <p className="mb-1 font-mono text-xs text-paper-faint">
              {candidate?.completed}/{candidate?.total} missions completed
              {candidate?.skipped ? ` · ${candidate.skipped} skipped` : ""}
            </p>
            {candidate?.missions && <MissionStrip missions={candidate.missions} size="lg" />}
          </div>

          <div className="rounded-sm border border-ink-hair bg-ink-raised p-4">
            <p className="eyebrow mb-2">Session</p>
            <p className="break-all font-mono text-xs text-paper-faint">{sessionId}</p>
          </div>

          <div className="rounded-sm border border-ink-hair bg-ink-raised p-4">
            <p className="eyebrow mb-2">Bar for completion</p>
            <p className="text-sm text-paper-dim">
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
