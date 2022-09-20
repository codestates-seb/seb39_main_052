import axios from "axios";
import { useState } from "react";
import { useForm } from "react-hook-form";
import GeneralButton from "../../common/Button/GeneralButton";
import { LogInFormContainer } from "./LogInFormStyle";
import { useNavigate } from "react-router-dom";

const LogInForm = () => {
  const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    formState: { isSubmitting, errors, isDirty, isValid },
  } = useForm();
  //isValid: errors 객체 비어있으면 true
  //isDirty: form 양식 어떤 input이라도 건드렸으면 true?

  const [isLoggedIn, setIsLoggedIn] = useState(false);
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

    axios
      .post("/api/login", data)
      .then((response) => {
        console.log(response);
        const ACCESS_TOKEN = response.headers["access-token"]; //eyJ0eX.. 서버에서 response header에 싣어보내는 토큰값
        if (response.status === 200) {
          axios.defaults.headers.common[
            "Authorization"
          ] = `Bearer ${ACCESS_TOKEN}`; //요청헤더에 액세스 토큰 설정
          console.log("ACCESS_TOKEN", ACCESS_TOKEN);
          setIsLoggedIn(true);
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
    // });
  };

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
