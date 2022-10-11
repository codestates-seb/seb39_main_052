import styled from "styled-components";

export const AdminPanelContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 32px 0;
  min-height: 60vh;
`;

export const Panel = styled.div`
  background-color: var(--gray-200);
  width: 340px;
  padding: 8px;
  font-size: 14px;
  margin: 16px 0;
`;

// 레시피ID, 제목, 작성자 레이블 감싸고있는 박스
export const SpanWrapper = styled.div`
  display: flex;
  /* justify-content: space-between; */
  align-items: center;
  font-size: 12px;

  //레시피ID
  > span :first-of-type {
    width: 32px;
  }

  //레시피제목
  > span:nth-of-type(2) {
    width: 200px;
  }

  //작성자
  > span:nth-of-type(3) {
    width: 76px;
  }
`;

export const RowWrapper = styled.div`
  display: flex;
  align-items: center;
  justify-items: flex-start;
  /* justify-content: space-between; */
  height: 30px;
  max-width: 25rem;

  //실제 숫자로된 레시피아이디
  > :first-child {
    width: 48.22px;
  }

  //레시피 제목
  > span.title {
    /* text-overflow: ellipsis;
    overflow: hidden;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    white-space: normal; */
    width: 200px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }

  //작성자
  > :nth-child(3) {
    width: 76px;
  }
`;
