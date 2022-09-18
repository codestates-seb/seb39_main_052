import { useForm } from "react-hook-form";
import GeneralButton from "../common/Button/GeneralButton";
import { LogInFormContainer } from "./LogInFormStyle";

const LogInForm = () => {
  const {
    register,
    handleSubmit,
    formState: { isSubmitting, errors, isDirty, isValid },
  } = useForm();
  //isValid: errors 객체 비어있으면 true
  //isDirty: form 양식 어떤 input이라도 건드렸으면 true?

  return (
    <LogInFormContainer>
      <form onSubmit={handleSubmit((data) => console.log(data))}>
        {/* {email: 'test@email.com', password: '1111'} */}
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
          width="300px"
          height="35px"
        >
          로그인
        </GeneralButton>
        {/* props로 width, height 내려주는 코드로 다시짜기 */}
      </form>
    </LogInFormContainer>
  );
};

export default LogInForm;
