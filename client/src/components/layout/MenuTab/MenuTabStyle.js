import styled, { css } from "styled-components";

export const MenuTabContainer = styled.div`
  width: 560px;
  height: fit-content;
  /* border: solid 1px; //just for checking  */
  display: flex;
  flex-direction: column;
  align-items: center;
`;

export const Menu = styled.ul`
  /* padding: 16px; */
  display: flex;
  flex-direction: row;
  width: 90%;
  justify-content: space-between;
  /* height: 40px; */
`;

export const Li = styled.li`
  width: calc(90% / 4); //li 차지공간 90%로하고 상위 컴포넌트에서 간격 띄우려고
  display: flex;
  height: 40px;
  cursor: pointer;
  border-radius: 35px;
  justify-content: center;
  align-items: center;
  background-color: var(--mint-300);

  &:hover {
    background-color: var(--mint-500);
    color: white;
    box-shadow: var(--red-050) 0px 10px 20px, var(--red-100) 0px 6px 6px;
  }

  ${({ isFocused }) =>
    isFocused &&
    css`
      background-color: var(--mint-500);
      box-shadow: rgba(0, 0, 0, 0.19) 0px 10px 20px, var(--red-075) 0px 6px 6px;
    `}
`;

export const MenuTitle = styled.div`
  font-weight: bold;
  color: var(--gray-800);
  display: flex;
  justify-content: center;
  margin-top: 16px;
`;

export const MenuContent = styled.div`
  display: flex;
  justify-content: center;
  margin: 32px;
  font-size: 14px;
  color: var(--gray-700);
`;
