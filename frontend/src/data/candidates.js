import raw from "./candidates.json";

/**
 * The backend does not expose a "list candidates" endpoint — the
 * hackathon-supplied candidates.json is the source of truth for this
 * screen. We bundle it directly rather than inventing an API.
 */
const candidates = raw.candidates.map((entry) => {
  const missions = entry.missions || [];
  const completed = missions.filter((m) => m.passed).length;
  const skipped = missions.filter((m) => m.skipped).length;
  const daysTouched = missions.map((m) => m.day).sort((a, b) => a - b);

  return {
    id: entry.member.id,
    name: entry.member.name,
    jobRole: entry.member.jobRole,
    yearsExperience: entry.member.yearsExperience,
    education: entry.member.education,
    status: entry.member.status,
    missions,
    completed,
    skipped,
    total: missions.length,
    daysTouched,
    signals: entry.signals || {},
  };
});

export default candidates;

export function getCandidateById(id) {
  return candidates.find((c) => c.id === id) || null;
}
