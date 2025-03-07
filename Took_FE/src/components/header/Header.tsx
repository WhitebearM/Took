import React, { useState } from "react";
import { FaGear, FaUser } from "react-icons/fa6";
import { FaBell } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { useTheme } from "@mui/material/styles";
import SearchBar from "../SearchBar";
import LocationInfo from "../LocationInfo";

const Header: React.FC = () => {
  const [openSettings, setOpenSettings] = useState(false);
  const [openNotifications, setOpenNotifications] = useState(false);
  const navigate = useNavigate();
  const theme = useTheme();

  return (
    <div
      style={{
        padding: "30px 10px",
        display: "flex",
        justifyContent: "space-between", // 왼쪽 검색창과 오른쪽 아이콘을 양쪽에 배치
        alignItems: "center",
      }}
    >
      {/* 검색창 */}
      <div style={{ width: "10%", margin: "0 0 0 100px" }}>
        <SearchBar /> {/* 검색창 컴포넌트 */}
      </div>
      {/* 위치 정보 */}
      <LocationInfo /> {/* You are now in 부분 */}
      {/* 우측 아이콘들 */}
      <div style={{ display: "flex", alignItems: "center" }}>
        {/* Settings 아이콘 */}
        <span
          onClick={() => setOpenSettings(true)}
          style={{
            cursor: "pointer",
            margin: "0 18px",
            fontSize: "1.25rem",
            color: theme.palette.primary.light,
          }}
        >
          <FaGear />
        </span>

        {/* Notifications 아이콘 */}
        <span
          onClick={() => setOpenNotifications(true)}
          style={{
            cursor: "pointer",
            margin: "0 18px",
            fontSize: "1.25rem",
            color: theme.palette.primary.light,
          }}
        >
          <FaBell />
        </span>

        {/* User 정보 아이콘 */}
        <span
          onClick={() => navigate("/mypage")}
          style={{
            cursor: "pointer",
            margin: "0 50px 0 18px",
            fontSize: "1.25rem",
            color: theme.palette.primary.light,
          }}
        >
          <FaUser />
        </span>
      </div>
    </div>
  );
};

export default Header;
