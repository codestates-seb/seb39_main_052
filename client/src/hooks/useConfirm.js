import { useEffect } from "react";

const useConfirm = (message= "", onConfirm, onCancel) => {
    // useEffect(() => {
    //     console.log(`hi`);
    // },[])
    if (!onConfirm || typeof onConfirm !== "function") {
        return;
    }
    if (onCancel && typeof onCancel !== "function") {
        return;
    }
    const confirmAction = () => {
        if (window.confirm(message)) {
            onConfirm();
        }
        else {
            onCancel();
        }
    };
    return confirmAction;
};

export default useConfirm;

// 컴포넌트 내 사용법 예시
// import useConfirm from "../../../hooks/useConfirm";

// const confirm = () => console.log("삭제 했습니다");
// const cancel = () => console.log("취소");
// const handleSubmit = useConfirm("정말 삭제하시겠습니까?", confirm, cancel);
