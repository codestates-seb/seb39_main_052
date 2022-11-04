import React, { useState, useEffect } from "react";
import GlobalStyle from "./GlobalStyle";
import {
  BrowserRouter,
  Routes,
  Route,
  useParams,
  useLocation,
} from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import styled from "styled-components";

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

import { useRef } from "react";
import MenuTab from "./components/layout/MenuTab/MenuTab";
import AdminSearchBar from "./components/layout/Admin/AdminSearchBar";
import AdminPanel from "./components/layout/Admin/AdminPanel";
import CustomToast from "./components/common/CustomToast/CustomToast";

// 페이지 이동마다 스크롤이 상단에 올 수 있도록 하는 컴포넌트
import ScrollToTop from "./components/others/ScrollToTop";

function App() {
  const [isBottom, setIsBottom] = useState(false); // 스크롤이 끝까지 내려갔는지 여부를 담은 상태

  const dispatch = useDispatch(); //for redux dispatch
  // const effectedCalled = useRef(false); //useEffect 한번만 실행하려고

  // alert 대신 사용되는 toast 관련 상태
  const showToast = useSelector((state) => {
    return state.toast.showToast;
  });

  // Floating Action의 위치 조정을 위해 스크롤을 인식하는 함수
  useEffect(() => {
    const handleScroll = (e) => {
      const bottom =
        window.innerHeight + window.scrollY >= document.body.offsetHeight;
      if (bottom) {
        setIsBottom(true);
      } else {
        setIsBottom(false);
      }
    };

    window.addEventListener("scroll", handleScroll);

    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);

  //로그인 상태 가져와서 변수에 저장
  const isLoggedIn = useSelector((state) => {
    return state.user.isLoggedIn;
  });
  const userToken = useSelector((state) => {
    return state.user.userToken;
  });

  // useSelector((state) => {
  //   console.log("userSlice 전체상태?", state.user); //{isLoggedIn: false, userId: null, userEmail: null}
  // });

  //! Appjs에서 액세스토큰 재발급 요청하고 토큰을 바로 헤더에 줘도 새로고침했을때 다른페이지 (마이페이지, 냉장고 조회) 요청이 먼저 처리되서 400에러..
  const JWT_EXPIRY_TIME = 30 * 60 * 1000; //액세스 토큰 만료시간 30분을 밀리초로 표현
  // const JWT_EXPIRY_TIME = 60 * 60 * 1000; //1시간으로설정
  const onSilentRefresh = () => {
    axios
      .post("/api/auth/refresh")
      .then((response) => {
        const ACCESS_TOKEN = response.headers["access-token"]; //eyJ0eX.. 서버에서 response header에 싣어보내는 토큰값
        if (response.status === 200) {
          axios.defaults.headers.common[
            "Authorization"
          ] = `Bearer ${ACCESS_TOKEN}`; //요청헤더에 액세스 토큰 설정

          console.log(
            "토큰 재발급 서버에 요청 후 App.js에서 재발급된 ACCESS_TOKEN",
            ACCESS_TOKEN
          );
          //refesh로 새로받아온 액세스 토큰 리덕스에도 저장하기
          dispatch(setLoggedIn({ userToken: ACCESS_TOKEN }));
          //액세스토큰 만료되기 1분 전 로그인 연장
          setTimeout(onSilentRefresh, JWT_EXPIRY_TIME - 60000);
          // setTimeout(onSilentRefresh, 3000); //3초로 실험
        }
      })
      .catch((error) => console.log(error, "silent refresh 에러"));
  };

  // useEffect(() => {
  // if (effectedCalled.current) return; //이미 useEffect 실행되었다면 useEffect실행안하고 탈출
  // effectedCalled.current = true;

  if (isLoggedIn && userToken) {
    axios.defaults.headers.common["Authorization"] = `Bearer ${userToken}`; //요청헤더에 액세스 토큰 설정
    // console.log(axios.defaults.headers.common);
    console.log("이미있는 리덕스 userToken으로 헤더에 설정", userToken);
    //새로고침하면 이전에 로그인 요청보내놓은것도 상태가 다 날라가는데.. 액세스토큰이 만료되면 재발급 받게하는 onSilentRefresh 함수를 넣지않으면
    //새로고침 이후에는 이전에 가지고있는 토큰만 세션스토리지에 저장되어있고 토큰 재발급이 안되는듯..? 토큰 재발급 요청 보내기
    // setTimeout(onSilentRefresh, JWT_EXPIRY_TIME - 60000);
    // setTimeout(onSilentRefresh, 3000); //3초로 실험
    onSilentRefresh(); //새로고침하면 바로 액세스토큰 재발급하는 함수실행
  }
  // }, []);
  //useEffect 하면 모든 컴포넌트 렌더링 된 이후에 App.js가 실행

  return (
    <Div>
      <BrowserRouter>
        <GlobalStyle />
        <ScrollToTop />
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
          <Route path="/mypage/:id" element={<MyPage />} />
          <Route path="/admin" element={<AdminPanel />} />
        </Routes>
        {/* alert창 대신 */}
        {showToast && <CustomToast />}
        {/* 우측 하단에 항상 있는 레시피 작성하기 */}
        {/* <OskiFooter>Hi I'm Oscar's Footer</OskiFooter> */}
        <Footer />
        <FloatingAction isBottom={isBottom} />
      </BrowserRouter>
    </Div>
  );
}

const Div = styled.div`
  width: 100vw;
  height: fit-content;
  /* overflow-x: hidden; */ //gnb 상위요소인데 부모요소에 스크롤 제한을 걸어두면 gnb sticky가 안되어서..
`;

export default App;
