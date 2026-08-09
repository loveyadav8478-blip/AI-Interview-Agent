const ACCENT_RING = {
  blue: "ring-blue/40 bg-blue",
  orange: "ring-orange/40 bg-orange",
  purple: "ring-purple/40 bg-purple",
  green: "ring-green/40 bg-green",
};

export default function TopBar({ eyebrow, title, subtitle, accent = "blue", right = null }) {
  return (
    <header className="flex items-center justify-between gap-6 border-b border-base-hair pb-5">
      <div className="flex items-center gap-3">
        <span className={`flex h-9 w-9 items-center justify-center rounded-xl ring-1 ${ACCENT_RING[accent]} text-base font-bold text-base`}>
          IA
        </span>
        <div>
          {eyebrow && <p className="eyebrow mb-0.5">{eyebrow}</p>}
          <h1 className="font-display text-lg font-semibold text-text">{title}</h1>
          {subtitle && <p className="text-xs text-text-dim">{subtitle}</p>}
        </div>
      </div>
      {right && <div className="flex items-center gap-3">{right}</div>}
    </header>
  );
}
