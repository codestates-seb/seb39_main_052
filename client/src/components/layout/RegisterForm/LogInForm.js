import axios from "axios";
import { useForm } from "react-hook-form";
import GeneralButton from "../../common/Button/GeneralButton";
import { LogInFormContainer, SignUpDiv } from "./LogInFormStyle";
import { Link, useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import {
  setIsAdmin,
  setLoggedIn,
  setUserInfo,
} from "../../../features/userSlice";
import { setWarningToast, setNoticeToast } from "../../../features/toastSlice";

const LogInForm = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch(); //for redux dispatch
  const {
    register,
    handleSubmit,
    formState: { isSubmitting, errors, isValid },
  } = useForm({ mode: "onChange" });
  //isValid: errors 객체 비어있으면 true
  //isDirty: form 양식 어떤 input이라도 건드렸으면 true?

  //userSlice 전체 상태 확인 - 콘솔 확인용
  useSelector((state) => {
    console.log("userSlice 전체상태?", state.user); //{isLoggedIn: false, userId: null, userEmail: null}
  });

  // //userSlice에서 가져오는 유저아이디
  // const userId = useSelector((state) => {
  //   console.log("리덕스 userId?", state.user.userId);
  //   return state.user.userId;
  // }); //axios 로그인 요청할때 리덕스에 저장하는 유저아이디는(state로 저장하는 상태도 마찬가지) 다음 사용자 정보 조회 get요청에 바로 쓸수 없다. 비동기로 작동해서 그런듯..?

  //userSlice 로그인 상태 확인
  const isLoggedIn = useSelector((state) => {
    // console.log("리덕스 isLoggedIn이니?", state.user.isLoggedIn);
    return state.user.isLoggedIn;
  });

  const JWT_EXPIRY_TIME = 30 * 60 * 1000; //액세스 토큰 만료시간 30분을 밀리초로 표현
  // const JWT_EXPIRY_TIME = 60 * 60 * 1000; //1시간으로설정

  const onSubmit = (data, event) => {
    // console.log(data); //{email: 'test1@email.com', password: 'aaaa1111'}

    console.log("이벤트서미터 이름", event.nativeEvent.submitter.name);
    const login = async () => {
      let response;
      try {
        //일반 로그인
        if (event.nativeEvent.submitter.name === "normal") {
          response = await axios.post("/api/login", data);
          console.log("일반로그인 리스폰스", response);
        }
        //게스트 로그인
        else if (event.nativeEvent.submitter.name === "guest") {
          response = await axios.post("/api/login/guest");
          console.log("게스트로그인 리스폰스", response);
        }
        //일반 or 게스트이던 post 요청해서 응답 성공하면 실행되는 코드
        {
          if (response.status === 200) {
            const ACCESS_TOKEN = response.headers["access-token"]; //eyJ0eX.. 서버에서 response header에 싣어보내는 토큰값
            axios.defaults.headers.common[
              "Authorization"
            ] = `Bearer ${ACCESS_TOKEN}`; //요청헤더에 액세스 토큰 설정
            console.log("ACCESS_TOKEN", ACCESS_TOKEN);
            //userSlice에 로그인 상태 true 저장하고 액세스 토큰도 저장
            dispatch(setLoggedIn({ userToken: ACCESS_TOKEN }));

            //서버에서 받아오는 memberId를 가지고 사용자 정보조회하는 API로 get요청
            getUserInfo(response.data.memberId);

            // alert창 대체
            dispatch(setNoticeToast({ message: `로그인 성공` }));
            navigate("/");
            //액세스토큰 만료되기 전 로그인 연장
            setTimeout(onSilentRefresh, JWT_EXPIRY_TIME - 60000);
            // setTimeout(onSilentRefresh, 3000); //3초로 실험
          }
        }
      } catch (error) {
        console.log(error);
        if (error.response.status === 401) {
          //일반 로그인 실패시
          if (event.nativeEvent.submitter.name === "normal") {
            // alert창 대체
            dispatch(
              setWarningToast({
                message: `아이디 혹은 비밀번호가 일치하지 않아요\n`,
              })
            );
          }
          //게스트 로그인 실패시
          else if (event.nativeEvent.submitter.name === "guest") {
            // alert창 대체
            dispatch(
              setWarningToast({ message: `게스트로 로그인할 수 없어요ㅠㅠ\n` })
            );
          }
        } else {
          // alert창 대체
          dispatch(setWarningToast({ message: error.message }));
        }
      }
    };
    login();

    //로그인 만료전 연장
    const onSilentRefresh = () => {
      axios
        .post("/api/auth/refresh")
        .then((response) => {
          const ACCESS_TOKEN = response.headers["access-token"]; //eyJ0eX.. 서버에서 response header에 싣어보내는 토큰값
          if (response.status === 200) {
            axios.defaults.headers.common[
              "Authorization"
            ] = `Bearer ${ACCESS_TOKEN}`; //요청헤더에 액세스 토큰 설정

            console.log("ACCESS_TOKEN 재발급", ACCESS_TOKEN);

            //refesh로 새로받아온 액세스 토큰 리덕스에도 저장하기
            dispatch(setLoggedIn({ userToken: ACCESS_TOKEN }));
            //액세스토큰 만료되기 1분 전 로그인 연장
            setTimeout(onSilentRefresh, JWT_EXPIRY_TIME - 60000);
            // setTimeout(onSilentRefresh, 3000); //3초로 실험
          }
        })
        .catch((error) => console.log(error, "silent refresh 에러"));
    };

    //기존 일반 게스트 로그인 분기 나누기 이전 일반 로그인 코드
    //=======================
    //로그인 요청. 액세스 토큰을 요청헤더에 설정
    // axios
    //   .post("/api/login", data)
    //   .then((response) => {
    //     // console.log(response); //{data: {…}, status: 200, statusText: 'OK', headers: {…}, config: {…}, …}
    //     const ACCESS_TOKEN = response.headers["access-token"]; //eyJ0eX.. 서버에서 response header에 싣어보내는 토큰값
    //     if (response.status === 200) {
    //       axios.defaults.headers.common[
    //         "Authorization"
    //       ] = `Bearer ${ACCESS_TOKEN}`; //요청헤더에 액세스 토큰 설정
    //       console.log("ACCESS_TOKEN", ACCESS_TOKEN);
    //       //로그인 성공 상태 리덕스 저장소로 보내기
    //       // dispatch(setLoggedIn({ userEmail: data.email }));
    //       // console.log(response.data); //서버에서 응답바디로 주는것 {memberId: 2}

    //       //userSlice에 로그인 상태 true 저장
    //       dispatch(setLoggedIn({})); //{isLoggedIn: true, userId: null, userName: null, userProfileImgPath: null}

    //       //액세스 토큰 리덕스에도 저장하기
    //       dispatch(setLoggedIn({ userToken: ACCESS_TOKEN }));

    //       //서버에서 받아오는 memberId를 가지고 사용자 정보조회하는 API로 get요청
    //       getUserInfo(response.data.memberId);

    //       // alert창 대체
    //       dispatch(setNoticeToast({ message: `로그인 성공` }));
    //       navigate("/");
    //       //액세스토큰 만료되기 전 로그인 연장
    //       setTimeout(onSilentRefresh, JWT_EXPIRY_TIME - 60000);
    //       // setTimeout(onSilentRefresh, 3000); //3초로 실험
    //     }
    //   })
    //   .catch((error) => {
    //     console.log(error);
    //     if (error.response.status === 401) {
    //       // alert창 대체
    //       dispatch(
    //         setWarningToast({
    //           message: `아이디 혹은 비밀번호가 일치하지 않아요\n`,
    //         })
    //       );
    //     } else {
    //       // alert창 대체
    //       dispatch(setWarningToast({ message: error.message }));
    //     }
    //   });
    //=============================

    // refresh 토큰없이 액세스 토큰만 받아오는 기존코드
    // accessToken을 로컬스토리지, 쿠키에 저장하지 않고 API 요청하는 콜마다 request 헤더에 담아 보내주는 방법. 요청헤더에 토큰 안담기는 문제 해결!
    // axios
    //   .post("/api/login", data)
    //   .then((response) => {
    //     console.log(response);
    //     const ACCESS_TOKEN = response.headers["access-token"]; //eyJ0eX.. 서버에서 response header에 싣어보내는 토큰값
    //     if (response.status === 200) {
    //       axios.defaults.headers.common[
    //         "Authorization"
    //       ] = `Bearer ${ACCESS_TOKEN}`; //요청헤더에 액세스 토큰 설정
    //       console.log("ACCESS_TOKEN", ACCESS_TOKEN); //
    //       setIsLoggedIn(true);
    //       alert("로그인 성공");
    //       // navigate("/");
    //     }
    //   })
  }; //onSubmit 함수끝

  //로그인 요청시 서버에서 보내주는 memberId로 사용자 정보를 조회
  const getUserInfo = (userIdFromServer) => {
    // console.log("겟유저인포 ");
    axios
      // .get(`/api/members/${userIdFromServer}}`)
      .get("/api/members/" + userIdFromServer)
      .then((response) => {
        // console.log(response);
        if (response.status === 200) {
          console.log("겟유저인포 response", response); //response.data = {memberId: 2, name: 'test1', profileImagePath: null}
          //관리자 권한이면
          if (response.data.roles[0] === "ROLE_ADMIN") {
            dispatch(setIsAdmin({})); //관리자 상태 true 추가
          }
          //그냥 로그인이면..
          //요청시 받아오는 response.data로 리덕스에 유저정보 저장
          else {
            dispatch(
              setUserInfo({
                userId: response.data.id,
                userName: response.data.name,
                userProfileImgPath: response.data.profileImagePath,
              })
            );
          }
        }
      })
      .catch((error) => console.log("겟유저인포 fail", error));
  };

  //로그인 상태 true일떄만 유저 정보 불러오기 - 안됨
  // useEffect(() => {
  // if (isLoggedIn) {
  //   getUserInfo();
  //   console.log(memberId);
  // }
  //   console.log(isLoggedIn);
  // }, [isLoggedIn]);

  return (
    <LogInFormContainer>
      <form onSubmit={handleSubmit(onSubmit)}>
        <label>이메일</label>

        <input
          id="email"
          type="email"
          name="email"
          placeholder="이메일을 입력해주세요"
          {...register("email", {
            required: true,
            pattern: {
              value: /\S+@\S+\.\S+/,
              message: "올바른 이메일 형식이 아니에요",
            },
          })}
        ></input>
        {errors.email && <span>{errors.email.message}</span>}
        <label>비밀번호</label>
        <input
          id="password"
          type="password"
          name="password"
          placeholder="비밀번호를 입력해주세요"
          {...register("password", {
            required: true,
            minLength: {
              value: 8,
              message: "비밀번호는 8자 이상이에요",
            },
          })}
        ></input>
        {errors.password && <span>{errors.password.message}</span>}
        <GeneralButton
          name="normal"
          // className={!(isValid && isDirty) ? "disabled-btn" : ""} //isDirty 는 input 전체 클릭하기만 해도 true가 되어서 빈문자열일때도 disabled해제
          disabled={!isValid || isSubmitting}
          // className={!isValid ? "disabled-btn" : ""}
          // className={!isValid && "disabled-btn"} //class로 해도 똑같이 작동
        >
          로그인
        </GeneralButton>
        <GeneralButton
          name="guest"
          // onClick={onSubmitGuest}
          // color={`var( --gray-600)`}
          backgroundColor="var(--mint-500)"
          hoverBackgroundColor={"var(--mint-600)"}
        >
          게스트 로그인
        </GeneralButton>
      </form>

      <SignUpDiv>
        {/* <span>오늘 뭐먹을지 고민중인가요?</span>
        <span>
          <Link to="/signup">회원가입</Link>
        </span> */}
        <span>오늘 뭐먹을지 고민중인가요?</span>
        <GeneralButton
          color={`var( --gray-600)`}
          backgroundColor={"var(--red-075)"}
          hoverBackgroundColor={"var(--red-150)"}
        >
          <Link to="/signup">회원가입</Link>
        </GeneralButton>
      </SignUpDiv>
    </LogInFormContainer>
  );
};

export default LogInForm;
