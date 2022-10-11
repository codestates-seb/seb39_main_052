import styled from "styled-components";

export const SignUpFormContainer = styled.section`
  display: flex;
  justify-content: center;
  margin-top: 32px;
  color: var(--gray-700);
  width: 100vw;
  height: 60vh;

  > form {
    display: flex;
    flex-direction: column;
    /* justify-content: center; */
    padding: 10px;
    /* background-color: turquoise; //just for checking */
  }

  > form > label {
    margin-top: 10px;
  }

  > form > input {
    border: none;
    border-bottom: 2px solid var(--fridge-500);
    width: 300px;
    margin: 10px 0;
  }

  //유효성검사 에러 메시지
  > form > span {
    font-size: 14px;
    /* color: var(--gray-600); */
    color: var(--red-400);
  }
`;
