import { useEffect, useRef } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import RecipeEditor from "../../components/layout/RecipeEditor/RecipeEditor";
import { PageName } from "./EditRecipeStyle";

const EditRecipe = () => {
    const navigate = useNavigate();
    const mountRef = useRef(false); // 두번 마운팅 방지

    // 로그인 상태 가져와서 변수에 저장
    const isLoggedIn = useSelector((state) => {
        return state.user.isLoggedIn;
    });

    // 로그아웃 상태로 페이지 접속 시 로그인 창으로 navigate
    useEffect(() => {
        if (!mountRef.current) {
            console.log("마운트")
            if (!isLoggedIn) {
                navigate("/login");
                alert("로그인이 필요한 서비스입니다");
            }
        }
        return () => { mountRef.current = true; console.log("언마운트")}
    }, [isLoggedIn])

    return (
        <>  
            <PageName>레시피 수정하기</PageName>
            <RecipeEditor />
        </>
    )
};

export default EditRecipe;