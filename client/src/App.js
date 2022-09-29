import React, { useEffect, useRef } from "react";
import GlobalStyle from "./GlobalStyle";
import styled from "styled-components";
import { BrowserRouter, Routes, Route, useParams } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { setLoggedIn, setUserInfo } from "./features/userSlice";

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
  //====cookie로 할때======
  // const [cookies, setCookie] = useCookies(["token", "id"]);
  // const dispatch = useDispatch();
  // const JWT_EXPIRY_TIME = 30 * 60 * 1000;
  // const mountRef = useRef(false); // useEffect 두번 mount 되는걸 방지하기 위해

  // 새로고침시 쿠키에 access-token이 있는 경우 token refresh 요청
  // useEffect(() => {
  //   if (cookies.token && !mountRef.current) {
  //     axios.defaults.headers.common["Authorization"] = cookies.token;
  //     onSilentRefresh();
  //   }
  //   return () => {mountRef.current = true;}
  // }, []);

  // const onSilentRefresh = async() => {
  //   await axios({
  //     method: "post",
  //     url: '/api/auth/refresh',
  //   })
  //   .then((response) => {
  //     console.log(response);
  //     const ACCESS_TOKEN = response.headers["access-token"]; // eyJ0eX.. 서버에서 response header에 싣어보내는 업데이트된 토큰값
  //     if (response.status === 200) {
  //       axios.defaults.headers.common["Authorization"] = `Bearer ${ACCESS_TOKEN}`; // 요청헤더에 액세스 토큰 설정
  //       setCookie("token", ACCESS_TOKEN);
  //       setTimeout(onSilentRefresh, JWT_EXPIRY_TIME - 60000); // 액세스토큰 만료되기 1분 전 로그인 연장
  //       dispatch(setLoggedIn({}));
  //       getUserInfo(cookies.id);
  //     }
  //   })
  //     .catch((error) => console.log(error.response))
  // };

  // //로그인 요청시 서버에서 보내주는 memberId로 사용자 정보를 조회
  // const getUserInfo = async (userId) => {
  //   await axios
  //     .get("/api/members/" + userId)
  //     .then((response) => {
  //       if (response.status === 200) {
  //         dispatch(
  //           setUserInfo({
  //             userId: response.data.id,
  //             userName: response.data.name,
  //             userProfileImgPath: response.data.profileImagePath,
  //           })
  //         );
  //       }
  //     })
  //     .catch((error) => console.log(error.response));
  // };
  //====cookie로 할때======

  //새로고침시에 로그인 true이면서 userToken 정보 있을때만 response 헤더에 가지고있는 userToken 정보로 액세스토큰 설정하기
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
