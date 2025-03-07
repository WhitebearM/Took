import React from "react";
import { Outlet } from "react-router-dom";
import { Box } from "@mui/material";

const MobileMainLayout = () => {
  return (
    <Box sx={{ flexGrow: 1, padding: "20px" }}>
      <Outlet /> {/* 페이지 컴포넌트가 렌더링될 부분 */}
    </Box>
  );
};

export default MobileMainLayout;
