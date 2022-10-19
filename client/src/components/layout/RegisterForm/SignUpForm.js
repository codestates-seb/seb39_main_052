import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import axios from "axios";
import { useDispatch } from "react-redux";
import { setWarningToast, setNoticeToast } from "../../../features/toastSlice";

import GeneralButton from "../../common/Button/GeneralButton";

import { SignUpFormContainer } from "./SignUpFormStyle";
import { useNavigate } from "react-router-dom";

const SignUpForm = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const formSchema = yup.object({
    email: yup.string().email("이메일 형식이 아닙니다").required(""),
    name: yup
      .string()
      .min(2, "2자 이상으로 입력해주세요")
      .max(10, "10자 이하로 입력해주세요")
      //   .matches(
      //     /[a-z]/ || /[0-9]/ || /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/,
      //     "한글, 영소문자, 숫자만 입력가능해요"
      //   )
      .required(),
    password: yup
      .string()
      .min(8, "최소 8자리 이상을 입력해주세요")
      .required()
      .matches(
        /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*#?&]{8,}$/,
        "영문 숫자포함 8자리 이상 입력해주세요"
      ),
    passwordConfirm: yup
      .string()
      .oneOf([yup.ref("password")], "비밀번호가 일치하지 않아요"),
  });

  const {
    register,
    handleSubmit,
    formState: { isSubmitting, errors, isValid },
  } = useForm({ resolver: yupResolver(formSchema), mode: "onChange" });

  const onSubmit = (data) => {
    console.log(data); // {name: '나나', email: 'test@email.com', password: 'aaaa1111', passwordConfirm: 'aaaa1111'}
    let newUser = {
      email: data.email,
      password: data.password,
      name: data.name,
    };
    axios
      .post("/api/signup", newUser)
      .then((response) => {
        // console.log(response.data)// {success: true, code: 0, failureReason: ''}
        //회원가입 요청 성공시
        if (response.status === 201) {
          // alert창 대체
          dispatch(setNoticeToast({ message: `회원가입 성공` }));
          navigate("/login");
        }
      })
      .catch((error) => {
        console.log(error);
        // alert창 대체
        dispatch(setWarningToast({ message: error.message }));
        //서버에서 예외처리 구현하면 실패 코드 응답에 따라 분기 나누기. 이미 가입된 회원입니다.
      });
  };

  return (
    <SignUpFormContainer>
      <form onSubmit={handleSubmit(onSubmit)}>
        <label>이름</label>
        <input
          id="name"
          type="name"
          name="name"
          placeholder="2자 이상 10자 이하로 입력해주세요"
          {...register("name")}
        ></input>
        {errors.name && <span>{errors.name.message}</span>}

        <label>이메일</label>
        <input
          id="email"
          type="email"
          name="email"
          placeholder="이메일을 입력해주세요"
          {...register("email")}
        ></input>
        {errors.email && <span>{errors.email.message}</span>}

        <label>비밀번호</label>
        <input
          id="password"
          type="password"
          name="password"
          placeholder="영문 숫자포함 8자리 이상 입력해주세요"
          {...register("password")}
        ></input>
        {errors.password && <span>{errors.password.message}</span>}

        <label>비밀번호 확인</label>
        <input
          id="passwordConfirm"
          type="password"
          name="passwordConfirm"
          placeholder="비밀번호를 다시 입력해주세요"
          {...register("passwordConfirm")}
        ></input>
        {errors.passwordConfirm && (
          <span>{errors.passwordConfirm.message}</span>
        )}

        <GeneralButton disabled={!isValid || isSubmitting}>
          회원가입
        </GeneralButton>
      </form>
    </SignUpFormContainer>
  );
};

export default SignUpForm;
