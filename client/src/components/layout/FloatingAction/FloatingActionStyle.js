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
`