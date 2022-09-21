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

  //로그아웃
  const handleLogOut = () => {
    axios
      .get("/api/logout")
      .then(() => {
        delete axios.defaults.headers.common["Authorization"]; //헤더에 설정해둔 액세스 토큰 권한부여 제거
        dispatch(setLoggedOut({})); //로그아웃 상태로 바꿔주는 함수 호출
        console.log("isLoggedIn이니?", isLoggedIn);
      })
      .catch((err) => alert(err));
  };

  return <div onClick={handleLogOut}>로그아웃</div>;
};

export default LogOut;
