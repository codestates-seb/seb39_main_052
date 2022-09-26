import styled, { css } from "styled-components";

export const MenuTabContainer = styled.div`
  width: 560px;
  height: fit-content;
  border: solid 1px;
  display: flex;
  flex-direction: column;
  /* justify-content: center; */
`;

export const Menu = styled.ul`
  background-color: turquoise;
  /* padding: 16px; */
  display: flex;
  flex-direction: row;
`;

export const Li = styled.li`
  width: calc(100% / 4);
  display: flex;
  height: 32px;
  justify-content: center;
  align-items: center;
  background-color: var(--mint-300);
  ${({ isFocused }) =>
    isFocused &&
    css`
      background-color: var(--mint-500);
    `}
`;

export const MenuContent = styled.div`
  display: flex;
  justify-content: center;
`;
