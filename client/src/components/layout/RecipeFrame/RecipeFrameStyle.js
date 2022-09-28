import styled from "styled-components";

export const RecipeFrameContainer = styled.div`
  //Frame 상위 each RecipeFrame을 담고있는 애한테 적용하기
  /* width: 80%;
  background-color: var(--gray-100);
  border-radius: 10px;
  padding: 16px; */
  display: flex;
  align-items: center;
  padding: 16px;
  border-bottom: solid 1px var(--gray-300);
`;
export const DisplayImg = styled.img`
  width: 100px;
  height: 100px;
  flex-basis: 20%;
`;

export const Description = styled.div`
  /* padding-right: 16px; */
  flex-basis: 70%;
  padding: 0 16px;
`;

export const Title = styled.div``;
export const Spec = styled.div`
  display: flex;
  justify-content: space-between;
`;

export const Date = styled.div`
  font-size: 12px;
`;
export const Icon = styled.div`
  flex-basis: 10%;
  display: flex;
  flex-direction: column;
  align-items: flex-end;

  & > span {
    padding: 8px 0 8px 0;
  }
`;
