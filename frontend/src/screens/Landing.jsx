import { useLayoutEffect, useMemo, useRef, useState } from "react";
import gsap from "gsap";
import candidates from "../data/candidates.js";
import TopBar from "../components/TopBar.jsx";
import StatCard from "../components/StatCard.jsx";
import MissionStrip from "../components/MissionStrip.jsx";
import Button from "../components/Button.jsx";

function statusFor(c) {
  if (c.completed === c.total) return { label: "Complete", cls: "bg-green/15 text-green-glow" };
  if (c.skipped > 0) return { label: `${c.skipped} skipped`, cls: "bg-orange/15 text-orange-glow" };
  return { label: "In progress", cls: "bg-blue/15 text-blue-glow" };
}

export default function Landing({ onBegin, starting, startError }) {
  const [selectedId, setSelectedId] = useState(null);
  const [query, setQuery] = useState("");
  const statsRef = useRef(null);
  const rowsRef = useRef(null);

  const aggregate = useMemo(() => {
    const totalCompleted = candidates.reduce((sum, c) => sum + c.completed, 0);
    const totalSkipped = candidates.reduce((sum, c) => sum + c.skipped, 0);
    const totalMissions = candidates.reduce((sum, c) => sum + c.total, 0);
    const avgRate = totalMissions > 0 ? (totalCompleted / totalMissions) * 100 : 0;
    return { totalCompleted, totalSkipped, avgRate };
  }, []);

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return candidates;
    return candidates.filter(
      (c) => c.name.toLowerCase().includes(q) || c.id.toLowerCase().includes(q) || c.jobRole.toLowerCase().includes(q)
    );
  }, [query]);

  useLayoutEffect(() => {
    const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    if (reduceMotion) return;

    const ctx = gsap.context(() => {
      gsap.fromTo(
        statsRef.current.children,
        { opacity: 0, y: 16 },
        { opacity: 1, y: 0, duration: 0.5, stagger: 0.07, ease: "power3.out" }
      );
    });

    return () => ctx.revert();
  }, []);

  useLayoutEffect(() => {
    const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    if (reduceMotion || !rowsRef.current) return;

    const ctx = gsap.context(() => {
      gsap.fromTo(
        rowsRef.current.querySelectorAll("[data-row]"),
        { opacity: 0, y: 10 },
        { opacity: 1, y: 0, duration: 0.35, stagger: 0.025, ease: "power2.out" }
      );
    });

    return () => ctx.revert();
  }, [filtered]);

  return (
    <div className="mx-auto max-w-6xl px-6 py-8 md:py-10">
      <TopBar
        eyebrow="ABTalks AI Cohort · 31-Day Program"
        title="Interview Agent"
        subtitle="Adaptive technical interviews from real cohort progress"
        right={
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Search candidates…"
            className="w-56 rounded-lg border border-base-hair bg-base-surface px-3.5 py-2 text-sm text-text placeholder:text-text-faint focus:border-blue focus:outline-none"
          />
        }
      />

      {startError && (
        <div className="mt-6 rounded-xl border border-orange/40 bg-orange-soft px-4 py-3 text-sm text-text">
          {startError}
        </div>
      )}

      <div ref={statsRef} className="mt-8 grid grid-cols-2 gap-4 md:grid-cols-4">
        <StatCard label="Candidates" value={candidates.length} icon="◈" accent="blue" />
        <StatCard label="Missions completed" value={aggregate.totalCompleted} icon="✓" accent="green" />
        <StatCard label="Missions skipped" value={aggregate.totalSkipped} icon="!" accent="orange" />
        <StatCard label="Avg completion rate" value={aggregate.avgRate} decimals={1} suffix="%" icon="%" accent="purple" />
      </div>

      <div className="mt-10 flex items-center justify-between">
        <p className="eyebrow">Roster · {filtered.length} of {candidates.length}</p>
      </div>

      <div className="mt-3 overflow-hidden rounded-xl2 border border-base-hair bg-base-surface shadow-panel">
        <div className="hidden grid-cols-[1.6fr_1fr_1.4fr_1fr_auto] gap-4 border-b border-base-hair px-5 py-3 md:grid">
          {["Candidate", "Role", "Cohort record", "Status", ""].map((h) => (
            <p key={h} className="eyebrow">{h}</p>
          ))}
        </div>

        <div ref={rowsRef}>
          {filtered.map((c) => {
            const isSelected = c.id === selectedId;
            const status = statusFor(c);

            return (
              <div
                key={c.id}
                data-row
                onClick={() => setSelectedId(c.id)}
                className={`grid grid-cols-2 gap-4 border-b border-base-hair px-5 py-4 last:border-b-0 md:grid-cols-[1.6fr_1fr_1.4fr_1fr_auto] md:items-center cursor-pointer transition-colors ${
                  isSelected ? "bg-blue/5" : "hover:bg-base-raised"
                }`}
              >
                <div>
                  <p className="font-display text-sm font-medium text-text">{c.name}</p>
                  <p className="font-mono text-xs text-text-faint">{c.id}</p>
                </div>
                <p className="text-sm text-text-dim">{c.jobRole}</p>
                <div>
                  <MissionStrip missions={c.missions} />
                  <p className="mt-1.5 font-mono text-[11px] text-text-faint">
                    {c.completed}/{c.total} · {c.yearsExperience} yrs exp
                  </p>
                </div>
                <span className={`w-fit rounded-full px-2.5 py-1 text-xs font-medium ${status.cls}`}>
                  {status.label}
                </span>
                <div className="flex justify-end">
                  <Button
                    variant={isSelected ? "primary" : "subtle"}
                    disabled={starting}
                    onClick={(e) => {
                      e.stopPropagation();
                      setSelectedId(c.id);
                      onBegin(c.id);
                    }}
                  >
                    {starting && isSelected ? "Starting…" : "Interview →"}
                  </Button>
                </div>
              </div>
            );
          })}

          {filtered.length === 0 && (
            <div className="px-5 py-10 text-center text-sm text-text-dim">
              No candidates match “{query}”.
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
