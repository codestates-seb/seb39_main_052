// 페이지 이동마다 스크롤이 상단에 올 수 있도록 하는 컴포넌트
// 해당 컴포넌트 없이는 페이지 이동 시 전 페이지의 스크롤 위치와 동일함
import { useEffect } from "react";
import { useLocation } from "react-router-dom";

export default function ScrollToTop(props) {

    const { pathname } = useLocation();
    console.log(pathname)

    useEffect(() => {
        console.log("여기!!!!!!!!!!!!", window)
        window.scrollTo({top: 0, left: 0, behavior: 'instant'});
    }, [pathname]);

    return <>{props.children}</>;
}