//redux toolkit related
import axios from "axios";
import { useSelector, useDispatch } from "react-redux";
import { persistor } from "../../..";
import { setLoggedIn, setLoggedOut } from "../../../features/userSlice";

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

  //store purge 세션스토리지에 저장된 로그인 상태 모두 삭제
  const purge = async () => {
    // location.reload();
    await persistor.purge(); // persistStore의 데이터 전부 날림
  };

  //로그아웃
  const handleLogOut = () => {
    axios
      .get("/api/logout")
      .then((res) => {
        if (res.status === 200) {
          delete axios.defaults.headers.common["Authorization"]; //헤더에 설정해둔 액세스 토큰 권한부여 제거
          dispatch(setLoggedOut()); //로그아웃 상태로 바꿔주는 함수 호출
          purge(); //persistor 세션스토리지에 저장되어있는 로그인 상태 데이터 날려버리기
          alert("로그아웃 완료!");
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
