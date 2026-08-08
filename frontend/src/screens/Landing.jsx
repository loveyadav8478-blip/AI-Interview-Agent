import { useLayoutEffect, useRef, useState } from "react";
import gsap from "gsap";
import candidates from "../data/candidates.js";
import MissionStrip from "../components/MissionStrip.jsx";
import Button from "../components/Button.jsx";

export default function Landing({ onBegin, starting, startError }) {
  const [selectedId, setSelectedId] = useState(null);
  const heroRef = useRef(null);
  const gridRef = useRef(null);
  const dockRef = useRef(null);

  useLayoutEffect(() => {
    const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    if (reduceMotion) return;

    const ctx = gsap.context(() => {
      const tl = gsap.timeline({ defaults: { ease: "power3.out" } });

      tl.fromTo(
        heroRef.current.querySelectorAll("[data-hero]"),
        { opacity: 0, y: 18 },
        { opacity: 1, y: 0, duration: 0.6, stagger: 0.09 }
      ).fromTo(
        gridRef.current.querySelectorAll("[data-card]"),
        { opacity: 0, y: 16 },
        { opacity: 1, y: 0, duration: 0.45, stagger: 0.035 },
        "-=0.25"
      ).fromTo(
        dockRef.current,
        { opacity: 0, y: 10 },
        { opacity: 1, y: 0, duration: 0.4 },
        "-=0.2"
      );
    });

    return () => ctx.revert();
  }, []);

  const handleSelect = (id, e) => {
    setSelectedId(id);
    const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    if (!reduceMotion) {
      gsap.fromTo(
        e.currentTarget,
        { scale: 0.98 },
        { scale: 1, duration: 0.3, ease: "back.out(2)" }
      );
    }
  };

  return (
    <div className="mx-auto max-w-5xl px-6 py-14 md:py-20">
      <header ref={heroRef} className="mb-14 max-w-2xl">
        <p data-hero className="eyebrow mb-4">
          ABTalks AI Cohort · 31-Day Program
        </p>
        <h1 data-hero className="font-display text-4xl md:text-5xl font-medium leading-[1.08] text-paper">
          Build the interviewer,
          <br />
          <span className="italic text-signal">not the interview.</span>
        </h1>
        <p data-hero className="mt-5 text-paper-dim text-[15px] leading-relaxed">
          Select a candidate to begin. The agent reads their cohort record —
          what they finished, skipped, and struggled with — and conducts a
          live technical interview built entirely around it.
        </p>
      </header>

      {startError && (
        <div className="mb-6 rounded-sm border border-coral/40 bg-coral-soft px-4 py-3 text-sm text-paper">
          {startError}
        </div>
      )}

      <div className="mb-4 flex items-baseline justify-between">
        <p className="eyebrow">Roster · {candidates.length} candidates</p>
        <p className="eyebrow hidden sm:block">
          <span className="text-teal">■</span> passed &nbsp;
          <span className="text-coral">■</span> skipped &nbsp;
          <span className="text-ink-hair">■</span> —
        </p>
      </div>

      <div ref={gridRef} className="grid grid-cols-1 gap-3 sm:grid-cols-2">
        {candidates.map((c) => {
          const isSelected = c.id === selectedId;
          return (
            <button
              key={c.id}
              data-card
              onClick={(e) => handleSelect(c.id, e)}
              className={`group text-left rounded-sm border px-5 py-4 transition-colors duration-150 ${
                isSelected
                  ? "border-signal bg-ink-panel shadow-[0_0_0_1px_rgba(242,184,75,0.5)]"
                  : "border-ink-hair bg-ink-raised hover:border-paper-dim"
              }`}
            >
              <div className="flex items-start justify-between gap-3">
                <div>
                  <p className="font-display text-lg text-paper">{c.name}</p>
                  <p className="text-sm text-paper-dim">{c.jobRole}</p>
                </div>
                <span
                  className={`font-mono text-[11px] tracking-wide px-2 py-0.5 rounded-sm border ${
                    isSelected
                      ? "border-signal text-signal"
                      : "border-ink-hair text-paper-faint"
                  }`}
                >
                  {c.id}
                </span>
              </div>

              <div className="mt-4 flex items-center justify-between text-xs text-paper-faint font-mono">
                <span>
                  {c.completed}/{c.total} missions
                  {c.skipped > 0 ? ` · ${c.skipped} skipped` : ""}
                </span>
                <span>{c.yearsExperience} yrs exp</span>
              </div>

              <div className="mt-3">
                <MissionStrip missions={c.missions} />
              </div>
            </button>
          );
        })}
      </div>

      <div ref={dockRef} className="sticky bottom-6 mt-10 flex justify-center">
        <div className="flex items-center gap-4 rounded-sm border border-ink-hair bg-ink-raised/95 backdrop-blur px-5 py-3 shadow-panel">
          <p className="text-sm text-paper-dim">
            {selectedId ? (
              <>
                Interviewing <span className="text-paper font-medium">{selectedId}</span>
              </>
            ) : (
              "Choose a candidate to continue"
            )}
          </p>
          <Button disabled={!selectedId || starting} onClick={() => onBegin(selectedId)}>
            {starting ? "Starting…" : "Begin interview →"}
          </Button>
        </div>
      </div>
    </div>
  );
}
