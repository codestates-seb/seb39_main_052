import axios from "axios";
import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Home from "../../../pages/Home/Home";
import LogIn from "../../../pages/LogIn/LogIn";

//RedirectURI 경로에 도달했을때 보여줄 컴포넌트
const OAuth2RedirectHandler = () => {
  //   const [isSocialLoggedIn, setisSocialLoggedIn] = useState(false);
  const navigate = useNavigate();

  let location = new URL(window.location.href);
  console.log("오오쓰 리다이렉트 핸들러 location", location);
  const ACCESS_TOKEN_FOR_OAUTH = location.searchParams.get("access-token");
  console.log("ACCESS_TOKEN_FOR_OAUTH", ACCESS_TOKEN_FOR_OAUTH); //eyJ0eXAiOiJKV1QiLC...

  useEffect(() => {
    //   try {
    //구글 로그인해서 액세스 토큰 받아오면 액세스 토큰 로컬스토리지에 저장후 홈페이지로 가기
    navigate("/");
    console.log("유즈이펙트");
  }, [ACCESS_TOKEN_FOR_OAUTH]); //navigate쓸때 useEffect 에서 쓰라고해서

  // if (ACCESS_TOKEN_FOR_OAUTH) {
  localStorage.setItem("ACCESS_TOKEN_FOR_OAUTH", ACCESS_TOKEN_FOR_OAUTH);
  console.log("로컬스토리지 이후에", ACCESS_TOKEN_FOR_OAUTH);
  // navigate(<Home></Home>);
  // history.push

  //   <Link to="/"></Link>;
  //   setisSocialLoggedIn(true);
  // }
  //   } catch (error) {
  //     console.log(error, "OAuth 액세스토큰 받기 에러");
  //   } //try catch로 잘못넣으면 무한 요청;;

  //   isSocialLoggedIn ? <Link to="/"></Link> : <Link to="/login"></Link>;
  return <div>스피너 넣을것</div>;
};

export default OAuth2RedirectHandler;
