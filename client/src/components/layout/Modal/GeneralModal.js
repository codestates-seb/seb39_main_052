import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faXmark } from "@fortawesome/free-solid-svg-icons";
import GeneralButton from "../../common/Button/GeneralButton";
import {
  Overlay,
  ModalContainer,
  CloseButton,
  Contents,
} from "./GeneralModalStyle";

import { useEffect } from "react";
import PortalContainer from "./PortalContainer";

const GeneralModal = ({ handleClose, children, width, height }) => {
  //모달창 외부 화면 스크롤 막기
  useEffect(() => {
    document.body.style.cssText = `
      position: fixed; 
      top: -${window.scrollY}px;
      overflow-y: scroll;
      width: 100%;`;
    return () => {
      const scrollY = document.body.style.top;
      document.body.style.cssText = "";
      window.scrollTo(0, parseInt(scrollY || "0", 10) * -1);
    };
  }, []);

  return (
    <PortalContainer>
      <Overlay>
        <ModalContainer
          onClick={(e) => e.stopPropagation()}
          width={width}
          height={height}
        >
          {" "}
          {/* stopPropagation 안쓰면 이벤트 버블링으로 엑스버튼말고도 화면 모두 누르면 닫힘 */}
          <CloseButton onClick={handleClose}>
            <FontAwesomeIcon icon={faXmark} size="xl" />
          </CloseButton>
          <Contents>{children}</Contents>
        </ModalContainer>
      </Overlay>
    </PortalContainer>
  );
};

export default GeneralModal;
