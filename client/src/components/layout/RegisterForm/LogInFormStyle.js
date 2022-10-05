import styled from "styled-components";

export const LogInFormContainer = styled.section`
  display: flex;
  justify-content: center;
  flex-direction: column;
  color: var(--gray-700);

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

  //유효성검사 메시지
  > form > span {
    font-size: 14px;
    color: var(--red-400);
  }
`;

export const SignUpDiv = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  margin: 8px;
  font-size: 14px;
  color: var(--gray-500);
  border-top: solid 2px var(--gray-200);

  //회원가입 버튼으로 나타낼때 hook msg
  > span {
    padding-top: 10px;
    margin-left: 60px;
  }
  //작은글씨로 회원가입 했을때
  & > span :first-of-type {
    padding-left: 8px;
    &:hover {
      color: var(--fridge-800);
      font-weight: bold;
      font-size: 15px;
    }
  }
`;
