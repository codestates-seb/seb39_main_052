import styled from "styled-components";

export const Header = styled.header`
  min-height: 88px;
  width: 100vw;
  background-color: var(--primary-400);
  box-shadow: var(--shadow-medium);
  /* background-color: white; */
  padding: 8px;
  /* display: flex;
  justify-content: center; */
  position: sticky;
  top: 0;
  z-index: 9999;

  /* media query 예시. theme.js에서 정의한 theme을 props로 받아서 mobile 사이즈 가져온다 */
  @media ${({ theme }) => theme.device.mobile} {
    box-shadow: var(--shadow-low);
    /* color: pink; */
    /* max-height: 88px; */
    /* min-height: 88px; */
  }
`;

//박스 담고있는 전체 박스
export const Nav = styled.nav`
  display: flex;
  justify-content: space-between;
  align-items: center;
  @media ${({ theme }) => theme.device.mobile} {
    max-height: 80px;
  }
`;

//냉장고 파먹기, 나의 냉장고 담고있는 박스 . 미디어 쿼리시 navBar아래에 위치
export const LeftBox = styled.div`
  width: 320px;
  @media ${({ theme }) => theme.device.mobile} {
    display: ${(props) => (props.isToggleOpen ? "block" : "none")};
    width: 100%;
    /* height: 40px; */
    position: absolute;
    top: 88px;
    left: 0;
    margin: 0;
    background-color: var(--fridge-300);
    box-shadow: var(--shadow-high);
    opacity: 90%;
  }
`;

//메뉴 토글 담는 박스 - LeftBoxForMobile로 컴포넌트화 했기떄문에 class 붙이지 않아도 작동!
export const LeftBoxForMobile = styled.div`
  width: 20px;
  height: 20px;
  cursor: pointer;
  padding-left: 16px;

  &.menuToggle {
    display: none; /* 모바일 화면 아닐떄는 안보임 */
  }
  @media ${({ theme }) => theme.device.mobile} {
    &.menuToggle {
      display: block; /*모바일 화면에서는 메뉴 토글 보여주기*/
      :hover {
        color: var(--fridge-500);
      }
    }
  }
`;

export const LogoBox = styled.div`
  /* display: flex; */
  @media ${({ theme }) => theme.device.mobile} {
    margin-left: 12%;
    padding-top: 4px;
  }
`;

export const RightBox = styled.div`
  width: 320px;
  @media ${({ theme }) => theme.device.mobile} {
    width: 68px;
  }
`;

export const Img = styled.img`
  width: 118px;
  height: 68px;
  display: flex;
  margin-left: 16px; //logo가운데로 오게하려고
  /* align-items: flex-end; */
`;

export const Ul = styled.ul`
  display: flex;
  justify-content: flex-end;
  align-items: center;
  @media ${({ theme }) => theme.device.mobile} {
    &.nav-menu-list {
      justify-content: space-around;
      margin: 0;
      height: 40px;
    }
  }
`;

export const Li = styled.li`
  cursor: pointer;
  padding: 0 32px;

  /* &.profile_icon_photo {
    width: 25px;
    height: 25px;
    border-radius: 50%;
  } //profile icon, photo 감싸고 있는 Li태그에서 이미지 크기 조정하면 안먹음 */

  @media ${({ theme }) => theme.device.mobile} {
    //icon 이랑 돋보기
    padding: 16px;

    //냉장고 파먹기 나의 냉장고 글씨 li
    &.each-nav-menu-list {
      padding: 8px;
      :hover {
        border-radius: 25px;
        background-color: var(--fridge-500);
        box-shadow: var(--shadow-3d);
      }
    }
    &.login {
      font-size: 13px;
    }

    &.admin {
      font-size: 13px;
      padding: 4px;
    }

    &.admin + &.profile_icon_photo {
      padding: 4px;
    }
  }
`;
