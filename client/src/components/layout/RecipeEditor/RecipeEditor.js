// 레시피 작성하기, 수정하기 기능 비활성화
import { useState, useEffect } from "react";
import { useLocation, useNavigate } from 'react-router-dom'
import { useSelector, useDispatch } from "react-redux";
import axios from "axios";
import ImageUploader from "../../common/ImageUploader/ImageUploader";
import InputList from "../../common/InputList/InputList";
import ImageInputList from "../ImageInputList/ImageInputList";
import { Container, Header, Warning, Main, ImageWrap, Input, Select, Ingredients, Steps, Portion, Time, ButtonWrap, Button } from "./RecipeEditorStyle";
import GeneralButton from "../../common/Button/GeneralButton";
import { setTitle, setPortion, setTime, clearRecipe } from "../../../features/recipeSlice";
import { clearImages } from "../../../features/imageSlice";
import Footer from "../Footer/Footer";
import { setNoticeToast, setWarningToast } from "../../../features/toastSlice";

const RecipeEditor = () => {

    const [isTitleEmpty, setIsTitleEmpty] = useState(true);
    const [isMainImgEmpty, setIsMainImgEmpty] = useState(true);
    const [isStepImgEmpty, setIsStepImgEmpty] = useState(true);
    const [isTimeEmpty, setIsTimeEmpty] = useState(true);
    const [isIngrEmpty, setIsIngrEmpty] = useState(true);
    const [isStepsEmpty, setIsStepsEmpty] = useState(true);
    const [isSubmitClicked, setIsSubmitClicked] = useState(false); // 첫 요청 이후 onChange마다 유효성 검사를 위한 상태
    const [isRequestNeeded, setIsRequestNeeded] = useState(false); // 클릭시 요청이 필요한지 여부 상태

    const titlesArr = ["name", "quantity"]; //재료 입력에서 각 column의 키값 배열
    const placeholders = ["예) 감자", "예) 100g"];
    const portionOptions = Array.from({length: 10}, (_, i) => i + 1); //인분 선택 dropdown

    const dispatch = useDispatch();
    const navigate = useNavigate();

    // 해당 컴포넌트를 사용하는 페이지가 새 레시피 작성하기인지 레시피 수정하기인지 구분
    const { pathname } = useLocation();

    // 새로 작성 페이지면 빈 input 창이 뜨도록
    useEffect(() => {
        // mount
        if (pathname === "/recipes/new") {
            dispatch(clearRecipe());
            dispatch(clearImages());
        }
        return () => {
            // // unmount
            // if (pathname === "/recipes/new") {
            //     dispatch(clearRecipe());
            //     dispatch(clearImages());
            // }
        }
    }, [])

    // 레시피 상세 데이터 (글)
    // 구조 분해 할당
    // 나머지는 .찍어서 쓰기
    const { recipe } = useSelector((state) => {
        return state;
    });
    // console.log(`redux 레시피`, recipe);

    // 레시피 내 모든 이미지 데이터
    const files = useSelector((state) => {
        return state.images;
    });
    // console.log(files);

    // 레시피 id값
    const recipeId = useSelector((state) => {
        return state.recipe.id;
    });
    // console.log(recipeId);


    // 레시피 id값
    // const { id, steps } = useSelector((state) => {
    //     return state.recipe;
    // });

    // 레시피 순서
    const recipeSteps = useSelector((state) => {
        return state.recipe.steps;
    });
    // console.log(recipeSteps);

    // 재료 유효성 경고 창 뜬 후 재작성 했을 때 유효하다면 경고창 없애기 
    useEffect(() => {
        if (isSubmitClicked) {
            // 모든 재료가 빈칸이 없는지
            for (let i = 0; i < recipe.ingredients.length; i++) {
                if (recipe.ingredients[i].name.length <= 0 || recipe.ingredients[i].quantity.length <= 0) {
                    setIsIngrEmpty(true);
                    break;
                }
                // 재료에 빈칸만 쓴 경우는 비어진 것으로 처리
                else if (!notOnlySpaces(recipe.ingredients[i].name)) {
                    setIsIngrEmpty(true);
                    break;
                }
                // 계량에 빈칸만 쓴 경우는 비어진 것으로 처리
                else if (!notOnlySpaces(recipe.ingredients[i].quantity)) { 
                    setIsIngrEmpty(true);
                    break;
                }
                setIsIngrEmpty(false);
            }
        }
    }, [recipe.ingredients])

    // 요리 순서 유효성 경고 창 뜬 후 재작성 했을 때 유효하다면 경고창 없애기
    useEffect(() => {
        // 요리 순서에 빈칸이 없는지
        for  (let i = 0; i < recipe.steps.length; i++) {
            if (recipe.steps[i].content <= 0) {
                setIsStepsEmpty(true);
                break;
            }
            // 요리 순서에 빈칸만 작성한 경우
            else if (!notOnlySpaces(recipe.steps[i].content)) {
                setIsStepsEmpty(true);
                break;
            }
            setIsStepsEmpty(false);
        }
    }, [recipe.steps])

    // 이미지 관련 경고 창 뜬 후 재업로드 했을 때 유효하다면 경고창 없애기 
    useEffect(() => {
        files[0] === null ? setIsMainImgEmpty(true) : setIsMainImgEmpty(false);

        for (let i = 1; i < files.length; i++) {
            if (files[i] === null) {
                setIsStepImgEmpty(true);
                break;
            }
            setIsStepImgEmpty(false);
        }
    }, [files])

    // 게시하기 클릭 시 (유효성 검사 후) 요청 보내기
    useEffect(() => {
        if (isRequestNeeded) {
            // 유효성 검사 통과시 서버에 데이터 전달
            if (!isTitleEmpty && !isTimeEmpty && !isIngrEmpty && !isStepsEmpty && !isMainImgEmpty && !isStepImgEmpty) {

                const formData = new FormData(); //서버에 전달될 폼데이터

                //폼데이터에 이미지 파일 개별적으로 추가
                for (let i = 0; i < files.length; i++) {
                    formData.append('files', files[i]);
                }
                //폼데이터에 레시피 데이터 객체(이미지 제외)로 추가
                formData.append('requestBody', new Blob([JSON.stringify(recipe)], {
                    type: "application/json"
                }))

                console.log("폼데이터", formData.files);
                // POST 요청 - 새 레시피 작성하기
                if (pathname === "/recipes/new") {
                    axios({
                        method: "post",
                        url: '/api/recipes',
                        headers: {
                            'Content-Type': 'multipart/form-data',
                        },
                        data: formData,
                    })
                        .then((response) => {
                            // 응답 처리
                            console.log(response);
                            // navigate(`/recipes/${response.data.id}`);
                            formData.delete('files');
                            formData.delete('requestBody');
                            dispatch(clearRecipe());
                            dispatch(clearImages());
                            // alert창 대체
                            dispatch(setNoticeToast({ message: `레시피를 등록했어요!` }))
                            navigate(`/recipes/${response.data.id}`)
                        })
                        .catch((error) => {
                            // 예외 처리
                            console.log(error.response);
                            formData.delete('files');
                            formData.delete('requestBody');
                            // alert창 대체
                            dispatch(setWarningToast({ message: `레시피 등록에 실패했어요ㅠㅠ` }))
                        })
                }
                // else로 patch 요청
                // // PATCH 요청 - 레시피 수정하기
                if (pathname === "/recipes/edit") {
                    axios({
                        method: "patch",
                        url: `/api/recipes/${recipeId}`,
                        headers: {
                            'Content-Type': 'multipart/form-data',
                        },
                        data: formData,
                    })
                        .then((response) => {
                            // 응답 처리
                            formData.delete('files');
                            formData.delete('requestBody');
                            dispatch(clearRecipe());
                            dispatch(clearImages());
                            // alert창 대체
                            dispatch(setNoticeToast({ message: "성공적으로 수정했어요!" }))
                            navigate(`/recipes/${response.data.id}`)
                        })
                        .catch((error) => {
                            // 예외 처리
                            console.log(error.response);
                            formData.delete('files');
                            formData.delete('requestBody');
                            // alert창 대체
                            dispatch(setWarningToast({ message: `레시피를 수정할 수 없어요ㅠㅠ` }))
                        })
                }
            }
            else if (isTitleEmpty || isTimeEmpty || isIngrEmpty || isStepsEmpty || isMainImgEmpty || isStepImgEmpty) {
                // alert창 대체
                dispatch(setWarningToast({ message: `입력하지 않은 칸이 있어요!` }))
            }
        }
        setIsRequestNeeded(false); // 요청 보냈으니 다시 상태 초기화
    }, [isRequestNeeded])

    // 유효성 검사
    const checkValidation = () => {

        // 필수 데이터 유효성 검사 (길이가 0 이상이고 전체 텍스트가 띄어쓰기로만 이루어진게 아닌 경우)
        recipe.title.length > 0 && notOnlySpaces(recipe.title) ? setIsTitleEmpty(false) : setIsTitleEmpty(true);
        recipe.time.length > 0 ? setIsTimeEmpty(false) : setIsTimeEmpty(true);
        // 재료에 빈칸이 없는지
        for (let i = 0; i < recipe.ingredients.length; i++) {
            if (recipe.ingredients[i].name.length <= 0 || recipe.ingredients[i].quantity.length <= 0) {
                setIsIngrEmpty(true);
                break;
            }
            // 재료에 빈칸만 쓴 경우는 비어진 것으로 처리
            else if (!notOnlySpaces(recipe.ingredients[i].name)) {
                setIsIngrEmpty(true);
                break;
            }
            // 계량에 빈칸만 쓴 경우는 비어진 것으로 처리
            else if (!notOnlySpaces(recipe.ingredients[i].quantity)) {
                setIsIngrEmpty(true);
                break;
            }
            setIsIngrEmpty(false);  
        }
        // 요리 순서에 빈칸이 없는지
        for  (let i = 0; i < recipe.steps.length; i++) {
            if (recipe.steps[i].content <= 0) {
                setIsStepsEmpty(true);
                break;
            }
            // 요리 순서에 빈칸만 작성한 경우
            else if (!notOnlySpaces(recipe.steps[i].content)) {
                setIsStepsEmpty(true);
                break;
            }
            setIsStepsEmpty(false);
        }
        // recipe.steps[0].content.length > 0 ? setIsStepsEmpty(false) : setIsStepsEmpty(true);

        // 메인 이미지 유효성 검사
        // 새 레시피 작성하기 
        if (pathname === "/recipes/new") {
            files[0] === null ? setIsMainImgEmpty(true) : setIsMainImgEmpty(false);
        }
        // 레시피 수정하기
        else {
            if (files[0] === null && recipe.imageInfo.isUpdated === "Y") {
                setIsMainImgEmpty(true)
            }
            else {
                setIsMainImgEmpty(false)
            }
        }
        // 요리순서 이미지 유효성 검사
        // 새 레시피 작성하기 
        if (pathname === "/recipes/new") {
            for (let i = 1; i < files.length; i++) {
                if (files[i] === null) {
                    setIsStepImgEmpty(true);
                    break;
                }
                setIsStepImgEmpty(false);
            }
        }
        // 레시피 수정하기
        else {
            let counter = 0;
            for (let i = 1; i < files.length; i++) {
                if (files[i] !== null) {
                    counter++
                }
            }
            for (let i = 0; i < recipeSteps.length; i++) {
                if (recipeSteps[i].imageInfo.isUpdated === "N") {
                    counter++
                }
            }
            // 수정 시 새로 등록된 파일 수와 수정되지 않은 이미지의 합이 총 요리순서 step의 수보다 작은 경우
            // "순서별 사진을 업로드해주세요" 메세지가 뜨도록 한다
            counter < recipeSteps.length ? setIsStepImgEmpty(true) : setIsStepImgEmpty(false);
        }
    }    

    // 게시하기 클릭 시 유효성 검사, 요청 필요 여부 상태 바꾸기
    const handleSaveClick = () => {
        checkValidation();
        setIsRequestNeeded(true);
        setIsSubmitClicked(true);
    }

    // 작성된 모든 문자열이 띄어쓰기만으로 이루어진게 아닌지 확인하는 함수 (true는 글이 있다, false는 띄어쓰기 뿐이다)
    const notOnlySpaces = (text) => {
        for (let i = 0; i < text.length; i++) {
            if (text[i] !== " ") {
                return true;
            }
        }
        return false;
    }

    // 인분 수 선택하는 드랍다운
    const SelectBox = ({ defaultValue }) => {

        const handleChange = (e) => {
            dispatch(setPortion({portion: e.target.value}))
        }

        return (
            <Select onChange={handleChange} value={recipe.portion}>
                {portionOptions.map((option) => {
                    return (
                        <option
                            key={option}
                            value={option}
                            // defaultValue={defaultValue === option}
                        >
                            {option}
                        </option>
                    )
                })}
            </Select>
        )
    }

    return (
        <Container>
            <Header>
                <Main>
                    <Input
                        placeholder="제목을 입력해주세요"
                        className="large"
                        type='text'
                        maxLength='24'
                        value={recipe.title}
                        onChange={(e) => {
                            dispatch(setTitle({ title: e.target.value }));
                            e.target.value.length > 0 && notOnlySpaces(e.target.value) ? setIsTitleEmpty(false) : setIsTitleEmpty(true);
                        }}
                    />
                    {isSubmitClicked && <Warning className={isTitleEmpty ? null : "invisible"}>제목을 입력해주세요</Warning>}
                    <Portion>
                        <h2>양</h2>
                        <div>
                            <SelectBox />
                            인분
                        </div>
                    </Portion>
                    <Time>
                        <h2>소요 시간</h2>
                        <div>
                            <Input
                                className="small noSideBar"
                                type='number'
                                maxLength={3}
                                value={recipe.time}
                                step={1}
                                onChange={(e) => {
                                    // type이 num으로 바뀌며 maxLength가 적용되지 않아 직접 limit을 줘야한다
                                    const limit = 3;
                                    // 음수 값은 입력할 수 없다, 숫자가 0으로 시작할 수도 없다
                                    if (e.target.value >= 0 && e.target.value[0] !== "0") {
                                        dispatch(setTime({ time: e.target.value.slice(0, limit) }));
                                        e.target.value.length > 0 ? setIsTimeEmpty(false) : setIsTimeEmpty(true);
                                    }
                                }}
                            />
                            분
                        </div>
                        {isSubmitClicked && <Warning className={isTimeEmpty ? null : "invisible"}>소요시간을 입력해주세요</Warning>}
                    </Time>
                </Main>
                <ImageWrap>
                    <ImageUploader size={`big`} mode={`main`} />
                    {isSubmitClicked && <Warning className={isMainImgEmpty? null : "invisible"}>메인 이미지를 입력해주세요</Warning>}
                </ImageWrap>
            </Header>
            <Ingredients>
                <h2>재료 (계량)</h2>
                <InputList 
                    titlesArr={titlesArr} 
                    placeholders={placeholders}
                />
                {isSubmitClicked && <Warning className={isIngrEmpty? null : "invisible"}>재료와 계량을 모두 입력해주세요</Warning>}
            </Ingredients>
            <Steps>
                <h2>요리 순서</h2>
                <ImageInputList />
                {isSubmitClicked && <Warning className={isStepsEmpty ? null : "invisible"}>요리 순서는 빈칸이 될 수 없습니다. 최소 하나 이상 입력해주세요</Warning>}
                {isSubmitClicked && <Warning className={isStepImgEmpty? null : "invisible"}>순서별 사진을 업로드해주세요</Warning>}
            </Steps>
            {pathname === "/recipes/new" && 
                <ButtonWrap>
                    {/* 작성페이지에서 취소시 메인페이지로 연결 */}
                    <GeneralButton className="medium gray" onClick={() => navigate("/")} >취소하기</GeneralButton>
                    <GeneralButton 
                        className="medium" 
                        // onClick={handleSaveClick}
                    >게시하기</GeneralButton>
                </ButtonWrap>
            }
            {pathname === "/recipes/edit" &&
                <ButtonWrap>
                    {/* 수정페이지에서 취소시 레시피 상세 페이지로 연결 예정 */}
                    <GeneralButton className="medium gray" onClick={() => navigate(-1)}>취소하기</GeneralButton>
                    <GeneralButton 
                        className="medium" 
                        // onClick={handleSaveClick}
                    >수정하기</GeneralButton>
                </ButtonWrap>
            }
        </ Container>
    )
};

export default RecipeEditor;