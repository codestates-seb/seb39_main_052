import { useEffect } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import RecipeEditor from "../../components/layout/RecipeEditor/RecipeEditor";
import { PageName } from "./EditRecipeStyle";

const EditRecipe = () => {
    const navigate = useNavigate();

    // 로그인 상태 가져와서 변수에 저장
    const isLoggedIn = useSelector((state) => {
        return state.user.isLoggedIn;
    });

    useEffect(() => {
        if (!isLoggedIn) {
            alert("로그인이 필요한 서비스입니다");
            navigate("/login");
        }
    }, [isLoggedIn])

    return (
        <>  
            <PageName>레시피 수정하기</PageName>
            <RecipeEditor />
        </>
    )
};

export default EditRecipe;