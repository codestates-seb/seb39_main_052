import styled, { keyframes } from "styled-components";

const BounceAnimation = keyframes`
  0% { margin-bottom: 0; }
  25% { margin-bottom: 10px }
  50% { margin-bottom: 0 }
  100% { margin-bottom: 0 }
`;

export const ButtonWrapper = styled.div`
    animation: ${BounceAnimation} 1s linear infinite;
    bottom: 30px;
    right: 40px;
    position: fixed;
`