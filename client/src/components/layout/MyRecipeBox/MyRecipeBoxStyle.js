import styled from "styled-components";

export const RecipeFrameOuterContainer = styled.div`
  /* width: 100%; */ //설정안해도 자동으로 상위부모 컨테이너 MenuContent 크기에 맞게 채워짐
  background-color: var(--gray-100);
  border-radius: 10px;
  padding: 16px;
`;

export const SortingTabWrapper = styled.div`
  padding-bottom: 26px;
  display: flex;
  justify-content: flex-end; //정렬탭 오른쪽으로 이동
`;

export const SpanWrapper = styled.div`
  & > span {
    /* font-size: 14px; */ //상위 레시피 프레임 컨테이너 Spec 컴포넌트에서 받아오는 fontsize 12px 받아오기
    padding-right: 8px;
  }
`;
