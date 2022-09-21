import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Home from "../../../pages/Home/Home";
import LogIn from "../../../pages/LogIn/LogIn";

//RedirectURI 경로에 도달했을때 보여줄 컴포넌트
const OAuth2RedirectHandler = () => {
  //   const [isSocialLoggedIn, setisSocialLoggedIn] = useState(false);
  const navigate = useNavigate();

  //구글 로그인해서 액세스 토큰 받아오면 액세스 토큰 로컬스토리지에 저장후 홈페이지로 가기
  let location = new URL(window.location.href);
  console.log("OAuth 리다이렉트 핸들러 location", location);
  const ACCESS_TOKEN_FOR_OAUTH = location.searchParams.get("access-token");
  console.log("ACCESS_TOKEN_FOR_OAUTH", ACCESS_TOKEN_FOR_OAUTH); //eyJ0eXAiOiJKV1QiLC...

  useEffect(() => {
    navigate("/");
    console.log("유즈이펙트");
  }, [ACCESS_TOKEN_FOR_OAUTH]); //navigate쓸때 useEffect 에서 쓰라고해서

  //로컬 스토리지에 저장
  //소셜로그인 토큰을 로컬스토리지에 저장하는 경우 소셜 로그인시 / 일반 로그인시 요청 보내는 코드를 분리해서 써야한다. 일반 로그인시 요청은 요청헤더에 토큰을 싣어보내지만 소셜로그인의 경우 지금 짜여있는 코드로 요청시 토큰이 없기때문에 401 Unauthorized
  //// if (ACCESS_TOKEN_FOR_OAUTH) {
  // localStorage.setItem("ACCESS_TOKEN_FOR_OAUTH", ACCESS_TOKEN_FOR_OAUTH);
  // console.log("로컬스토리지에 넣은 이후 액세스토큰", ACCESS_TOKEN_FOR_OAUTH);
  //요청헤더에 액세스 토큰 설정
  axios.defaults.common["Authorization"] = `Bearer ${ACCESS_TOKEN_FOR_OAUTH}`; //요청헤더에 액세스 토큰 설정
  console.log("요청헤더에 설정하고난후 액세스 토큰", ACCESS_TOKEN_FOR_OAUTH);

  //   setisSocialLoggedIn(true);
  //   } catch (error) {
  //     console.log(error, "OAuth 액세스토큰 받기 에러");
  //   } //try catch로 잘못넣으면 무한 요청;;

  return <div>OAuth Redirect page</div>;
};

export default OAuth2RedirectHandler;
