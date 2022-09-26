import styled, { css } from "styled-components";

export const DropDownContainer = styled.div`
  position: relative;
`;

export const DropDownButton = styled.div`
  cursor: pointer;
  &:hover {
    border-radius: 8px;
    box-shadow: 0 1px 8px rgba(0, 0, 0, 0.3);
  }
`;

export const Menu = styled.div`
  background: white;
  position: absolute;
  border-radius: 8px;
  top: 40px;
  /* right: 0; */
  right: -250%;
  width: 120px;
  text-align: center;
  box-shadow: 0 1px 8px rgba(0, 0, 0, 0.3);
  opacity: 0;
  visibility: hidden;
  transform: translateY(-20px);
  transition: opacity 0.4s ease, transform 0.4s ease, visibility 0.4s;
  /* z-index: 9; */
  /* padding: 8px; */

  ${({ isDropped }) =>
    isDropped &&
    css`
      opacity: 1;
      visibility: visible;
      transform: translateY(0);
    `}
`;

export const Ul = styled.ul`
  padding: 8px;
`;
export const Li = styled.li`
  padding: 4px;
`;
