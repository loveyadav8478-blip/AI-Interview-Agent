export default function ErrorBanner({ message, onDismiss }) {
  if (!message) return null;

  return (
    <div className="flex items-start justify-between gap-4 rounded-sm border border-coral/40 bg-coral-soft px-4 py-3 text-sm text-paper animate-rise">
      <p>
        <span className="mr-2 font-mono text-[11px] uppercase tracking-widest text-coral">
          Error
        </span>
        {message}
      </p>
      {onDismiss && (
        <button
          onClick={onDismiss}
          className="text-paper-dim hover:text-paper"
          aria-label="Dismiss error"
        >
          ✕
        </button>
      )}
    </div>
  );
}
