import { useEffect, useRef } from "react";
import { useSelector, useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import RecipeEditor from "../../components/layout/RecipeEditor/RecipeEditor";
import { PageName } from "./EditRecipeStyle";
import usePreventLeave from "../../hooks/usePreventLeave";
import { setWarningToast } from "../../features/toastSlice";

const EditRecipe = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const mountRef = useRef(false); // 두번 마운팅 방지
    const { enablePrevent, disablePrevent } = usePreventLeave(); // 페이지 나갈 때 경고창 훅

    // 로그인 상태 가져와서 변수에 저장
    const isLoggedIn = useSelector((state) => {
        return state.user.isLoggedIn;
    });

    // 로그아웃 상태로 페이지 접속 시 로그인 창으로 navigate
    useEffect(() => {
        if (!mountRef.current) {
            console.log("마운트")
            if (!isLoggedIn) {
                // alert창 대체
                dispatch(setWarningToast({ message: `로그인이 필요한 서비스입니다` }));
                navigate("/login");
            }
        }
        enablePrevent(); // 페이지 나가는거 인식해서 경고창 띄우는 함수 실행
        return () => { 
            mountRef.current = true;
            disablePrevent();
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