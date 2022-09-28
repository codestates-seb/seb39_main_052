import styled from "styled-components";

export const Overlay = styled.div`
  position: fixed;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 10000; //모달창은 모든 윈도우 레이어에서 가장 상위에 위치로 제일 큰 숫자
  /* display: flex;
  flex-direction: column;
  align-items: center; */
`;

export const ModalContainer = styled.div`
  width: ${(props) => props.width || "600px"};
  height: ${(props) => props.height || "fit-content"};
  background-color: white;
  position: absolute;
  top: 20%;
  left: 20%;
  padding: 32px;
  /* overflow: scroll; //스크롤이 적용안됨..흠 */
`;

export const CloseButton = styled.button`
  background-color: white;
  width: 20px;
  height: 20px;
  float: right;
  /* position: absolute;
  top: 32px;
  right: 32px; */
`;

export const Contents = styled.div`
  padding: 16px 0;
`;
