import styled from "styled-components";

export const RecipeCardWrapper = styled.div`
  padding: 16px;
  display: flex;
  width: fit-content;

  &.invisible {
    display: none;
  }

  @media ${({ theme }) => theme.device.mobile} {
    //모바일 화면에서는 숨기기
    display: none;
  }
`;

//레시피카드(모바일) 담고있는 큰 박스
export const RecipeCardWrapperMobile = styled.div`
  display: none; //데스크탑 버전에서는 안보여야함

  &.invisible {
    display: none;
  }

  @media ${({ theme }) => theme.device.mobile} {
    //모바일 화면에서는 레시피카드 모바일 버젼으로 보여주기
    display: block;
    display: flex;
    flex-direction: column;
    align-items: center;
  }
`;

export const NoticeMsg = styled.div`
  width: 90%;
  display: flex;
  justify-content: center;
  padding: 16px 0 0 0;
  /* font-family: "GmarketSansLight"; */
  color: var(--gray-700);
  white-space: pre-line; //줄바꿈
  line-height: 30px; //줄간격

  &.invisible {
    display: none;
  }
  @media ${({ theme }) => theme.device.mobile} {
    margin-left: 16px;
  }
`;
export const ModalSearchBarWrapper = styled.div`
  /* padding: 16px; */
  /* margin-left: 8px; //레시피 카드랑 시작점 맞추기 */
  /* padding-right: 40px; //레시피 카드랑 끝나는 라인 맞추기 */
  margin: 0 40px 16px 24px;

  @media ${({ theme }) => theme.device.mobile} {
    //모바일 화면에서 서치바 위치
    padding-top: 0;
  }
`;

export const GeneralButtonWrapper = styled.div`
  /* float: right; */
  position: absolute;
  top: 420px;
  /* right: 32px; */
  right: 70px;

  &.invisible {
    display: none;
  }

  @media ${({ theme }) => theme.device.mobile} {
    //모바일 화면에서 버튼 위치
    position: absolute;
    top: 704px;
    right: 48px;
    /* position: relative;
    bottom: 20px; */
  }
`;
