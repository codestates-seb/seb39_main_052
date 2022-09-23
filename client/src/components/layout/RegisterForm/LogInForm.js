import axios from "axios";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import GeneralButton from "../../common/Button/GeneralButton";
import { LogInFormContainer } from "./LogInFormStyle";
import { useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { setLoginSuccess } from "../../../features/userSlice";

const LogInForm = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch(); //for redux dispatch
  const {
    register,
    handleSubmit,
    formState: { isSubmitting, errors, isDirty, isValid },
  } = useForm();
  //isValid: errors 객체 비어있으면 true
  //isDirty: form 양식 어떤 input이라도 건드렸으면 true?

  const [memberId, setMemberId] = useState(0); //처음 로그인 요청시에 서버에서 받아오는 아이디는 memberId로 저장

  //userSlice 전체 상태 확인
  useSelector((state) => {
    console.log("userSlice 전체상태?", state.user); //{isLoggedIn: false, userId: null, userEmail: null}
  });

  //userSlice에서 가져오는 유저아이디
  const userId = useSelector((state) => {
    console.log("리덕스 userId?", state.user.userId);
    return state.user.userId;
  }); //userSlice저장소에서 유저아이디 가져오기

  //userSlice 로그인 상태 확인
  const isLoggedIn = useSelector((state) => {
    console.log("리덕스 isLoggedIn이니?", state.user.isLoggedIn);
    return state.user.isLoggedIn;
  });

  const JWT_EXPIRY_TIME = 30 * 60 * 1000; //액세스 토큰 만료시간 30분을 밀리초로 표현

  const onSubmit = (data) => {
    // console.log(data); //{email: 'test1@email.com', password: 'aaaa1111'}
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
            console.log("ACCESS_TOKEN", ACCESS_TOKEN);
            //액세스토큰 만료되기 1분 전 로그인 연장
            setTimeout(onSilentRefresh, JWT_EXPIRY_TIME - 60000);
            // setTimeout(onSilentRefresh, 3000); //3초로 실험
          }
        })
        .catch((error) => console.log(error, "silent refresh 에러"));
    };

    //=======================
    //로그인 요청. 액세스 토큰을 요청헤더에 설정
    //일반 로그인 요청보낼때 response body에 memberID 추가될 예정
    //memberID있으면 유저 정보(이름, 프사path)를 받아올수있는 API 추가될 예정 -> 그 api에 요청보내서 유저 정보를 받아온걸 슬라이스에 저장해야될듯?

    axios
      .post("/api/login", data)
      .then((response) => {
        // console.log(response); //{data: {…}, status: 200, statusText: 'OK', headers: {…}, config: {…}, …}
        const ACCESS_TOKEN = response.headers["access-token"]; //eyJ0eX.. 서버에서 response header에 싣어보내는 토큰값
        if (response.status === 200) {
          axios.defaults.headers.common[
            "Authorization"
          ] = `Bearer ${ACCESS_TOKEN}`; //요청헤더에 액세스 토큰 설정
          console.log("ACCESS_TOKEN", ACCESS_TOKEN);
          // setIsLoggedIn(true);

          //로그인 성공 상태 리덕스 저장소로 보내기
          // dispatch(setLoginSuccess({ userEmail: data.email }));
          // dispatch(setLoginSuccess({}));
          console.log(response.data); //서버에서 응답바디로 주는것 {memberId: 2}
          dispatch(setLoginSuccess({ userId: response.data.memberId })); //userSlice에 유저아이디 저장
          getUserInfo(response.data.memberId); //여기서 한번에 유저정보 받아오는 요청까지 보내면 아직 memberId가 들어오지 않는다.

          //로그인 성공하면 memberId는 우선 상태로 저장
          setMemberId(response.data.memberId);

          alert("로그인 성공");
          navigate("/");
          //액세스토큰 만료되기 전 로그인 연장
          setTimeout(onSilentRefresh, JWT_EXPIRY_TIME - 60000);
          // setTimeout(onSilentRefresh, 3000); //3초로 실험
        }
      })
      .catch((error) => {
        console.log(error);
        if (error.response.status === 401) {
          alert("아이디 혹은 비밀번호가 일치하지 않아요\n" + error.message);
        } else {
          alert(error.message);
        }
      });
    //=============================

    // getUserInfo();//onSubmit 함수안에있어도 유저정보 못받아옴

    // 기존코드
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

    //액세스 토큰 로컬스토리지에 저장
    // axios
    //   .post("/api/login", data)
    //   .then((response) => {
    //     console.log(response);
    //     if (response.status === 200) {
    //       const ACCESS_TOKEN = response.headers["access-token"]; //eyJ0eX..
    //       localStorage.setItem("ACEESS_TOKEN", ACCESS_TOKEN); //로컬스토리지에 토큰 저장
    //       //요청 헤더에 토큰 넣어주기

    //       setIsLoggedIn(true);
    //       alert("로그인 성공");
    //     }
    //   })
    // .catch((error) => {
    //   console.log(error);
    //   if (error.response.status === 401) {
    //     alert("아이디 혹은 비밀번호가 일치하지 않아요\n" + error.message);
    //   } else {
    //     alert(error.message);
    //   }
    // })
  }; //onSubmit 함수끝

  console.log("memberId", memberId);

  //로그인 요청시 서버에서 보내주는 memberId로 사용자 정보를 조회
  const getUserInfo = (userId) => {
    console.log("겟유저인포 ");
    axios
      // .get(`/api/members/${userId}}`)
      .get("/api/members/" + userId)
      .then((response) => {
        if (response.status === 200) {
          console.log(response);
        }
      });
    // .catch((error) => console.log("사용자 정보 조회 fail", error));
  };

  //로그인 상태 true일떄만 유저 정보 불러오기 - 안됨
  // useEffect(() => {
  // if (isLoggedIn) {
  // getUserInfo();
  // console.log(memberId);
  // // }
  // console.log(isLoggedIn);
  // }, [isLoggedIn]);

  // if (memberId !== 0) {
  //   setTimeout(getUserInfo(), 2000);
  // }

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
            // required: true,
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
            // required: true,
            minLength: {
              value: 8,
              message: "비밀번호는 8자 이상이에요",
            },
          })}
        ></input>
        {errors.password && <span>{errors.password.message}</span>}
        <GeneralButton
          // disabled={errors.email && errors.password}
          // disabled={!(isValid && isDirty)}
          // className={!(isValid && isDirty) ? "disabled-btn" : ""} //isDirty 는 input 전체 클릭하기만 해도 true가 되어서 빈문자열일때도 disabled해제
          disabled={!isValid}
          // className={!isValid ? "disabled-btn" : ""}
        >
          로그인
        </GeneralButton>
      </form>
    </LogInFormContainer>
  );
};

export default LogInForm;
