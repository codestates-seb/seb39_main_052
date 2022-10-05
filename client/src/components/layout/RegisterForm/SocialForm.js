import styled from "styled-components";
import GeneralButton from "../../common/Button/GeneralButton";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faComment } from "@fortawesome/free-solid-svg-icons";
import { faGoogle } from "@fortawesome//free-brands-svg-icons";

const SocialForm = () => {
  return (
    <SocialFormContainer>
      <BorderLine></BorderLine>
      <span>소셜 간편 로그인</span>
      <GeneralButton
        // backgroundColor="var(--gray-800)"
        // hoverBackgroundColor="black"
        backgroundColor="#4285F4"
        hoverBackgroundColor="#356bc3"
      >
        <a href="https://api.seb39myfridge.ml/oauth2/authorization/google">
          <FontAwesomeIcon
            icon={faGoogle}
            size="lg"
            style={{ marginRight: 5 }}
          />
          구글로 로그인
        </a>
      </GeneralButton>
      <GeneralButton
        backgroundColor="#FDE502"
        color="black"
        hoverBackgroundColor="#cfbd1b"
      >
        <a href="https://api.seb39myfridge.ml/oauth2/authorization/kakao">
          <FontAwesomeIcon
            icon={faComment}
            size="lg"
            style={{ marginRight: 5 }}
          />
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
