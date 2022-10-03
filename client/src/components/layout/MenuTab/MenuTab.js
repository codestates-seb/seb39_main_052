import { useEffect } from "react";
import { useState } from "react";
import { Link, Routes, Route, useParams } from "react-router-dom";
import CommentsBox from "../MyCommentsBox/CommentsBox";
import MyProfile from "../MyProfile/MyProfile";
import MyRecipeBox from "../MyRecipeBox/MyRecipeBox";
import MyRecipeLikedBox from "../MyRecipeLikedBox/MyRecipeLikedBox";
import {
  MenuTabContainer,
  Menu,
  Li,
  MenuTitle,
  MenuContent,
} from "./MenuTabStyle";

const MenuTab = () => {
  //실시간 시간 변환
  function timeSince(date) {
    let seconds = Math.floor((new Date() - date) / 1000);

    let interval = seconds / 31536000;

    if (interval > 1) {
      return Math.floor(interval) + "년 전";
    }
    interval = seconds / 2592000;
    if (interval > 1) {
      return Math.floor(interval) + "달 전";
    }
    interval = seconds / 86400;
    if (interval > 1) {
      return Math.floor(interval) + "일 전";
    }
    interval = seconds / 3600;
    if (interval > 1) {
      return Math.floor(interval) + "시간 전";
    }
    interval = seconds / 60;
    if (interval > 1) {
      return Math.floor(interval) + "분 전";
    }
    // return Math.floor(seconds) + "초 전";
    return "방금";
  }

  const [currentTab, setCurrentTab] = useState(0);

  /* <Route path="/mypage/:detail" element={<MenuTab />} /> */ //App.js Route 부분
  // const { detail } = useParams(); // /mypage/profile 이런식으로 pathname으로 나타내려고했을때
  const { id } = useParams();
  console.log("usParmas아이디값", id);

  const menuArr = [
    {
      name: "내 프로필",
      title: "",
      content: <MyProfile />,
      pathname: "/profile",
      id: 0,
    },
    {
      name: "내 레시피",
      title: "내가 작성한 레시피",
      content: <MyRecipeBox timeSince={timeSince} />,
      pathname: "/written_recipes",
      id: 1,
    },
    {
      name: "내 좋아요",
      title: "내가 좋아한 레시피",
      content: <MyRecipeLikedBox timeSince={timeSince} />,
      pathname: "/loved_recipes",
      id: 2,
    },
    {
      name: "내 받은 댓글",
      title: "내가 받은 댓글",
      content: <CommentsBox timeSince={timeSince} />, //시간 변환 함수 내려주기
      pathname: "/received_comments",
      id: 3,
    },
  ];
  // console.log(menuArr);

  const selectMenuHandler = (idx) => {
    setCurrentTab(idx);
  };

  return (
    <MenuTabContainer>
      <Menu className="menu">
        {menuArr.map((menu, idx) => (
          //기존 menuTab에 useParams 적용 이전
          // <Li
          //   key={idx}
          //   onClick={() => selectMenuHandler(idx)}
          //   isFocused={idx === currentTab}
          // >
          //   {menu.name}
          // </Li>
          <Li key={idx} isFocused={idx === menu.id}>
            {/* 탭 메뉴 요소에 링크달기 */}
            {/* <Link to={`/mypage${menuArr[idx].pathname}`}>{menu.name}</Link> */}
            {/* 링크랑 컴포넌트 탭 안에있는 컨텐트 연결 */}
            <Link to={`/mypage/${menu.id}`}>{menu.name}</Link>
          </Li>
        ))}
      </Menu>
      <MenuTitle>
        {/* {menuArr[currentTab].title} */}
        {menuArr[id].title}
      </MenuTitle>
      <MenuContent className="MenuContent">
        {/* {menuArr[currentTab].content} */}
        {menuArr[id].content}
      </MenuContent>
    </MenuTabContainer>
  );
};

export default MenuTab;
