import React, { useEffect } from "react";
import GlobalStyle from "./GlobalStyle";
import { BrowserRouter, Routes, Route, useParams } from "react-router-dom";
import { useSelector } from "react-redux";

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
import MyFridge from "./pages/MyFridge/MyFridge";
import MyPage from "./pages/MyPage/MyPage";
import axios from "axios";

function App() {

  //로그인 상태 가져와서 변수에 저장
  const isLoggedIn = useSelector((state) => {
    return state.user.isLoggedIn;
  });
  const userToken = useSelector((state) => {
    return state.user.userToken;
  });

  useEffect(() => {
    if (isLoggedIn && userToken) {
      axios.defaults.headers.common["Authorization"] = `Bearer ${userToken}`; //요청헤더에 액세스 토큰 설정
      console.log("리덕스 userToken으로 헤더에 설정", userToken);
    }
  }, []);

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
          <Route path="/myfridge" element={<MyFridge />} />
          <Route path="/mypage" element={<MyPage />} />
        </Routes>
        {/* <FloatingAction /> */}
      </BrowserRouter>
    </>
  );
}

export default App;
