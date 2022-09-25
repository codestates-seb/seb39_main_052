import { useState } from "react";
import { MenuTabContainer, Menu, Li, MenuContent } from "./MenuTabStyle";

const MenuTab = () => {
  const [currentTab, setCurrentTab] = useState(0);
  const menuArr = [
    { name: "내 프로필", content: "1" },
    { name: "내 레시피", content: "2" },
    { name: "내 좋아요", content: "3" },
    { name: "내 받은 댓글", content: "4" },
  ];
  const selectMenuHandler = (idx) => {
    setCurrentTab(idx);
  };

  return (
    <MenuTabContainer>
      <Menu>
        {menuArr.map((menu, idx) => (
          <Li
            onClick={() => selectMenuHandler(idx)}
            isFocused={idx === currentTab}
          >
            {menu.name}
          </Li>
        ))}
      </Menu>
      <MenuContent>
        <p>{menuArr[currentTab].content}</p>
      </MenuContent>
    </MenuTabContainer>
  );
};

export default MenuTab;
