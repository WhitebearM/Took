import React from "react";
import { Outlet } from "react-router-dom";

const MobileAuthLayout = () => {
  return (
    <div style={{ display: "flex", flexDirection: "column", height: "100vh" }}>
      <Outlet /> {/* auth 페이지 콘텐츠 */}
    </div>
  );
};

export default MobileAuthLayout;
