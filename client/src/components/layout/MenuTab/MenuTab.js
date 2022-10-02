import { useEffect } from "react";
import { useState } from "react";
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

// !! useParams 냉장고파먹기에서 상세페이지로 갈떄 처럼
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
  const menuArr = [
    { name: "내 프로필", title: "", content: <MyProfile /> },
    {
      name: "내 레시피",
      title: "내가 작성한 레시피",
      content: <MyRecipeBox timeSince={timeSince} />,
    },
    {
      name: "내 좋아요",
      title: "내가 좋아한 레시피",
      content: <MyRecipeLikedBox timeSince={timeSince} />,
    },
    {
      name: "내 받은 댓글",
      title: "내가 받은 댓글",
      content: <CommentsBox timeSince={timeSince} />, //시간 변환 함수 내려주기
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
          <Li
            key={idx}
            onClick={() => selectMenuHandler(idx)}
            isFocused={idx === currentTab}
          >
            {menu.name}
          </Li>
        ))}
      </Menu>
      <MenuTitle>{menuArr[currentTab].title}</MenuTitle>
      <MenuContent className="MenuContent">
        {menuArr[currentTab].content}
      </MenuContent>
    </MenuTabContainer>
  );
};

export default MenuTab;
