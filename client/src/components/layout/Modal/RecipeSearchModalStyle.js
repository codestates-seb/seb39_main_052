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
  /* display: flex;
  flex-direction: column;
  align-items: center; */
`;

export const ModalContainer = styled.div`
  /* width: 880px; */
  width: 864px;
  /* height: 520px; */
  height: 490px;
  /* height: fit-content; */
  background-color: white;
  position: absolute;
  top: 25%;
  left: 20%;
  padding: 32px;

  //더보기 버튼 담고있는 i태그
  & > i {
    /* float: right; */
    position: absolute;
    top: 420px;
    /* right: 32px; */
    right: 88px;
  }
`;
export const CloseButton = styled.button`
  background-color: white;
  width: 20px;
  height: 20px;
  /* float: right; */ //float으로하면 너무 오른쪽
  position: absolute;
  top: 32px;
  right: 32px;
`;

export const Contents = styled.div`
  padding: 16px 0;
  /* 서치바, 레시피 카드 감싸고 있는 each div 태그 */
  & > div {
    padding: 16px;
    display: flex;
  }
`;
