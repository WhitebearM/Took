import React from "react";
import { useTheme } from "@mui/material/styles";

interface MainLayoutProps {
  rightComponent?: JSX.Element;
  children: React.ReactNode;
}

const MainLayout: React.FC<MainLayoutProps> = ({
  rightComponent,
  children,
}) => {
  const theme = useTheme();

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        height: "100vh",
        margin: 0,
        padding: 0,
      }}
    >
      {/* 중앙과 우측 레이아웃 */}
      <div style={{ display: "flex", flexGrow: 1, margin: 0, padding: 0 }}>
        {/* 중앙 부분 */}
        <div
          style={{
            flexGrow: 2,
            padding: "20px",
            backgroundColor: theme.palette.background.default,
            margin: 0,
            borderTop: "2px solid #ccc", // 헤더-중앙 경계선
            borderRight: "2px solid #ccc", // 중앙-우측 경계선
          }}
        >
          {children} {/* 중앙에 전달된 children 컴포넌트를 렌더링 */}
        </div>

        {/* 우측 부분 */}
        {rightComponent && (
          <div
            style={{
              width: "400px",
              padding: "20px",
              backgroundColor: theme.palette.background.default,
              margin: 0,
              borderTop: "2px solid #ccc", // 헤더-우측 경계선
              borderLeft: "2px solid #ccc", // 중앙-우측 경계선
            }}
          >
            {rightComponent} {/* 우측에 전달된 컴포넌트 렌더링 */}
          </div>
        )}
      </div>
    </div>
  );
};

export default MainLayout;
