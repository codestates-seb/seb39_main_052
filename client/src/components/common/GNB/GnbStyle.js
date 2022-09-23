import styled from "styled-components";

export const Header = styled.header`
  min-height: 88px;
  width: 100%;
  /* background-color: var(--primary-400); */
  background-color: white;
  padding: 16px;
  /* display: flex;
  justify-content: center; */
  position: sticky;
  top: 0;
`;
export const Nav = styled.nav`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;
export const LeftBox = styled.div``;
export const LogoBox = styled.div`
  display: flex;
`;
export const RightBox = styled.div``;
export const Img = styled.img`
  width: 118px;
  height: 68px;
  align-items: flex-end;
`;

export const Ul = styled.ul`
  display: flex;
`;

export const Li = styled.li`
  padding: 0 32px;
`;
