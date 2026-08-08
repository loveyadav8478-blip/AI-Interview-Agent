export default function OnAirDot({ active = true, className = "" }) {
  return (
    <span className={`relative inline-flex h-2 w-2 ${className}`}>
      {active && (
        <span className="absolute inline-flex h-full w-full animate-ping rounded-full bg-signal opacity-60" />
      )}
      <span
        className={`relative inline-flex h-2 w-2 rounded-full ${
          active ? "bg-signal" : "bg-paper-faint"
        }`}
      />
    </span>
  );
}
