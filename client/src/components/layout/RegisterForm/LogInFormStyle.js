import styled from "styled-components";

export const LogInFormContainer = styled.section`
  display: flex;
  justify-content: center;

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
