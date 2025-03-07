import { Suspense, lazy, ComponentType } from "react";
import { Navigate, useRoutes } from "react-router-dom";
import { useMediaQuery } from "@mui/material";

// layouts
import DashboardLayout from "../layouts/dashboard";
import AuthLayout from "../layouts/auth";
import MobileDashboardLayout from "../layouts/dashboard/Mobile"; // 모바일 dashboard layout
import MobileMainLayout from "../layouts/main/Mobile"; // 모바일 main layout
import MobileAuthLayout from "../layouts/auth/Mobile"; // 모바일 auth

// 컴포넌트 로딩 스크린
import LoadingScreen from "../components/LoadingScreen";

// Lazy 로딩 컴포넌트 설정
const Loadable = (Component: ComponentType<any>) => (props: any) => {
  return (
    <Suspense fallback={<LoadingScreen />}>
      <Component {...props} />
    </Suspense>
  );
};

export default function Router() {
  const isMobile = useMediaQuery("(max-width: 768px)"); // 모바일 여부 판별

  return useRoutes([
    {
      path: "/auth",
      element: isMobile ? <MobileAuthLayout /> : <AuthLayout />,
      children: [
        { path: "login", element: <LoginPage /> },
        { path: "register", element: <RegisterPage /> },
        { path: "find-id", element: <FindIdPage /> },
        { path: "find-password", element: <FindPasswordPage /> },
        { path: "reset-password", element: <ResetPasswordPage /> },
      ],
    },
    {
      path: "/",
      element: isMobile ? <MobileDashboardLayout /> : <DashboardLayout />,
      children: [
        { path: "main", element: <MainPage /> },
        { path: "create", element: <CreatePostPage /> },
        { path: "chat", element: <ChatPage /> },
        { path: "explore", element: <FullPostPage /> },
        { path: "community", element: <CommunityPage /> },
        { path: "404", element: <Page404 /> },
        { path: "*", element: <Navigate to="/404" replace /> },
      ],
    },
    { path: "*", element: <Navigate to="/404" replace /> },
  ]);
}

// 페이지 컴포넌트 로드 설정 (Lazy loading)
const MainPage = Loadable(lazy(() => import("../pages/MainPage")));
const Page404 = Loadable(lazy(() => import("../pages/Page404")));
const LoginPage = Loadable(lazy(() => import("../pages/auth/LoginPage")));
const RegisterPage = Loadable(lazy(() => import("../pages/auth/RegisterPage")));
const FindIdPage = Loadable(lazy(() => import("../pages/auth/FindIdPage")));
const FindPasswordPage = Loadable(
  lazy(() => import("../pages/auth/FindPasswordPage"))
);
const ResetPasswordPage = Loadable(
  lazy(() => import("../pages/auth/ResetPasswordPage"))
);
const ChatPage = Loadable(lazy(() => import("../pages/chat/ChatPage")));
const CreatePostPage = Loadable(
  lazy(() => import("../pages/create/CreatePost"))
);
const FullPostPage = Loadable(lazy(() => import("../pages/posts/FullPost")));
const CommunityPage = Loadable(
  lazy(() => import("../pages/community/CommunityPage"))
);
