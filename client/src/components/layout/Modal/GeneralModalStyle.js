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
  @media ${({ theme }) => theme.device.mobile} {
    /* display: none; */
  }
`;

export const ModalContainer = styled.div`
  width: ${(props) => props.width || "600px"};
  height: ${(props) => props.height || "fit-content"};
  background-color: white;
  position: absolute;
  /* top: 20%;
  left: 20%; */
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%); //정중앙에 오게하기
  padding: 32px;
  border-radius: 16px;
  /* overflow: scroll; //스크롤이 적용안됨..흠 */
  @media ${({ theme }) => theme.device.mobile} {
    overflow-y: scroll;
    padding: 16px;
  }

  @media ${({ theme }) => theme.device.mobile} {
    width: 100%;
    height: 100%;
    background-color: white;
    opacity: 95%;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%); //정중앙에 오게하기
    border-radius: 0;
  }
`;

export const CloseButton = styled.button`
  background-color: white;
  width: 20px;
  height: 20px;
  /* float: right; */
  position: absolute;
  top: 24px;
  right: 24px;
`;

export const Contents = styled.div`
  padding: 16px 0;
`;
