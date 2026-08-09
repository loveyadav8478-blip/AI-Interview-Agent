/**
 * Renders the candidate's real 31-day cohort record as a strip of cells —
 * one per curriculum day. Built entirely from the supplied candidates.json
 * (mission day + passed/skipped), nothing invented.
 */
export default function MissionStrip({ missions, size = "sm" }) {
  const byDay = new Map(missions.map((m) => [m.day, m]));
  const cellSize = size === "lg" ? "h-3.5 w-2" : "h-2.5 w-1.5";

  return (
    <div className="flex gap-[3px]" role="img" aria-label="31-day cohort mission record">
      {Array.from({ length: 31 }, (_, i) => i + 1).map((day) => {
        const mission = byDay.get(day);
        let cls = "bg-base-hair";
        if (mission?.passed) cls = "bg-green";
        else if (mission?.skipped) cls = "bg-orange/70";

        return (
          <span
            key={day}
            title={mission ? `Day ${day} — ${mission.title}${mission.skipped ? " (skipped)" : ""}` : `Day ${day} — not assigned`}
            className={`${cellSize} rounded-[1px] ${cls}`}
          />
        );
      })}
    </div>
  );
}
