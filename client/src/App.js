import React, { useEffect } from "react";
import GlobalStyle from "./GlobalStyle";
import styled from "styled-components";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import Nav from "./components/layout/Nav/Nav";
import SignUpForm from "./components/layout/RegisterForm/SignUpForm";
import NewRecipe from "./pages/NewRecipe/NewRecipe";
import Home from "./pages/Home/Home";
import LogIn from "./pages/LogIn/LogIn";
import OAuth2RedirectHandler from "./components/layout/RegisterForm/OAuth2RedirectHandler";

function App() {
  return (
    <>
      {/* Global Styles는 Router 안, 컴포넌트 상단에 위치  */}
      <GlobalStyle />
      <BrowserRouter>
        <Nav />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<LogIn />} />
          <Route path="/signup" element={<SignUpForm />} />
          <Route path="/recipes/new" element={<NewRecipe />} />
          <Route path="/auth/redirect" element={<OAuth2RedirectHandler />} />
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
