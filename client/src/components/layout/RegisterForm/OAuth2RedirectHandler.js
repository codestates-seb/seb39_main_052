import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import { useSelector, useDispatch } from "react-redux";
import { setLoginSuccess } from "../../../features/userSlice";

//RedirectURI 경로에 도달했을때 보여줄 컴포넌트
const OAuth2RedirectHandler = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch(); //for redux dispatch

  //redirect 페이지에서 query parameter로 액세스 토큰 받아오면 액세스 토큰은 요청헤더에 설정, 로그인 상태는 리덕스에 저장 후 home으로 보냄
  let location = new URL(window.location.href);
  // console.log("OAuth 리다이렉트 핸들러 location", location);
  const ACCESS_TOKEN_FOR_OAUTH = location.searchParams.get("access-token");
  // console.log("ACCESS_TOKEN_FOR_OAUTH", ACCESS_TOKEN_FOR_OAUTH); //eyJ0eXAiOiJKV1QiLC...
  //소셜로그인의 경우 query param에 memberId 정보를 추가할 것

  useEffect(() => {
    //if 액세스토큰 있으면
    //navigate되기 이전에 리덕스에 상태 저장
    //없으면 alert 로 에러 띄우고 로그인 페이지에 남아있게 분기
    if (ACCESS_TOKEN_FOR_OAUTH) {
      //로그인 성공 상태 리덕스 저장소로 보내기
      dispatch(setLoginSuccess({}));
      //유저id 싣어서 보내기
      navigate("/");
    } else {
      console.log("OAuth 액세스 토큰 없음");
      navigate("/login");
    }
    console.log("유즈이펙트");
  }, [ACCESS_TOKEN_FOR_OAUTH]); //navigate쓸때 useEffect 에서 쓰라고해서
  //액세스토큰값이 있으면 재렌더링

  //로컬 스토리지에 저장
  //소셜로그인 토큰을 로컬스토리지에 저장하는 경우 소셜 로그인시 / 일반 로그인시 요청 보내는 코드를 분리해서 써야한다. 일반 로그인시 요청은 요청헤더에 토큰을 싣어보내지만 소셜로그인의 경우 지금 짜여있는 코드로 요청시 토큰이 없기때문에 401 Unauthorized
  //// if (ACCESS_TOKEN_FOR_OAUTH) {
  // localStorage.setItem("ACCESS_TOKEN_FOR_OAUTH", ACCESS_TOKEN_FOR_OAUTH);
  // console.log("로컬스토리지에 넣은 이후 액세스토큰", ACCESS_TOKEN_FOR_OAUTH);

  //요청헤더에 액세스 토큰 설정
  axios.defaults.headers.common[
    "Authorization"
  ] = `Bearer ${ACCESS_TOKEN_FOR_OAUTH}`; //요청헤더에 액세스 토큰 설정
  console.log("요청헤더에 설정하고난후 액세스 토큰", ACCESS_TOKEN_FOR_OAUTH);

  // //로그인 성공 상태 리덕스 저장소로 보내기
  // dispatch(setLoginSuccess({}));
  // //유저id 싣어서 보내기

  //로그인 상태 확인용
  useSelector((state) => {
    console.log("isLoggedIn이니?", state.user.isLoggedIn);
  });

  //   setisSocialLoggedIn(true);
  //   } catch (error) {
  //     console.log(error, "OAuth 액세스토큰 받기 에러");
  //   } //try catch로 잘못넣으면 무한 요청;;

  return <div>OAuth Redirect page</div>;
};

export default OAuth2RedirectHandler;
