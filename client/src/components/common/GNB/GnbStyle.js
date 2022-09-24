import styled from "styled-components";

export const Header = styled.header`
  min-height: 88px;
  width: 100%;
  background-color: var(--primary-400);
  /* background-color: white; */
  padding: 16px;
  /* display: flex;
  justify-content: center; */
  position: sticky;
  top: 0;

  /* media query 예시. theme.js에서 정의한 theme을 props로 받아서 mobile 사이즈 가져온다 */
  @media ${({ theme }) => theme.device.mobile} {
    color: pink;
  }
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
  cursor: pointer;
  padding: 0 32px;
`;
