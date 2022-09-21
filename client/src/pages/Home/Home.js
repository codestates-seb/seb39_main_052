import axios from "axios";
import { useEffect } from "react";

const Home = () => {
  //로그인시 요청헤더에 설정한 액세스토큰이 다른 요청시 잘 불러와지는지 테스트
  // useEffect(() => {
  //   axios.post("/api/recipes", {}).then((res) => console.log(res));
  // });
  return <div>홈페이지</div>;
};

export default Home;
