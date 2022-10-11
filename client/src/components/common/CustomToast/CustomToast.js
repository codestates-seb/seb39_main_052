import { useSelector, useDispatch } from "react-redux";
import { useState, useEffect } from "react";
import { Box, Container, Text, StyledFontAwesomeIcon } from "./CustomToastStyle"
import { closeToast } from "../../../features/toastSlice";
import { faExclamation, faCheck } from "@fortawesome/free-solid-svg-icons";

// mode는 warning 또는 notice
const CustomToast = () => {
    // 렌더링 후 초기 상태는 토스트 토스트 열리는 애니메이션
    const [toastAnimation, setToastAnimation] = useState("openAnimation");
    const dispatch = useDispatch();
    
    const toast = useSelector((state) => {
        return state.toast;
    })

    useEffect(() => {
        let toastTimer;
        // 2초 후 토스트 사라지는 애니메이션
        // 그 후 .5초 후 토스트 보여주는 상태를 false로 변경
        const animationTimer = setTimeout (() => {
            setToastAnimation("closeAnimation")
            toastTimer = setTimeout (() => {
                dispatch(closeToast());
            }, 500)
        }, 2000)
        return () => {
            clearTimeout(toastTimer);
            clearTimeout(animationTimer)
        };
    }, [])

    return (
        <Container className={toastAnimation}>
            <Box 
                bgColor={toast.mode === "warning" ? "var(--red-075)" : "var(--green-100)"} 
            >
                {toast.mode === "warning" && <StyledFontAwesomeIcon icon={faExclamation} className="red" />}
                {toast.mode === "notice" && <StyledFontAwesomeIcon icon={faCheck} className="green" />}
                <Text>{toast.message}</Text>
            </Box>
        </Container>
    )
}

export default CustomToast