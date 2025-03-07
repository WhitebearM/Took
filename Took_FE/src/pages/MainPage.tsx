import React from "react";
import BannerContainer from "@/components/container/BannerContainer";
import MainContainer from "@/components/container/MainContainer";
import RecommendedFriends from "@/components/rightsection/Recommend";
import MainLayout from "@/layouts/main";

const MainPage: React.FC = () => {
  return (
    <MainLayout rightComponent={<RecommendedFriends />}>
      <div
        style={{ height: "100vh", display: "flex", flexDirection: "column" }}
      >
        <BannerContainer title="Featured most" />
        <MainContainer title="Current Location" />
        <p>main page</p>
      </div>
    </MainLayout>
  );
};

export default MainPage;
