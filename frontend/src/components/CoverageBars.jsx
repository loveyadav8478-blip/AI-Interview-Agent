import { useLayoutEffect, useRef } from "react";
import gsap from "gsap";

function Bar({ label, value, min, unit }) {
  const fillRef = useRef(null);
  const meets = value >= min;
  const pct = Math.max(0, Math.min(100, (value / min) * 100));

  useLayoutEffect(() => {
    const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    if (!fillRef.current) return;

    if (reduceMotion) {
      fillRef.current.style.width = `${pct}%`;
      return;
    }

    gsap.fromTo(
      fillRef.current,
      { width: "0%" },
      { width: `${pct}%`, duration: 1, delay: 0.2, ease: "power3.out" }
    );
  }, [pct]);

  return (
    <div>
      <div className="mb-1.5 flex items-baseline justify-between">
        <p className="text-sm text-text-dim">{label}</p>
        <p className="font-mono text-xs text-text-faint">
          {value} {unit} <span className="text-text-faint">/ {min} min</span>
        </p>
      </div>
      <div className="h-2 w-full overflow-hidden rounded-full bg-base-hair">
        <div
          ref={fillRef}
          className={`h-full rounded-full ${meets ? "bg-green" : "bg-orange"}`}
          style={{ width: "0%" }}
        />
      </div>
    </div>
  );
}

export default function CoverageBars({ questionsAsked, minQuestions = 8, daysCovered, minDays = 4 }) {
  return (
    <div className="space-y-5">
      <Bar label="Questions asked" value={questionsAsked ?? 0} min={minQuestions} unit="asked" />
      <Bar label="Curriculum days covered" value={daysCovered ?? 0} min={minDays} unit="days" />
    </div>
  );
}
