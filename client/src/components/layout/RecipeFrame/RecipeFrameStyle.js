import styled from "styled-components";

export const RecipeFrameContainer = styled.div`
  display: flex;
  align-items: center;
  padding: 16px;
  border-bottom: solid 1px var(--gray-300);
`;
export const DisplayImg = styled.img`
  width: 100px;
  height: 100px;
  flex-basis: 20%;
  cursor: pointer;
`;

export const Description = styled.div`
  /* padding-right: 16px; */
  flex-basis: 70%;
  padding: 0 16px;
  cursor: pointer;
`;

export const Title = styled.div`
  /* padding: 4px 0; */
  margin: 4px 0;
  color: var(--gray-900);
  //2줄 넘어가면 ellipsis 로 보여주기
  text-overflow: ellipsis;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  /* white-space: normal; */
`;
export const Spec = styled.div`
  display: flex;
  /* justify-content: space-between; */
  font-size: 12px; //자식요소 하위에 더 div, span 있어서 여기서 폰트사이즈 설정시 안먹는 요소가 몇개있음
  padding: 4px 0;
`;

export const Date = styled.div`
  font-size: 12px;
  padding: 4px 0;
`;
export const Icon = styled.div`
  flex-basis: 10%;
  display: flex;
  flex-direction: column;
  align-items: flex-end;

  & > span {
    padding: 8px 0 8px 0;
    cursor: pointer;
  }
`;
