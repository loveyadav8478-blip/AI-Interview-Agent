import { useLayoutEffect, useRef } from "react";
import gsap from "gsap";

/**
 * Wraps a screen and fades/rises it in on mount with GSAP.
 * Respects prefers-reduced-motion.
 */
export default function PageFade({ children, className = "" }) {
  const ref = useRef(null);

  useLayoutEffect(() => {
    const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    if (reduceMotion || !ref.current) return;

    const ctx = gsap.context(() => {
      gsap.fromTo(
        ref.current,
        { opacity: 0, y: 14 },
        { opacity: 1, y: 0, duration: 0.55, ease: "power3.out" }
      );
    }, ref);

    return () => ctx.revert();
  }, []);

  return (
    <div ref={ref} className={className}>
      {children}
    </div>
  );
}
