import GeneralButton from "../../common/Button/GeneralButton";

const SocialForm = () => {
  return (
    <>
      <GeneralButton>
        <a href="https://seb39myfridge.ml/oauth2/authorization/google">
          구글로 로그인
        </a>
      </GeneralButton>
      <GeneralButton>
        <a href="https://seb39myfridge.ml/oauth2/authorization/kakao">
          카카오로 로그인
        </a>
      </GeneralButton>
    </>
  );
};

export default SocialForm;
