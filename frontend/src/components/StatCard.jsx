import { useLayoutEffect, useRef } from "react";
import gsap from "gsap";

const ACCENTS = {
  blue: { bg: "from-blue/20 to-blue/5", border: "border-blue/30", text: "text-blue-glow", chip: "bg-blue/15 text-blue-glow" },
  orange: { bg: "from-orange/20 to-orange/5", border: "border-orange/30", text: "text-orange-glow", chip: "bg-orange/15 text-orange-glow" },
  purple: { bg: "from-purple/20 to-purple/5", border: "border-purple/30", text: "text-purple-glow", chip: "bg-purple/15 text-purple-glow" },
  green: { bg: "from-green/20 to-green/5", border: "border-green/30", text: "text-green-glow", chip: "bg-green/15 text-green-glow" },
};

/**
 * value: number to count up to (or a string to render as-is, e.g. "—")
 * decimals: for scores like 7.4
 */
export default function StatCard({ label, value, decimals = 0, suffix = "", icon, accent = "blue" }) {
  const numRef = useRef(null);
  const style = ACCENTS[accent];
  const isNumeric = typeof value === "number";

  useLayoutEffect(() => {
    if (!isNumeric || !numRef.current) return;
    const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;

    if (reduceMotion) {
      numRef.current.textContent = value.toFixed(decimals);
      return;
    }

    const obj = { val: 0 };
    gsap.to(obj, {
      val: value,
      duration: 1,
      ease: "power3.out",
      onUpdate: () => {
        numRef.current.textContent = obj.val.toFixed(decimals);
      },
    });
  }, [value, decimals, isNumeric]);

  return (
    <div
      className={`rounded-xl2 border ${style.border} bg-gradient-to-br ${style.bg} p-5 shadow-panel`}
    >
      <div className="mb-4 flex items-center justify-between">
        <span className={`flex h-8 w-8 items-center justify-center rounded-lg text-sm ${style.chip}`}>
          {icon}
        </span>
      </div>
      <p className="font-display text-2xl font-semibold text-text">
        {isNumeric ? <span ref={numRef}>0</span> : value}
        {suffix}
      </p>
      <p className="eyebrow mt-1">{label}</p>
    </div>
  );
}
