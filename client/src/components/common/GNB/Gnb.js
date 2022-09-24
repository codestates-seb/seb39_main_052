import LogOut from "../../layout/DropDown/LogOut";
import largelogoblack from "../../../assets/largelogoblack.png";
import { Link } from "react-router-dom";
import {
  Header,
  Nav,
  LeftBox,
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
} from "@fortawesome/free-solid-svg-icons";
import DropDownMenu from "../../layout/DropDown/DropDownMenu";

//로그인 상태 가져오려고 리덕스 저장소 정보 import
import { useSelector, useDispatch } from "react-redux";
import { setLoginSuccess, setLoggedOut } from "../../../features/userSlice";

import { useState } from "react";
import RecipeSearchModal from "../../layout/Modal/RecipeSearchModal";

const Gnb = () => {
  // //로그인 상태 가져와서 변수에 저장
  const isLoggedIn = useSelector((state) => {
    return state.user.isLoggedIn;
  });
  // console.log("GNB에서 isLoggedIn이니?", isLoggedIn);

  //userSlice 전체 상태 확인
  useSelector((state) => {
    console.log("userSlice 전체상태?", state.user);
  }); //{isLoggedIn: false, userId: null, userEmail: undefined}

  //search bar 모달 상태
  const [showModal, setShowModal] = useState(false);

  const clickModal = () => {
    setShowModal(!showModal);
  };
  // console.log("showModal 상태?", showModal);

  return (
    <Header>
      <Nav>
        <LeftBox className="leftbox">
          <Ul>
            <Li>냉장고 파먹기</Li>
            <Li>나의 냉장고</Li>
          </Ul>
        </LeftBox>
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
              <Li>
                <DropDownMenu />
              </Li>
            )}

            {/* <Li onClick={() => setShowModal(true)}> */}
            <Li onClick={clickModal}>
              <FontAwesomeIcon
                icon={faMagnifyingGlass}
                size="lg"
              ></FontAwesomeIcon>
              {showModal && (
                <RecipeSearchModal
                  // handleClose={() => {
                  //   setShowModal(false);
                  // }} //이렇게 상태관리하면 모달 안닫힘
                  handleClose={clickModal}
                ></RecipeSearchModal>
              )}
            </Li>
          </Ul>
        </RightBox>
      </Nav>
    </Header>
  );
};

export default Gnb;
