import { createTheme } from "@mui/material/styles";
import palette from "./palette";

const theme = createTheme({
  palette,
  typography: {
    fontFamily: "M PLUS Rounded 1c, sans-serif", // 기본 폰트
    h1: {
      fontFamily: "M PLUS Rounded 1c, sans-serif",
      fontWeight: 700,
      fontSize: "1.025rem",
    },
    h2: {
      fontFamily: "M PLUS Rounded 1c, sans-serif",
      fontWeight: 700,
      fontSize: "1.1rem",
    },
    body1: {
      fontFamily: "M PLUS Rounded 1c, sans-serif",
      fontSize: "1rem", // 16px
      lineHeight: 1.5,
    },
    body2: {
      fontSize: "0.875rem", // 14px
      lineHeight: 1.43,
    },
  },
});

export default theme;
