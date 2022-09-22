import { Link } from "react-router-dom";
import styled from "styled-components";
import LogInForm from "../../components/layout/RegisterForm/LogInForm";
import SocialForm from "../../components/layout/RegisterForm/SocialForm";

const LogIn = () => {
  return (
    <LogInContainer>
      <LogInForm />
      {/* <span>내 냉장고에 있는 재료로 요리하고 싶으신가요?</span> */}
      <div>
        {/* <span id="hookmsg">
          냉장고에 있는 재료로 만들 수 있는 요리를 추천받고 싶으신가요?
        </span> */}

        <span id="signup">
          <Link to="/signup">회원가입</Link>
        </span>
      </div>
      <SocialForm />
    </LogInContainer>
  );
};

const LogInContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  > div > span {
    font-size: 14px;
    color: var(--gray-500);
  }
`;

export default LogIn;
