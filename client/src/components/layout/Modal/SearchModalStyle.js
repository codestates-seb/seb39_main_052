import styled from "styled-components";

export const RecipeCardWrapper = styled.div`
  padding: 16px;
  display: flex;

  &.invisible {
    display: none;
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
`;
export const ModalSearchBarWrapper = styled.div`
  padding: 16px;
`;

export const GeneralButtonWrapper = styled.div`
  /* float: right; */
  position: absolute;
  top: 420px;
  /* right: 32px; */
  right: 88px;

  &.invisible {
    display: none;
  }
`;
