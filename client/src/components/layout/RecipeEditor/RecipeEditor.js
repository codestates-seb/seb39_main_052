import { useState, useEffect } from "react";
import { useLocation, useNavigate } from 'react-router-dom'
import { useSelector, useDispatch } from "react-redux";
import axios from "axios";
import ImageUploader from "../../common/ImageUploader/ImageUploader";
import InputList from "../../common/InputList/InputList";
import ImageInputList from "../ImageInputList/ImageInputList";
import { Container, Header, Warning, Main, ImageWrap, Input, Select, Ingredients, Steps, Portion, Time, Button, ButtonWrap } from "./RecipeEditorStyle";
import GeneralButton from "../../common/Button/GeneralButton";
import { setTitle, setPortion, setTime, clearRecipe } from "../../../features/recipeSlice";
import { clearImages } from "../../../features/imageSlice";

const RecipeEditor = () => {

    const [isTitleEmpty, setIsTitleEmpty] = useState(true);
    const [isMainImgEmpty, setIsMainImgEmpty] = useState(true);
    const [isStepImgEmpty, setIsStepImgEmpty] = useState(true);
    const [isTimeEmpty, setIsTimeEmpty] = useState(true);
    const [isIngrEmpty, setIsIngrEmpty] = useState(true);
    const [isStepsEmpty, setIsStepsEmpty] = useState(true);
    const [isSubmitClicked, setIsSubmitClicked] = useState(false);

    const titlesArr = ["name", "amount"]; //재료 입력에서 각 column의 키값 배열
    const placeholders = ["예) 감자", "예) 100g"];
    const portionOptions = Array.from({length: 10}, (_, i) => i + 1); //인분 선택 dropdown

    const dispatch = useDispatch();
    const navigate = useNavigate();

    // 이제 프롭스로 받을 필요 없어요!
    const { pathname } = useLocation();

    // 페이지 나가면 작성 데이터 남지 않도록
    useEffect(() => {
        // mount
        
        return() => {
          // unmount
          dispatch(clearRecipe());
          dispatch(clearImages());
        }
    }, [])

    // 레시피 상세 데이터 (글)
    const recipe = useSelector((state) => {
        return state.recipe;
    });
    console.log(`recipe`, recipe);

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
    const recipeSteps = useSelector((state) => {
        return state.recipe.steps;
    });
    // console.log(recipeSteps);

    // 재료 유효성 경고 창 뜬 후 재작성 했을 때 유효하다면 경고창 없애기 
    useEffect(() => {
        if (recipe.ingredients[0].name.length > 0) {
            recipe.ingredients[0].amount.length > 0
                ? setIsIngrEmpty(false)
                : setIsIngrEmpty(true);
        }
    }, [recipe.ingredients])

    // 요리 순서 유효성 경고 창 뜬 후 재작성 했을 때 유효하다면 경고창 없애기 
    useEffect(() => {
        if (recipe.steps[0].content.length > 0) {
            recipe.steps[0].content.length > 0 ? setIsStepsEmpty(false) : setIsStepsEmpty(true);
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

    const handleSaveClick = () => {
        setIsSubmitClicked(true);

        // 필수 데이터 유효성 검사
        recipe.title.length > 0 ? setIsTitleEmpty(false) : setIsTitleEmpty(true);
        recipe.time.length > 0 ? setIsTimeEmpty(false) : setIsTimeEmpty(true);
        recipe.ingredients[0].name.length > 0 && recipe.ingredients[0].amount.length > 0 
            ? setIsIngrEmpty(false) 
            : setIsIngrEmpty(true);
        recipe.steps[0].content.length > 0 ? setIsStepsEmpty(false) : setIsStepsEmpty(true);

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

        // 유효성 검사 통과시 서버에 데이터 전달
        if (!isTitleEmpty && !isTimeEmpty && !isIngrEmpty && !isStepsEmpty && !isMainImgEmpty &&!isStepImgEmpty) {

            const formData = new FormData(); //서버에 전달될 폼데이터

            //폼데이터에 이미지 파일 개별적으로 추가
            for (let i = 0; i < files.length; i++) {
                formData.append('files', files[i]);
            }
            //폼데이터에 레시피 데이터 객체(이미지 제외)로 추가
            formData.append('requestBody', new Blob([JSON.stringify(recipe)], {
                type: "application/json"
            }))
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
                    alert(`성공적으로 게시되었습니다`)
                })
                .catch((error) => {
                    // 예외 처리
                    console.log(error.response);
                    formData.delete('files');
                    formData.delete('requestBody');
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
                    // navigate(`/recipes/${response.data.id}`);
                    formData.delete('files');
                    formData.delete('requestBody');
                    alert(`성공적으로 수정되었습니다`)
                })
                .catch((error) => {
                    // 예외 처리
                    console.log(error.response);
                    formData.delete('files');
                    formData.delete('requestBody');
                })
            }
        };
    }

    // 인분 수 선택하는 드랍다운
    const SelectBox = ({ defaultValue }) => {

        const handleChange = (e) => {
            dispatch(setPortion({portion: e.target.value}))
        }

        return (
            <Select onChange={handleChange}>
                {portionOptions.map((option) => {
                    return (
                        <option
                            key={option}
                            value={option}
                            defaultValue={defaultValue === option}
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
                            e.target.value.length > 0 ? setIsTitleEmpty(false) : setIsTitleEmpty(true);
                        }}
                    />
                    {isSubmitClicked && <Warning className={isTitleEmpty ? null : "invisible"}>제목을 입력해주세요</Warning>}
                    <Portion>
                        <h2>양</h2>
                        <div>
                            <SelectBox defaultValue={1} />
                            인분
                        </div>
                    </Portion>
                    <Time>
                        <h2>소요 시간</h2>
                        <div>
                            <Input
                                className="small"
                                type='text'
                                maxLength='3'
                                value={recipe.time}
                                onChange={(e) => {
                                    dispatch(setTime({ time: e.target.value }));
                                    e.target.value.length > 0 ? setIsTimeEmpty(false) : setIsTimeEmpty(true);
                                }}
                                // 숫자만 입력 가능하도록
                                onKeyDown={(e) => {
                                    if (!/[0-9]/.test(e.key)) {
                                        e.preventDefault();
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
                {isSubmitClicked && <Warning className={isStepsEmpty ? null : "invisible"}>요리 순서를 최소 하나 이상 입력해주세요</Warning>}
                {isSubmitClicked && <Warning className={isStepImgEmpty? null : "invisible"}>순서별 사진을 업로드해주세요</Warning>}
            </Steps>
            {pathname === "/recipes/new" && 
                <ButtonWrap>
                    {/* 작성페이지에서 취소시 메인페이지로 연결 */}
                    <GeneralButton className="medium gray" onClick={() => navigate("/")} >취소하기</GeneralButton>
                    <GeneralButton className="medium" onClick={handleSaveClick}>게시하기</GeneralButton>
                </ButtonWrap>
            }
            {pathname === "/recipes/edit" &&
                <ButtonWrap>
                    {/* 수정페이지에서 취소시 레시피 상세 페이지로 연결 예정 */}
                    <GeneralButton className="medium gray" >취소하기</GeneralButton>
                    <GeneralButton className="medium" onClick={handleSaveClick}>수정하기</GeneralButton>
                </ButtonWrap>
            }
        </ Container>
    )
};

export default RecipeEditor;