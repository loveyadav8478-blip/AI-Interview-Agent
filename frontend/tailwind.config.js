/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      colors: {
        ink: {
          DEFAULT: "#0A0D13",
          raised: "#12161F",
          panel: "#161B26",
          hair: "#252B3A",
        },
        paper: {
          DEFAULT: "#EDEBE3",
          dim: "#A6A499",
          faint: "#6E6D66",
        },
        signal: {
          DEFAULT: "#F2B84B",
          dim: "#C99436",
          soft: "#3A2E1A",
        },
        teal: {
          DEFAULT: "#41BAAE",
          soft: "#16302E",
        },
        coral: {
          DEFAULT: "#E1604F",
          soft: "#331E1B",
        },
      },
      fontFamily: {
        display: ["'Fraunces'", "serif"],
        body: ["'IBM Plex Sans'", "sans-serif"],
        mono: ["'IBM Plex Mono'", "monospace"],
      },
      boxShadow: {
        panel: "0 1px 0 0 rgba(237,235,227,0.04) inset, 0 20px 40px -24px rgba(0,0,0,0.6)",
      },
      keyframes: {
        pulseDot: {
          "0%, 100%": { opacity: 1, transform: "scale(1)" },
          "50%": { opacity: 0.45, transform: "scale(0.85)" },
        },
        rise: {
          "0%": { opacity: 0, transform: "translateY(8px)" },
          "100%": { opacity: 1, transform: "translateY(0)" },
        },
      },
      animation: {
        pulseDot: "pulseDot 1.6s ease-in-out infinite",
        rise: "rise 0.45s cubic-bezier(0.16,1,0.3,1) both",
      },
    },
  },
  plugins: [],
};
