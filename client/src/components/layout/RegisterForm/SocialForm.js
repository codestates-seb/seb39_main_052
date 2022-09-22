import styled from "styled-components";
import GeneralButton from "../../common/Button/GeneralButton";

const SocialForm = () => {
  return (
    <SocialFormContainer>
      <BorderLine></BorderLine>
      <span>소셜 간편 로그인</span>
      <GeneralButton
        backgroundColor="var(--gray-800)"
        hoverBackgroundColor="black"
      >
        <a href="https://seb39myfridge.ml/oauth2/authorization/google">
          구글로 로그인
        </a>
      </GeneralButton>
      <GeneralButton
        backgroundColor="#FDE502"
        color="black"
        hoverBackgroundColor="#cfbd1b"
      >
        <a href="https://seb39myfridge.ml/oauth2/authorization/kakao">
          카카오로 로그인
        </a>
      </GeneralButton>
    </SocialFormContainer>
  );
};
const SocialFormContainer = styled.section`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  //소셜 간편 로그인
  > span {
    font-size: 14px;
    color: var(--gray-500);
  }
`;
const BorderLine = styled.div`
  border-bottom: 2px solid var(--gray-200);
  width: 300px;
  margin: 10px 0;
`;
export default SocialForm;
