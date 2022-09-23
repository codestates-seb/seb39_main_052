import styled, { keyframes } from "styled-components";

const BounceAnimation = keyframes`
  0% { margin-bottom: 0; }
  5% { margin-bottom: 10px }
  10% { margin-bottom: 0 }
  100% { margin-bottom: 0 }
`;

export const ButtonWrapper = styled.div`
  /* 정신 사나울 때 끄기 위해 */
  /* display: none; */
  animation: ${BounceAnimation} 4s linear infinite;
  bottom: 30px;
  right: 40px;
  position: fixed;
  > * {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    // 호버시 레시피 작성하기 글 보이기
    &:not(:hover) {
      > div:first-of-type {
        display: none;
        color: red;
      }
    }
  }
`

export const Text = styled.div`
  font-size: 10px;
  padding: 0px 4px 4px 0px;
  color: var(--fridge-800);
`