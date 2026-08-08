import { useLayoutEffect, useRef } from "react";
import gsap from "gsap";
import OnAirDot from "./OnAirDot.jsx";

export default function ChatTurn({ role, content, questionNumber }) {
  const isAI = role === "assistant";
  const ref = useRef(null);

  useLayoutEffect(() => {
    const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    if (reduceMotion || !ref.current) return;

    gsap.fromTo(
      ref.current,
      { opacity: 0, y: 10, x: isAI ? -6 : 6 },
      { opacity: 1, y: 0, x: 0, duration: 0.4, ease: "power2.out" }
    );
  }, [isAI]);

  return (
    <div ref={ref} className={`flex flex-col gap-1.5 ${isAI ? "items-start" : "items-end"}`}>
      <div className="flex items-center gap-2 px-0.5">
        {isAI ? (
          <>
            <OnAirDot active={false} />
            <span className="eyebrow">Interviewer{questionNumber ? ` · Q${questionNumber}` : ""}</span>
          </>
        ) : (
          <span className="eyebrow">You</span>
        )}
      </div>
      <div
        className={`max-w-[36rem] rounded-sm px-4 py-3 text-[15px] leading-relaxed ${
          isAI
            ? "bg-ink-panel border border-ink-hair text-paper rounded-tl-none"
            : "bg-signal-soft border border-signal/30 text-paper rounded-tr-none"
        }`}
      >
        {content}
      </div>
    </div>
  );
}
