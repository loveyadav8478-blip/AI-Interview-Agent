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
    "inline-flex items-center justify-center gap-2 rounded-lg px-5 py-2.5 font-body text-sm font-medium transition-all duration-150 disabled:cursor-not-allowed disabled:opacity-40";

  const variants = {
    primary:
      "bg-gradient-to-b from-blue to-[#3D63E0] text-white shadow-glow hover:from-[#5C87FF] active:from-[#3D63E0]",
    ghost:
      "bg-transparent text-text border border-base-hair hover:border-text-dim hover:bg-base-raised",
    subtle:
      "bg-base-raised text-text-dim border border-base-hair hover:text-text hover:border-text-dim",
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
