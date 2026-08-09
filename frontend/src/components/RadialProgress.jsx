import { useLayoutEffect, useRef } from "react";
import gsap from "gsap";

const STROKE_COLOR = {
  blue: "#4F7DFF",
  orange: "#FF7A5C",
  purple: "#9B7BFF",
  green: "#34D399",
};

/**
 * value/max define the fill fraction. centerValue/centerSuffix render inside the ring.
 */
export default function RadialProgress({ value, max = 10, size = 96, accent = "blue", centerValue, centerSuffix = "" }) {
  const r = (size - 12) / 2;
  const circumference = 2 * Math.PI * r;
  const circleRef = useRef(null);
  const numRef = useRef(null);

  useLayoutEffect(() => {
    const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    const pct = Math.max(0, Math.min(1, (value ?? 0) / max));
    const targetOffset = circumference * (1 - pct);

    if (!circleRef.current) return;

    if (reduceMotion) {
      circleRef.current.style.strokeDashoffset = targetOffset;
      if (numRef.current) numRef.current.textContent = centerValue ?? (value ?? 0).toFixed(1);
      return;
    }

    gsap.set(circleRef.current, { strokeDashoffset: circumference });
    gsap.to(circleRef.current, { strokeDashoffset: targetOffset, duration: 1.1, delay: 0.15, ease: "power3.out" });

    if (numRef.current && typeof value === "number") {
      const obj = { val: 0 };
      gsap.to(obj, {
        val: value,
        duration: 1.1,
        delay: 0.15,
        ease: "power3.out",
        onUpdate: () => {
          numRef.current.textContent = obj.val.toFixed(1);
        },
      });
    }
  }, [value, max, circumference, centerValue]);

  return (
    <div className="relative flex items-center justify-center" style={{ height: size, width: size }}>
      <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`} className="-rotate-90">
        <circle cx={size / 2} cy={size / 2} r={r} fill="none" stroke="#242938" strokeWidth="6" />
        <circle
          ref={circleRef}
          cx={size / 2}
          cy={size / 2}
          r={r}
          fill="none"
          stroke={STROKE_COLOR[accent]}
          strokeWidth="6"
          strokeLinecap="round"
          strokeDasharray={circumference}
        />
      </svg>
      <div className="absolute flex flex-col items-center">
        <span ref={numRef} className="font-display text-lg font-semibold text-text">
          {centerValue ?? "0.0"}
        </span>
        {centerSuffix && <span className="font-mono text-[9px] uppercase tracking-widest text-text-faint">{centerSuffix}</span>}
      </div>
    </div>
  );
}
