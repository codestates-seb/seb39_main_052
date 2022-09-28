import { useState } from "react";
import CommentsBox from "../MyCommentsBox/CommentsBox";
import MyProfile from "../MyProfile/MyProfile";
import MyRecipeBox from "../MyRecipeBox/MyRecipeBox";
import MyRecipeLikedBox from "../MyRecipeLikedBox/MyRecipeLikedBox";
import { MenuTabContainer, Menu, Li, MenuContent } from "./MenuTabStyle";

const MenuTab = () => {
  const [currentTab, setCurrentTab] = useState(0);
  const menuArr = [
    { name: "내 프로필", content: <MyProfile /> },
    { name: "내 레시피", content: <MyRecipeBox /> },
    { name: "내 좋아요", content: <MyRecipeLikedBox /> },
    { name: "내 받은 댓글", content: <CommentsBox /> },
  ];
  // console.log(menuArr);

  const selectMenuHandler = (idx) => {
    setCurrentTab(idx);
  };

  return (
    <MenuTabContainer>
      <Menu>
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
      <MenuContent className="MenuContent">
        {menuArr[currentTab].content}
      </MenuContent>
    </MenuTabContainer>
  );
};

export default MenuTab;
