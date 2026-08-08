export default function Button({
  children,
  onClick,
  disabled,
  variant = "primary",
  type = "button",
  className = "",
  ...rest
}) {
  const base =
    "inline-flex items-center justify-center gap-2 rounded-sm px-5 py-2.5 font-body text-sm font-medium transition-all duration-150 disabled:cursor-not-allowed disabled:opacity-40";

  const variants = {
    primary:
      "bg-signal text-ink hover:bg-[#f7c866] active:bg-signal-dim shadow-[0_0_0_1px_rgba(242,184,75,0.4)]",
    ghost:
      "bg-transparent text-paper border border-ink-hair hover:border-paper-dim hover:bg-ink-panel",
    subtle:
      "bg-ink-panel text-paper-dim border border-ink-hair hover:text-paper hover:border-paper-dim",
  };

  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`${base} ${variants[variant]} ${className}`}
      {...rest}
    >
      {children}
    </button>
  );
}
