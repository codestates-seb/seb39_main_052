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

const MenuTab = () => {
  const [currentTab, setCurrentTab] = useState(0);
  const menuArr = [
    { name: "내 프로필", title: "", content: <MyProfile /> },
    {
      name: "내 레시피",
      title: "내가 작성한 레시피",
      content: <MyRecipeBox />,
    },
    {
      name: "내 좋아요",
      title: "내가 좋아한 레시피",
      content: <MyRecipeLikedBox />,
    },
    { name: "내 받은 댓글", title: "내가 받은 댓글", content: <CommentsBox /> },
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
