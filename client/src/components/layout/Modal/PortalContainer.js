import { createPortal } from "react-dom";

//모달창 최상위에서 보여주기 위함. 부모 컴포넌트의 DOM 계층 밖에 있는 DOM 노드로 자식을 렌더링을하는 best way

const PortalContainer = ({ children }) => {
  return createPortal(<>{children}</>, document.getElementById("modal"));
};

export default PortalContainer;
