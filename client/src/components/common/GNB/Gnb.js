import LogOut from "../../layout/DropDown/LogOut";
import largelogoblack from "../../../assets/largelogoblack.png";
import { Link, useSearchParams } from "react-router-dom";
import {
  Header,
  Nav,
  LeftBox,
  LeftBoxForMobile,
  LogoBox,
  Img,
  RightBox,
  Ul,
  Li,
} from "./GnbStyle";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faMagnifyingGlass,
  faUserLarge,
  faBars,
} from "@fortawesome/free-solid-svg-icons";
import DropDownMenu from "../../layout/DropDown/DropDownMenu";

//로그인 상태 가져오려고 리덕스 저장소 정보 import
import { useSelector, useDispatch } from "react-redux";

import { useEffect, useState } from "react";
import RecipeSearchModal from "../../layout/Modal/RecipeSearchModal"; //모달 컴포넌트 만들기 이전 레시피 서치 모달
import SearchModal from "../../layout/Modal/SearchModal"; //컴포넌트로 만든 이후의 서치 모달

const Gnb = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  //로그인 상태 가져와서 변수에 저장
  const isLoggedIn = useSelector((state) => {
    return state.user.isLoggedIn;
  });
  // console.log("GNB에서 isLoggedIn이니?", isLoggedIn);

  //userSlice 전체 상태 확인
  // useSelector((state) => {
  //   console.log("userSlice 전체상태?", state.user);
  // }); //{isLoggedIn: false, userId: null, userEmail: undefined}

  //로그인한 회원 프로필 사진 redux에서 가져오기
  const profilePhoto = useSelector((state) => {
    return state.user.userProfileImgPath;
  });
  // console.log("프로필사진 상태?", profilePhoto);

  //search bar 모달 상태
  const [showModal, setShowModal] = useState(false);

  //모바일 화면에서 보이는 왼쪽 상단 햄버거 메뉴 토글 상태
  const [isToggleOpen, setIsToggleOpen] = useState(false);

  //모달창 열때 서치파람 없애기
  useEffect(() => {
    if (showModal) {
      console.log("모달창 유즈이펙트");
      // searchParams.set("keyword", "");
      setSearchParams({ keyword: "" });
    }
  }, [showModal]);

  const clickModal = () => {
    setShowModal(!showModal);
  };
  // console.log("showModal 상태?", showModal);

  const handleToggle = () => {
    setIsToggleOpen(!isToggleOpen);
  };
  // console.log("isToggleOpen상태", isToggleOpen);

  return (
    <Header>
      <Nav>
        <LeftBox className="leftbox" isToggleOpen={isToggleOpen}>
          <Ul className="nav-menu-list">
            <Li className="each-nav-menu-list">
              <Link to="/search">냉장고 파먹기</Link>
            </Li>
            <Li className="each-nav-menu-list">
              <Link to="/myfridge">나의 냉장고</Link>
            </Li>
          </Ul>
        </LeftBox>
        {/* for Mobile toggle */}
        <LeftBoxForMobile className="menuToggle" onClick={handleToggle}>
          <FontAwesomeIcon icon={faBars} size="lg" />{" "}
        </LeftBoxForMobile>

        <LogoBox className="logo">
          <Link to="/">
            <Img src={largelogoblack} />
          </Link>
        </LogoBox>
        <RightBox className="rightbox">
          <Ul>
            {!isLoggedIn ? (
              <Li>
                <Link to="/login">로그인</Link>
              </Li>
            ) : (
              <Li className="profile_icon_photo">
                <DropDownMenu profileIconPhoto={profilePhoto} />
              </Li>
            )}

            {/* <Li onClick={() => setShowModal(true)}> */}
            <Li onClick={clickModal}>
              <FontAwesomeIcon
                icon={faMagnifyingGlass}
                size="lg"
              ></FontAwesomeIcon>
              {/* {showModal && (
                <RecipeSearchModal
                  // handleClose={() => {
                  //   setShowModal(false);
                  // }} //이렇게 상태관리하면 모달 안닫힘
                  handleClose={clickModal}
                ></RecipeSearchModal>
              )}  //모달 컴포넌트화 이전 코드*/}
              {showModal && (
                <SearchModal handleClose={clickModal}></SearchModal>
              )}
            </Li>
          </Ul>
        </RightBox>
      </Nav>
    </Header>
  );
};

export default Gnb;
