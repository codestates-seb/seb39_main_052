import { useRef } from "react";
import useDetectOutsideClick from "../../../hooks/useDetectOutsideClick";
import LogOut from "./LogOut";
import styled, { css } from "styled-components";
//user아이콘 가져오기
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUserLarge } from "@fortawesome/free-solid-svg-icons";
//Style파일에서 css 가져오기
import {
  DropDownContainer,
  DropDownButton,
  Menu,
  Ul,
  Li,
} from "./DropDownMenuStyle";

const DropDownMenu = () => {
  const dropdownRef = useRef(null);
  const [userIsOpen, setUserIsOpen] = useDetectOutsideClick(dropdownRef, false);
  const onClickOpen = () => {
    setUserIsOpen(!userIsOpen);
  };
  console.log("userIsOpen?", userIsOpen);

  return (
    <DropDownContainer>
      <DropDownButton onClick={onClickOpen} ref={dropdownRef}>
        {/* 유저아이콘 */}
        <FontAwesomeIcon icon={faUserLarge} size="lg" />
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
