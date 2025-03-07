import { FaHouse, FaUserGroup } from "react-icons/fa6";
import { FaPlusSquare, FaEnvelope, FaIcons } from "react-icons/fa";

// 사이드바에 들어갈 아이콘 리스트
const Nav_Buttons = [
  {
    index: 0,
    icon: <FaHouse />, // home(main)
    title: "main",
    path: "/main", // main 경로
  },
  {
    index: 1,
    icon: <FaPlusSquare />, // create contents
    title: "create",
    path: "/create", // create 경로
  },
  {
    index: 2,
    icon: <FaEnvelope />, // message
    title: "message",
    path: "/chat", // message 경로
  },
  {
    index: 3,
    icon: <FaIcons />, // explore contents
    title: "explore",
    path: "/explore", // explore 경로
  },
  {
    index: 4,
    icon: <FaUserGroup />, // community
    title: "community",
    path: "/community", // community 경로
  },
];

export { Nav_Buttons };
