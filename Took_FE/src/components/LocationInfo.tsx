import React from "react";
import { Box, Typography } from "@mui/material";
import { FaMapLocationDot } from "react-icons/fa6"; // 위치 아이콘

const LocationInfo = () => {
  const city = "Seoul"; // 도시 (일단 하드코딩)
  const country = "Korea"; // 나라 (일단 하드코딩)

  return (
    <Box
      display="flex"
      alignItems="center"
      sx={{
        marginLeft: "20px",
      }}
    >
      {/* 아이콘을 크게 설정 */}
      <FaMapLocationDot
        style={{ color: "#0095FF", marginRight: "18px", fontSize: "2.7rem" }}
      />

      {/* 텍스트 두 줄로 나누어 표시 */}
      <Box>
        {/* 첫 번째 줄 - You are now in */}
        <Typography
          variant="body1"
          sx={{ fontWeight: "bold", fontSize: "0.9rem" }}
        >
          You are now in
        </Typography>

        {/* 두 번째 줄 - [도시], [나라] */}
        <Typography
          variant="body1"
          sx={{ fontWeight: "bold", fontSize: "1.1rem", color: "#000000" }}
        >
          {city}, {country}
        </Typography>
      </Box>
    </Box>
  );
};

export default LocationInfo;
