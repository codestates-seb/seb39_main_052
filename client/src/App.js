import React from "react";
import GlobalStyle from "./GlobalStyle";
import styled from "styled-components";
import "./style.css";
import LogInForm from "./components/layout/LogInForm";
import SignUpForm from "./components/layout/SignUpForm";

function App() {
  return (
    <>
      {/* Global Styles는 Router 안, 컴포넌트 상단에 위치  */}
      <GlobalStyle />
      {/* <Test> 나만의 냉장고 </Test>
      <div> My Fridge </div> */}
      {/* <LogInForm></LogInForm> */}
      <SignUpForm></SignUpForm>
    </>
  );
}

const Test = styled.div`
  color: var(--red-500);
  box-shadow: var(--shadow-high);
`;

export default App;
