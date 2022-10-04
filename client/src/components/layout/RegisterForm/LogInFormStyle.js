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

  > form > span {
    font-size: 14px;
    color: var(--gray-600);
  }
`;

export const SignUpDiv = styled.div`
  display: flex;
  justify-content: flex-end;
  margin: 8px;
  font-size: 14px;
  color: var(--gray-500);
  & > span :first-of-type {
    padding-left: 8px;
    &:hover {
      color: var(--fridge-800);
      font-weight: bold;
      font-size: 15px;
    }
  }
`;
