import React, { useState } from "react";
import { AppBar, Toolbar, IconButton, Drawer, Box } from "@mui/material";
import { FaBars } from "react-icons/fa6";
import SearchBar from "../SearchBar";
import { FaGear, FaBell, FaUser } from "react-icons/fa6";

const MobileHeader = () => {
  const [drawerOpen, setDrawerOpen] = useState(false);

  const toggleDrawer =
    (open: boolean) => (event: React.KeyboardEvent | React.MouseEvent) => {
      setDrawerOpen(open);
    };

  return (
    <>
      <AppBar position="static" color="transparent" elevation={0}>
        <Toolbar>
          {/* 파란색 메뉴 버튼 */}
          <IconButton edge="start" onClick={toggleDrawer(true)}>
            <FaBars color="#0095FF" />
          </IconButton>

          {/* 검색창 */}
          <Box sx={{ flexGrow: 1, ml: 1 }}>
            {/* 모바일에서는 width를 작게 설정 */}
            <SearchBar width="95%" />
          </Box>
        </Toolbar>
      </AppBar>

      {/* Drawer (Settings, Notifications, User정보) */}
      <Drawer anchor="left" open={drawerOpen} onClose={toggleDrawer(false)}>
        <Box sx={{ width: 250, padding: 2 }}>
          <IconButton>
            <FaGear />
          </IconButton>
          <IconButton>
            <FaBell />
          </IconButton>
          <IconButton>
            <FaUser />
          </IconButton>
        </Box>
      </Drawer>
    </>
  );
};

export default MobileHeader;
