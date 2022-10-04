import { useState, useEffect } from "react";
import { Link, useLocation } from 'react-router-dom'
import GeneralButton from "../../common/Button/GeneralButton";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPencil } from "@fortawesome/free-solid-svg-icons";
import { ButtonWrapper, Text } from "./FloatingActionStyle";

const FloatingAction = () => {

    const [ isNotNeeded, setIsNotNeeded ] = useState(false);
    const { pathname } = useLocation();

    // 레시피 작성하기/수정하기 경로에서는 사라지도록 하기
    useEffect(() => {
        if (pathname === "/recipes/new" || pathname === "/recipes/edit" ) {
            setIsNotNeeded(true);
        }
        else {
            setIsNotNeeded(false);
        }
    }, [pathname])

    return (
        <ButtonWrapper className={isNotNeeded ? "invisible" : null}>
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