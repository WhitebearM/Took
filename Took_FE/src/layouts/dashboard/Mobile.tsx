import React, { useState } from "react";
import {
  Box,
  BottomNavigation,
  BottomNavigationAction,
  Paper,
  useTheme,
} from "@mui/material";
import { Outlet, useNavigate } from "react-router-dom";
import { Nav_Buttons } from "@/data";
import MobileHeader from "@/components/header/\bMobileHeader";

const MobileDashboardLayout = () => {
  const theme = useTheme(); // 테마 불러오기
  const [value, setValue] = useState(0); // 선택된 아이콘 상태
  const navigate = useNavigate();

  // Nav 버튼을 클릭하면 해당 경로로 이동
  const handleNavigation = (index: number, path: string) => {
    setValue(index);
    navigate(path);
  };

  return (
    <Box sx={{ display: "flex", flexDirection: "column", height: "100vh" }}>
      {/* 상단 헤더 */}
      <Box sx={{ padding: "60px 0 0 20px" }}>
        <MobileHeader />
      </Box>

      {/* 중앙 콘텐츠 (페이지들) */}
      <Box sx={{ flexGrow: 1, overflow: "auto", padding: 2 }}>
        <Outlet /> {/* 각 페이지 컴포넌트가 렌더링될 부분 */}
      </Box>

      {/* 하단 네비게이션 바 */}
      <Paper
        sx={{
          position: "fixed",
          bottom: 0,
          left: 0,
          right: 0,
          paddingTop: "5px",
          paddingBottom: "20px",
        }}
        elevation={3}
      >
        <BottomNavigation
          showLabels
          value={value}
          onChange={(event, newValue) =>
            handleNavigation(newValue, Nav_Buttons[newValue].path)
          }
        >
          {Nav_Buttons.map((el, index) => (
            <BottomNavigationAction
              key={index}
              icon={
                <Box
                  sx={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    fontSize: value === index ? "1.6rem" : "1.5rem", // 아이콘 크기 조정
                    color:
                      value === index
                        ? theme.palette.primary.main
                        : theme.palette.primary.light, // 클릭 여부에 따른 색상 적용
                  }}
                >
                  {el.icon}
                </Box>
              }
              sx={{
                minWidth: "auto",
                padding: "5px", // 아이콘 간격 조정(최대로 축소)
              }}
            />
          ))}
        </BottomNavigation>
      </Paper>
    </Box>
  );
};

export default MobileDashboardLayout;
