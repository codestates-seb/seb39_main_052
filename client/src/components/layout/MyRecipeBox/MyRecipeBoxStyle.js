import styled from "styled-components";
export const RecipeFrameOuterContainer = styled.div`
  width: 80%;
  background-color: var(--gray-100);
  border-radius: 10px;
  padding: 16px;
`;

export const SpanWrapper = styled.div`
  & > span {
    font-size: 14px;
    padding-right: 4px;
  }
`;
