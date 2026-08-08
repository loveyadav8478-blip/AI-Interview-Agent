import { useLayoutEffect, useRef } from "react";
import gsap from "gsap";
import Button from "../components/Button.jsx";

const RADIUS = 54;
const CIRCUMFERENCE = 2 * Math.PI * RADIUS;

function ScoreDial({ score }) {
  const circleRef = useRef(null);
  const numberRef = useRef(null);

  useLayoutEffect(() => {
    const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    const pct = Math.max(0, Math.min(10, score ?? 0)) / 10;
    const targetOffset = CIRCUMFERENCE * (1 - pct);

    if (!circleRef.current) return;

    if (reduceMotion) {
      circleRef.current.style.strokeDashoffset = targetOffset;
      if (numberRef.current) numberRef.current.textContent = (score ?? 0).toFixed(1);
      return;
    }

    gsap.set(circleRef.current, { strokeDashoffset: CIRCUMFERENCE });

    const obj = { val: 0 };
    gsap.to(circleRef.current, {
      strokeDashoffset: targetOffset,
      duration: 1.1,
      delay: 0.2,
      ease: "power3.out",
    });
    gsap.to(obj, {
      val: score ?? 0,
      duration: 1.1,
      delay: 0.2,
      ease: "power3.out",
      onUpdate: () => {
        if (numberRef.current) numberRef.current.textContent = obj.val.toFixed(1);
      },
    });
  }, [score]);

  return (
    <div className="relative flex h-40 w-40 items-center justify-center">
      <svg width="160" height="160" viewBox="0 0 120 120" className="-rotate-90">
        <circle cx="60" cy="60" r={RADIUS} fill="none" stroke="#252B3A" strokeWidth="6" />
        <circle
          ref={circleRef}
          cx="60"
          cy="60"
          r={RADIUS}
          fill="none"
          stroke="#F2B84B"
          strokeWidth="6"
          strokeLinecap="round"
          strokeDasharray={CIRCUMFERENCE}
        />
      </svg>
      <div className="absolute flex flex-col items-center">
        <span ref={numberRef} className="font-display text-4xl text-paper">
          0.0
        </span>
        <span className="font-mono text-[11px] uppercase tracking-widest text-paper-faint">/ 10</span>
      </div>
    </div>
  );
}

function ReportColumn({ label, items, accent }) {
  const dot = { teal: "bg-teal", coral: "bg-coral", signal: "bg-signal" }[accent];
  const listRef = useRef(null);

  useLayoutEffect(() => {
    const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    if (reduceMotion || !listRef.current) return;

    gsap.fromTo(
      listRef.current.querySelectorAll("li"),
      { opacity: 0, x: -8 },
      { opacity: 1, x: 0, duration: 0.4, stagger: 0.06, delay: 0.6, ease: "power2.out" }
    );
  }, []);

  return (
    <div className="rounded-sm border border-ink-hair bg-ink-raised p-5">
      <p className="eyebrow mb-4">{label}</p>
      <ul ref={listRef} className="space-y-3">
        {(items || []).map((item, i) => (
          <li key={i} className="flex gap-3 text-sm leading-relaxed text-paper-dim">
            <span className={`mt-2 h-1.5 w-1.5 shrink-0 rounded-full ${dot}`} />
            <span>{item}</span>
          </li>
        ))}
        {(!items || items.length === 0) && (
          <li className="text-sm text-paper-faint">Nothing recorded.</li>
        )}
      </ul>
    </div>
  );
}

export default function Result({ candidate, feedback, onRestart }) {
  const rootRef = useRef(null);

  useLayoutEffect(() => {
    const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    if (reduceMotion || !rootRef.current) return;

    const ctx = gsap.context(() => {
      const tl = gsap.timeline({ defaults: { ease: "power3.out" } });

      tl.fromTo(
        rootRef.current.querySelectorAll("[data-block]"),
        { opacity: 0, y: 16 },
        { opacity: 1, y: 0, duration: 0.5, stagger: 0.1 }
      );
    }, rootRef);

    return () => ctx.revert();
  }, []);

  return (
    <div ref={rootRef} className="mx-auto max-w-4xl px-6 py-14 md:py-20">
      <header data-block className="mb-10">
        <p className="eyebrow mb-3">Interview Complete · {candidate?.id}</p>
        <h1 className="font-display text-3xl md:text-4xl text-paper">
          Assessment for {candidate?.name}
        </h1>
      </header>

      <div
        data-block
        className="mb-10 flex flex-col items-center gap-8 rounded-sm border border-ink-hair bg-ink-raised p-8 sm:flex-row sm:items-center sm:justify-between"
      >
        <ScoreDial score={feedback?.overallScore} />
        <div className="flex flex-1 gap-8 sm:justify-end">
          <div>
            <p className="font-display text-3xl text-paper">{feedback?.questionsAsked ?? "—"}</p>
            <p className="eyebrow mt-1">Questions asked</p>
          </div>
          <div>
            <p className="font-display text-3xl text-paper">{feedback?.curriculumDaysCovered ?? "—"}</p>
            <p className="eyebrow mt-1">Curriculum days</p>
          </div>
        </div>
      </div>

      <div data-block className="mb-10 rounded-sm border border-ink-hair bg-ink-raised p-6">
        <p className="eyebrow mb-3">Overall assessment</p>
        <p className="text-[15px] leading-relaxed text-paper">{feedback?.summary}</p>
      </div>

      <div data-block className="grid grid-cols-1 gap-4 sm:grid-cols-3">
        <ReportColumn label="Strengths" items={feedback?.strengths} accent="teal" />
        <ReportColumn label="Areas to improve" items={feedback?.gaps} accent="coral" />
        <ReportColumn label="Recommended next steps" items={feedback?.next} accent="signal" />
      </div>

      <div data-block className="mt-12 flex justify-center">
        <Button variant="ghost" onClick={onRestart}>
          Start another interview
        </Button>
      </div>
    </div>
  );
}
