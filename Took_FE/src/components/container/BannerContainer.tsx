import React from "react";
import { Box, Typography } from "@mui/material";

interface BannerContainerProps {
  title: string; // 제목을 props로 받을 수 있게 정의
}

const BannerContainer: React.FC<BannerContainerProps> = ({ title }) => {
  return (
    <Box
      sx={{
        backgroundColor: "#fff",
        // boxShadow: "0 2px 10px rgba(0, 0, 0, 0.1)",
        borderRadius: "15px",
        padding: "20px",
        marginTop: "20px",
        marginLeft: "90px",
        width: "970px",
        height: "230px",
      }}
    >
      {/* 제목 영역 */}
      <Box sx={{ display: "flex", justifyContent: "space-between", mb: 2 }}>
        <Typography
          variant="h2"
          fontWeight="bold"
          sx={{ color: "#000", marginTop: "5px", marginLeft: "10px" }}
        >
          {title} {/* 기능별 title */}
        </Typography>
        <Typography
          variant="body1"
          sx={{ color: "#0095FF", cursor: "pointer" }}
        ></Typography>
      </Box>
      {/* 배너 콘텐츠를 넣을 부분 */}
    </Box>
  );
};

export default BannerContainer;
