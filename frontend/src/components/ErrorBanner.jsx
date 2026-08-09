export default function ErrorBanner({ message, onDismiss }) {
  if (!message) return null;

  return (
    <div className="flex items-start justify-between gap-4 rounded-xl border border-orange/40 bg-orange-soft px-4 py-3 text-sm text-text animate-rise">
      <p>
        <span className="mr-2 font-mono text-[11px] uppercase tracking-widest text-orange-glow">
          Error
        </span>
        {message}
      </p>
      {onDismiss && (
        <button
          onClick={onDismiss}
          className="text-text-dim hover:text-text"
          aria-label="Dismiss error"
        >
          ✕
        </button>
      )}
    </div>
  );
}
