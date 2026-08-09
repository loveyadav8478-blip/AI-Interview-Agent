import { useLayoutEffect, useRef } from "react";
import gsap from "gsap";

/**
 * segments: [{ label, value, color }]
 * Renders real proportions of whatever counts are passed in — no fabricated data.
 */
export default function DonutChart({ segments, size = 128, strokeWidth = 16 }) {
  const r = (size - strokeWidth) / 2;
  const circumference = 2 * Math.PI * r;
  const total = segments.reduce((sum, s) => sum + s.value, 0);
  const groupRef = useRef(null);

  let cumulative = 0;
  const arcs = segments.map((seg) => {
    const fraction = total > 0 ? seg.value / total : 0;
    const len = fraction * circumference;
    const rotation = (cumulative / (total || 1)) * 360;
    cumulative += seg.value;
    return { ...seg, len, rotation };
  });

  useLayoutEffect(() => {
    const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    if (reduceMotion || !groupRef.current) return;

    gsap.fromTo(
      groupRef.current,
      { opacity: 0, scale: 0.85, transformOrigin: "50% 50%" },
      { opacity: 1, scale: 1, duration: 0.6, ease: "back.out(1.6)" }
    );
  }, [total]);

  if (total === 0) {
    return (
      <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
        <circle cx={size / 2} cy={size / 2} r={r} fill="none" stroke="#242938" strokeWidth={strokeWidth} />
      </svg>
    );
  }

  return (
    <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
      <g ref={groupRef}>
        <circle cx={size / 2} cy={size / 2} r={r} fill="none" stroke="#1A1E2B" strokeWidth={strokeWidth} />
        {arcs.map((arc, i) => (
          <circle
            key={i}
            cx={size / 2}
            cy={size / 2}
            r={r}
            fill="none"
            stroke={arc.color}
            strokeWidth={strokeWidth}
            strokeDasharray={`${arc.len} ${circumference - arc.len}`}
            strokeLinecap="butt"
            transform={`rotate(${arc.rotation - 90} ${size / 2} ${size / 2})`}
          />
        ))}
      </g>
    </svg>
  );
}
