import { useRef } from "react";
import useDetectOutsideClick from "../../../hooks/useDetectOutsideClick";
import LogOut from "./LogOut";
import styled, { css } from "styled-components";
//user아이콘 가져오기
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUserLarge } from "@fortawesome/free-solid-svg-icons";
//기본 냉장고 얼굴 이미지 가져오기
import small_logoface from "../../../assets/small_logoface.png";

//Style파일에서 css 가져오기
import {
  DropDownContainer,
  DropDownButton,
  Menu,
  Ul,
  Li,
} from "./DropDownMenuStyle";

const DropDownMenu = ({ profileIconPhoto }) => {
  const dropdownRef = useRef(null);
  const [userIsOpen, setUserIsOpen] = useDetectOutsideClick(dropdownRef, false);
  const onClickOpen = () => {
    setUserIsOpen(!userIsOpen);
  };
  console.log("userIsOpen?", userIsOpen);

  return (
    <DropDownContainer className="dropdown_container">
      <DropDownButton onClick={onClickOpen} ref={dropdownRef}>
        {/* GNB에서 받아온 props profileIconPhoto null이면 유저아이콘 보여주고 null이 아니면 프로필 이미지 path 보여주기 */}
        {profileIconPhoto === null ? (
          // <FontAwesomeIcon icon={faUserLarge} size="lg" /> //폰트어썸아이콘말고 냉장고 얼굴 대표이미지
          <img src={small_logoface} />
        ) : (
          <img src={profileIconPhoto} />
        )}
      </DropDownButton>
      <Menu isDropped={userIsOpen}>
        <Ul>
          <Li>내 프로필</Li>
          <Li>마이페이지</Li>
          <Li>
            <LogOut />
          </Li>
        </Ul>
      </Menu>
    </DropDownContainer>
  );
};

export default DropDownMenu;
