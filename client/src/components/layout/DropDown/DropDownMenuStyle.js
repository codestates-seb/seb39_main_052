import styled, { css } from "styled-components";

export const DropDownContainer = styled.div`
  position: relative;
`;

export const DropDownButton = styled.div`
  cursor: pointer;
  /* width: 20px;
  height: 20px; */
  transition: transform 0.5s ease; //여기선 transition을 hover에 바로 줘도 똑같이작동

  & > img {
    width: 35px;
    height: 35px;
    border-radius: 50%;
    object-fit: cover;
  }

  &:hover {
    /* border-radius: 50%;
    box-shadow: 0 1px 8px rgba(0, 0, 0, 0.3);*/ //프로필사진에 갖다대면 동그란 그림자 생기기 .. but too ugly
    transform: scale(1.15);
  }
`;

export const Menu = styled.div`
  background: white;
  position: absolute;
  border-radius: 8px;
  top: 50px;
  /* right: 0; */
  right: -120%;
  width: 120px;
  text-align: center;
  box-shadow: 0 1px 8px rgba(0, 0, 0, 0.3);
  opacity: 0;
  visibility: hidden; //document에는 남아있고 레이아웃에 공간도 차지하지만 안보이게 함
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
  padding: 8px;
  /* margin: 8px; */

  &:hover {
    border-radius: 10px;
    box-shadow: var(--shadow-3d);
  }
`;
