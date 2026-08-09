/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      colors: {
        base: {
          DEFAULT: "#0A0D16",
          surface: "#12151F",
          raised: "#171B28",
          hair: "#242938",
        },
        text: {
          DEFAULT: "#EDEFF5",
          dim: "#9096A8",
          faint: "#5A5F70",
        },
        blue: { DEFAULT: "#4F7DFF", soft: "#15203C", glow: "#7C9CFF" },
        orange: { DEFAULT: "#FF7A5C", soft: "#3A2119", glow: "#FF9A82" },
        purple: { DEFAULT: "#9B7BFF", soft: "#231C3E", glow: "#B79FFF" },
        green: { DEFAULT: "#34D399", soft: "#0F2C22", glow: "#6FE5BB" },
      },
      fontFamily: {
        display: ["'Inter'", "sans-serif"],
        body: ["'Inter'", "sans-serif"],
        mono: ["'IBM Plex Mono'", "monospace"],
      },
      boxShadow: {
        panel: "0 1px 0 0 rgba(237,239,245,0.03) inset, 0 24px 48px -28px rgba(0,0,0,0.7)",
        glow: "0 0 0 1px rgba(79,125,255,0.35), 0 16px 40px -20px rgba(79,125,255,0.35)",
      },
      borderRadius: {
        xl2: "1.25rem",
      },
    },
  },
  plugins: [],
};
