import React from 'react';
import GlobalStyle from './GlobalStyle';
import styled from 'styled-components';
import { BrowserRouter, Routes, Route } from "react-router-dom";

import Nav from './components/layout/Nav/Nav';
import LogInForm from "./components/layout/RegisterForm/LogInForm";
import SignUpForm from "./components/layout/RegisterForm/SignUpForm";
import NewRecipe from './pages/NewRecipe/NewRecipe';
import Home from "./pages/Home/Home";
import LogIn from "./pages/LogIn/LogIn";

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
        <RecipeEditor />
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
