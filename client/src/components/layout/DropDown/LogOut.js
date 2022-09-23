//redux toolkit related
import axios from "axios";
import { useSelector, useDispatch } from "react-redux";
import { setLoginSuccess, setLoggedOut } from "../../../features/userSlice";

const LogOut = () => {
  const dispatch = useDispatch();

  //로그인 상태 가져와서 변수에 저장
  const isLoggedIn = useSelector((state) => {
    return state.user.isLoggedIn;
  });
  // console.log("로그아웃 함수 호출 이후 isLoggedIn이니?", isLoggedIn); //false 로 잘찍힘

  //userSlice 전체 상태 확인
  // useSelector((state) => {
  //   console.log("userSlice 전체상태?", state.user);
  // }); //{isLoggedIn: false, userId: null, userEmail: undefined}

  //로그아웃
  const handleLogOut = () => {
    axios
      .get("/api/logout")
      .then((res) => {
        if (res.status === 200) {
          delete axios.defaults.headers.common["Authorization"]; //헤더에 설정해둔 액세스 토큰 권한부여 제거
          dispatch(setLoggedOut()); //로그아웃 상태로 바꿔주는 함수 호출
          // console.log("axios then 안에있는 로그아웃 함수 호출 이후 isLoggedIn이니?",isLoggedIn); //왜 바로 콘솔로 찍어보면 로그아웃했는데도 로그인 상태 true로 남아있는지 ..?
          alert("로그아웃 완료");
        }
      })
      .catch((err) => alert(err));
  };

  return (
    <div style={{ cursor: "pointer" }} onClick={handleLogOut}>
      로그아웃
    </div> //우선 로그아웃 디브태그에 포인터 스타일 추가
  );
};

export default LogOut;
