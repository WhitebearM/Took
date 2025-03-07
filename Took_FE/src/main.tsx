import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { ThemeProvider } from "@mui/material/styles";
import { CssBaseline } from "@mui/material"; // MUI의 기본 CSS 리셋
import App from "./App.tsx";
import theme from "./theme";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <ThemeProvider theme={theme}>
      <CssBaseline /> {/* 브라우저 스타일 초기화 */}
      <App />
    </ThemeProvider>
  </StrictMode>
);
