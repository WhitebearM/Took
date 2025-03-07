import React from "react";
import { Box, Typography } from "@mui/material";

interface MainContainerProps {
  title: string; // 제목을 props로 받을 수 있게 정의
  width?: string | number; // width를 props로 받을 수 있게 정의
  margin?: string | number; // margin을 props로 받을 수 있게 정의
}

const MainContainer: React.FC<MainContainerProps> = ({
  title,
  width = "970px",
  margin = "20px 90px",
}) => {
  return (
    <Box
      sx={{
        backgroundColor: "#fff",
        // boxShadow: "0 2px 10px rgba(0, 0, 0, 0.1)",
        borderRadius: "12px",
        padding: "20px",
        margin, // 여기서 margin을 props로 받음
        width, // 여기서 width를 props로 받음
        flexGrow: 1, // 남는 공간을 차지하도록 설정
        display: "flex",
        flexDirection: "column",
      }}
    >
      {/* 제목 영역 */}
      <Box sx={{ display: "flex", justifyContent: "space-between", mb: 2 }}>
        <Typography
          variant="h2"
          fontWeight="bold"
          sx={{
            color: "#000",
            marginTop: "5px",
            marginLeft: "10px",
          }}
        >
          {title} {/* 기능별 title */}
        </Typography>
        <Typography
          variant="body1"
          sx={{ color: "#0095FF", cursor: "pointer" }}
        ></Typography>
      </Box>
      <Box sx={{ padding: "10px 0", flexGrow: 1 }}>
        {/* 이 부분에 컨텐츠 삽입*/}
      </Box>
    </Box>
  );
};

export default MainContainer;
