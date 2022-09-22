import React, { useEffect } from "react";
import GlobalStyle from "./GlobalStyle";
import styled from "styled-components";
import { BrowserRouter, Routes, Route, useParams } from "react-router-dom";

import Nav from "./components/layout/Nav/Nav";
import SignUpForm from "./components/layout/RegisterForm/SignUpForm";
import NewRecipe from "./pages/NewRecipe/NewRecipe";
import Home from "./pages/Home/Home";
import LogIn from "./pages/LogIn/LogIn";
import OAuth2RedirectHandler from "./components/layout/RegisterForm/OAuth2RedirectHandler";
import EditRecipe from "./pages/EditRecipe/EditRecipe";
import RecipeDetail from "./pages/RecipeDetail/RecipeDetail";
import FloatingAction from "./components/layout/FloatingAction/FloatingAction";
import Gnb from "./components/common/GNB/Gnb";
import FridgeDigging from "./pages/FridgeDigging/FridgeDigging";

function App() {

  return (
    <>
      <BrowserRouter>
        <GlobalStyle />
        <Nav />
        <Gnb />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<LogIn />} />
          <Route path="/signup" element={<SignUpForm />} />
          <Route path="/recipes/new" element={<NewRecipe />} />
          <Route path="/auth/redirect" element={<OAuth2RedirectHandler />} />
          <Route path="/recipes/edit" element={<EditRecipe />} /> 
          <Route path="/recipes/:id" element={<RecipeDetail />} />
          <Route path="/search" element={<FridgeDigging />} />
        </Routes>
        <FloatingAction />
      </BrowserRouter>
    </>
  );
}

export default App;
