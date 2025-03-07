import React from "react";
import BannerContainer from "@/components/container/BannerContainer";
import MainContainer from "@/components/container/MainContainer";

const FullPost: React.FC = () => {
  return (
    <div style={{ height: "100vh", display: "flex", flexDirection: "column" }}>
      <BannerContainer title="Featured most" />

      <MainContainer title="Following Contents" />
      <p>explore page</p>
    </div>
  );
};

export default FullPost;
