import { useState, useEffect } from "react";
import { Link, useLocation } from 'react-router-dom'
import GeneralButton from "../../common/Button/GeneralButton";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPencil } from "@fortawesome/free-solid-svg-icons";
import { ButtonWrapper, Text } from "./FloatingActionStyle";

const FloatingAction = ({ isBottom }) => {
    console.log(isBottom);
    const [ divClass, setDivClass ] = useState("");
    const [ isNotNeeded, setIsNotNeeded ] = useState(false);
    const { pathname } = useLocation();

    // 레시피 작성하기/수정하기, 로그인, 회원가입 경로에서는 사라지도록 하기
    useEffect(() => {
        if (pathname === "/recipes/new"
            || pathname === "/recipes/edit"
            || pathname === "/login"
            || pathname === "/signup") {
            setIsNotNeeded(true);
        }
        else {
            setIsNotNeeded(false);
        }
    }, [pathname])

    // 페이지 스크롤 변경 시 footer 가리지 않도록 위치 변경
    useEffect(() => {
        if (isBottom) {
            setDivClass("bottom");
        }
        else {
            setDivClass("");
        }
    }, [isBottom])

    return (
        <ButtonWrapper className={isNotNeeded ? "invisible" : divClass}>
            <Link to="/recipes/new">
                <Text>레시피 작성하기</Text>
                <GeneralButton className={"round large shadow"}>
                    <FontAwesomeIcon icon={faPencil}/>
                </GeneralButton>
            </Link>
        </ButtonWrapper>
    )
};

export default FloatingAction;