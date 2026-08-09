import { useLayoutEffect, useRef } from "react";
import gsap from "gsap";
import Button from "../components/Button.jsx";
import RadialProgress from "../components/RadialProgress.jsx";
import DonutChart from "../components/DonutChart.jsx";
import CoverageBars from "../components/CoverageBars.jsx";
import TopBar from "../components/TopBar.jsx";

const COLUMN_STYLE = {
  green: { dot: "bg-green", chip: "bg-green/15 text-green-glow" },
  orange: { dot: "bg-orange", chip: "bg-orange/15 text-orange-glow" },
  blue: { dot: "bg-blue", chip: "bg-blue/15 text-blue-glow" },
};

function ReportColumn({ label, items, accent, icon }) {
  const style = COLUMN_STYLE[accent];
  const listRef = useRef(null);

  useLayoutEffect(() => {
    const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    if (reduceMotion || !listRef.current) return;

    gsap.fromTo(
      listRef.current.querySelectorAll("li"),
      { opacity: 0, x: -8 },
      { opacity: 1, x: 0, duration: 0.4, stagger: 0.06, delay: 0.5, ease: "power2.out" }
    );
  }, []);

  return (
    <div className="rounded-xl2 border border-base-hair bg-base-surface p-5 shadow-panel">
      <div className="mb-4 flex items-center gap-2">
        <span className={`flex h-7 w-7 items-center justify-center rounded-lg text-xs ${style.chip}`}>{icon}</span>
        <p className="eyebrow">{label}</p>
      </div>
      <ul ref={listRef} className="space-y-3">
        {(items || []).map((item, i) => (
          <li key={i} className="flex gap-3 text-sm leading-relaxed text-text-dim">
            <span className={`mt-2 h-1.5 w-1.5 shrink-0 rounded-full ${style.dot}`} />
            <span>{item}</span>
          </li>
        ))}
        {(!items || items.length === 0) && <li className="text-sm text-text-faint">Nothing recorded.</li>}
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
      gsap.fromTo(
        rootRef.current.querySelectorAll("[data-block]"),
        { opacity: 0, y: 16 },
        { opacity: 1, y: 0, duration: 0.5, stagger: 0.09, ease: "power3.out" }
      );
    }, rootRef);

    return () => ctx.revert();
  }, []);

  return (
    <div ref={rootRef} className="mx-auto max-w-5xl px-6 py-8 md:py-10">
      <div data-block>
        <TopBar
          eyebrow={`Interview Complete · ${candidate?.id ?? ""}`}
          title={`Assessment for ${candidate?.name ?? "Candidate"}`}
          subtitle={candidate?.jobRole}
          accent="purple"
        />
      </div>

      <div data-block className="mt-8 grid grid-cols-2 gap-4 md:grid-cols-4">
        <div className="rounded-xl2 border border-blue/30 bg-gradient-to-br from-blue/20 to-blue/5 p-5 shadow-panel">
          <RadialProgress value={feedback?.overallScore ?? 0} max={10} size={64} accent="blue" centerSuffix="/10" />
          <p className="eyebrow mt-3">Overall score</p>
        </div>
        <div className="rounded-xl2 border border-green/30 bg-gradient-to-br from-green/20 to-green/5 p-5 shadow-panel">
          <p className="font-display text-2xl font-semibold text-text">{feedback?.questionsAsked ?? "—"}</p>
          <p className="eyebrow mt-1">Questions asked</p>
        </div>
        <div className="rounded-xl2 border border-purple/30 bg-gradient-to-br from-purple/20 to-purple/5 p-5 shadow-panel">
          <p className="font-display text-2xl font-semibold text-text">{feedback?.curriculumDaysCovered ?? "—"}</p>
          <p className="eyebrow mt-1">Curriculum days</p>
        </div>
        <div className="rounded-xl2 border border-orange/30 bg-gradient-to-br from-orange/20 to-orange/5 p-5 shadow-panel">
          <p className="font-mono text-sm font-medium text-text truncate">{candidate?.id ?? "—"}</p>
          <p className="eyebrow mt-1">Candidate</p>
        </div>
      </div>

      <div data-block className="mt-6 grid grid-cols-1 gap-4 lg:grid-cols-[1.3fr_1fr]">
        <div className="rounded-xl2 border border-base-hair bg-base-surface p-6 shadow-panel">
          <p className="eyebrow mb-4">Coverage vs requirements</p>
          <CoverageBars
            questionsAsked={feedback?.questionsAsked}
            minQuestions={8}
            daysCovered={feedback?.curriculumDaysCovered}
            minDays={4}
          />
        </div>

        <div className="rounded-xl2 border border-base-hair bg-base-surface p-6 shadow-panel">
          <p className="eyebrow mb-4">Feedback composition</p>
          <div className="flex items-center gap-6">
            <DonutChart
              size={104}
              strokeWidth={14}
              segments={[
                { label: "Strengths", value: (feedback?.strengths || []).length, color: "#34D399" },
                { label: "Gaps", value: (feedback?.gaps || []).length, color: "#FF7A5C" },
                { label: "Next steps", value: (feedback?.next || []).length, color: "#4F7DFF" },
              ]}
            />
            <ul className="space-y-2">
              {[
                { label: "Strengths", value: (feedback?.strengths || []).length, dot: "bg-green" },
                { label: "Gaps", value: (feedback?.gaps || []).length, dot: "bg-orange" },
                { label: "Next steps", value: (feedback?.next || []).length, dot: "bg-blue" },
              ].map((row) => (
                <li key={row.label} className="flex items-center gap-2 text-sm">
                  <span className={`h-2 w-2 rounded-full ${row.dot}`} />
                  <span className="text-text-dim">{row.label}</span>
                  <span className="font-mono text-xs text-text-faint">{row.value}</span>
                </li>
              ))}
            </ul>
          </div>
        </div>
      </div>

      <div data-block className="mt-6 rounded-xl2 border border-base-hair bg-base-surface p-6 shadow-panel">
        <p className="eyebrow mb-3">Overall assessment</p>
        <p className="text-[15px] leading-relaxed text-text">{feedback?.summary}</p>
      </div>

      <div data-block className="mt-6 grid grid-cols-1 gap-4 sm:grid-cols-3">
        <ReportColumn label="Strengths" items={feedback?.strengths} accent="green" icon="✓" />
        <ReportColumn label="Areas to improve" items={feedback?.gaps} accent="orange" icon="!" />
        <ReportColumn label="Recommended next steps" items={feedback?.next} accent="blue" icon="→" />
      </div>

      <div data-block className="mt-10 flex justify-center">
        <Button variant="ghost" onClick={onRestart}>
          Start another interview
        </Button>
      </div>
    </div>
  );
}
