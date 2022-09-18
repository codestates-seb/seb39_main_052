import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";

import GeneralButton from "../common/Button/GeneralButton";
import { SignUpFormContainer } from "./SignUpFormStyle";

const SignUpForm = () => {
  const formSchema = yup.object({
    email: yup.string().email("이메일 형식이 아닙니다").required(""),
    username: yup
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
        "영문 숫자포함 8자리 이상 입력해주세요."
      ),
    passwordConfirm: yup
      .string()
      .oneOf([yup.ref("password")], "비밀번호가 일치하지 않아요"),
  });

  const {
    register,
    handleSubmit,
    formState: { isSubmitting, errors, isValid },
  } = useForm({ resolver: yupResolver(formSchema) });

  return (
    <SignUpFormContainer>
      <form onSubmit={handleSubmit((data) => console.log(data))}>
        {/* {email: 'test@email.com', password: '1111'} */}
        <label>이름</label>
        <input
          id="username"
          type="username"
          name="username"
          placeholder="닉네임을 입력해주세요"
          {...register("username")}
          //react hook form 기존 코드
          //   {...register("username", {
          //     // required: true,
          //     pattern: {
          //       value: /[a-z]/ || /[0-9]/ || /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/,
          //       message: "닉네임은 영소문자, 한글, 숫자로 만들 수 있어요",
          //     },
          //     minLength: {
          //       value: 2,
          //       message: "닉네임은 2자 이상 10자 이하로 만들 수 있어요",
          //     },
          //     maxLength: {
          //       value: 10,
          //       message: "닉네임은 2자 이상 10자 이하로 만들 수 있어요",
          //     },
          //   })}
        ></input>
        {errors.username && <span>{errors.username.message}</span>}

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
          placeholder="비밀번호를 입력해주세요"
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

        <GeneralButton>회원가입</GeneralButton>
        {/* props로 width, height 내려주는 코드로 다시짜기 */}
      </form>
    </SignUpFormContainer>
  );
};

export default SignUpForm;
