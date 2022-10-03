import React, { useEffect } from "react";
import GlobalStyle from "./GlobalStyle";
import { BrowserRouter, Routes, Route, useParams, useLocation } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";

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
import { setLoggedIn } from "./features/userSlice";
import Footer from "./components/layout/Footer/Footer";

function App() {
  const dispatch = useDispatch(); //for redux dispatch
  //로그인 상태 가져와서 변수에 저장
  const isLoggedIn = useSelector((state) => {
    return state.user.isLoggedIn;
  });
  const userToken = useSelector((state) => {
    return state.user.userToken;
  });

  useSelector((state) => {
    console.log("userSlice 전체상태?", state.user); //{isLoggedIn: false, userId: null, userEmail: null}
  });

  const JWT_EXPIRY_TIME = 30 * 60 * 1000; //액세스 토큰 만료시간 30분을 밀리초로 표현

  const onSilentRefresh = () => {
    axios
      .post("/api/auth/refresh")
      .then((response) => {
        const ACCESS_TOKEN = response.headers["access-token"]; //eyJ0eX.. 서버에서 response header에 싣어보내는 토큰값
        if (response.status === 200) {
          axios.defaults.headers.common[
            "Authorization"
          ] = `Bearer ${ACCESS_TOKEN}`; //요청헤더에 액세스 토큰 설정

          // console.log("App.js에서 재발급하는 ACCESS_TOKEN", ACCESS_TOKEN);

          //refesh로 새로받아온 액세스 토큰 리덕스에도 저장하기
          dispatch(setLoggedIn({ userToken: ACCESS_TOKEN }));
          //액세스토큰 만료되기 1분 전 로그인 연장
          setTimeout(onSilentRefresh, JWT_EXPIRY_TIME - 60000);
          // setTimeout(onSilentRefresh, 3000); //3초로 실험
        }
      })
      .catch((error) => console.log(error, "silent refresh 에러"));
  };

  useEffect(() => {
    axios.defaults.headers.common["Authorization"] = `Bearer ${userToken}`; //요청헤더에 액세스 토큰 설정
    // console.log("가지고있는 리덕스 userToken으로 헤더에 설정", userToken);

    if (isLoggedIn && userToken) {
      //새로고침하면 이전에 로그인 요청보내놓은것도 상태가 다 날라가는데.. 액세스토큰이 만료되면 재발급 받게하는 onSilentRefresh 함수를 넣지않으면
      //새로고침 이후에는 이전에 가지고있는 토큰만 세션스토리지에 저장되어있고 토큰 재발급이 안되는듯..? 토큰 재발급 요청 보내기
      // setTimeout(onSilentRefresh, JWT_EXPIRY_TIME - 60000);
      // setTimeout(onSilentRefresh, 3000); //3초로 실험
      onSilentRefresh(); //새로고침하면 바로 액세스토큰 재발급하는 함수실행..?
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
      <Footer />
    </>
  );
}

export default App;
