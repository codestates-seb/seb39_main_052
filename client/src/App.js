import React from "react";
import GlobalStyle from "./GlobalStyle";
import styled from "styled-components";
import "./style.css";
import LogInForm from "./components/layout/RegisterForm/LogInForm";
import SignUpForm from "./components/layout/RegisterForm/SignUpForm";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home/Home";
import LogIn from "./pages/LogIn/LogIn";

function App() {
  return (
    <>
      {/* Global Styles는 Router 안, 컴포넌트 상단에 위치  */}
      <GlobalStyle />
      <BrowserRouter>
        {/* <Test> 나만의 냉장고 </Test>
      <div> My Fridge </div> */}
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<LogIn />} />
          <Route path="/signup" element={<SignUpForm />} />
        </Routes>
      </BrowserRouter>
    </>
  );
}

const Test = styled.div`
  color: var(--red-500);
  box-shadow: var(--shadow-high);
`;

export default App;
