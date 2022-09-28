import styled from "styled-components";
export const RecipeFrameOuterContainer = styled.div`
  width: 80%;
  background-color: var(--gray-100);
  border-radius: 10px;
  padding: 16px;
`;

export const SpanWrapper = styled.div`
  & > span {
    /* font-size: 14px; */ //상위 레시피 프레임 컨테이너 Spec 컴포넌트에서 받아오는 fontsize 12px 받아오기
    padding-right: 8px;
  }
`;
