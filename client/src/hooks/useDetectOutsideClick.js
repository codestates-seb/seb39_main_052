import { useEffect, useState } from "react";

//드롭다운 메뉴에서 elRef는 dropdownRef될거고 initialState는 false될것
const useDetectOutsideClick = (elRef, initialState) => {
  const [isOpen, setIsOpen] = useState(initialState);

  useEffect(() => {
    const onClick = (e) => {
      if (elRef.current !== null && !elRef.current.contains(e.target)) {
        setIsOpen(!isOpen);
      }
    };

    if (isOpen) {
      window.addEventListener("click", onClick);
    }
    return () => {
      window.removeEventListener("click", onClick);
    };
  }, [isOpen, elRef]);

  return [isOpen, setIsOpen];
};

export default useDetectOutsideClick;
