import React from "react";
import { BrowserRouter as Router } from "react-router-dom";
import RouterConfig from "@/routes";

const App = () => {
  return (
    <Router>
      <RouterConfig /> {/* 모든 라우트 설정을 가져옴 */}
    </Router>
  );
};

export default App;
