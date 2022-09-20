import { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import axios from "axios";
import ImageUploader from "../../common/ImageUploader/ImageUploader";
import InputList from "../../common/InputList/InputList";
import ImageInputList from "../ImageInputList/ImageInputList";
import { Container, Header, Warning, Main, ImageWrap, Input, Select, Ingredients, Steps, Portion, Time, Button, ButtonWrap } from "./RecipeEditorStyle";
import { setTitle, setImagePath, setPortion, setTime, clearRecipe } from "../../../features/recipeSlice";

const RecipeEditor = ({ editMode }) => {
    const token = `Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqd3QtYWNjZXNzLXRva2VuIiwiaWQiOjIsImV4cCI6MTY2MzY1NzE0OX0.pN8Vi_Aoy2KpuGdxd6pnYUgJIvc7SJyTXHbhI5Vm-ZLhZXYf2HPwIJytameU-t9dfnwfzAID_EbUKzFoA0C7ug`;
    const refresh = `refresh-token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqd3QtcmVmcmVzaC10b2tlbiIsImV4cCI6MTY2MzczMjQ3OX0.HSggtx9AuoGKi7IZBozhgAtlNXdltqKao6f3onf5kWJrj9Ck4LJ56qapb9gsMpNMIVSgIcIMZ_sVebM-3Jbn1A`;

    const [isTitleEmpty, setIsTitleEmpty] = useState(false);
    const [isTimeEmpty, setIsTimeEmpty] = useState(false);
    const [isIngrEmpty, setIsIngrEmpty] = useState(false);
    const [isStepsEmpty, setIsStepsEmpty] = useState(false);

    const [stepFiles, setStepFiles] = useState([null]);

    const titlesArr = ["food", "amount"]; //재료 입력에서 각 column의 키값 배열
    const placeholders = ["예) 감자", "예) 100g"];
    const portionOptions = Array.from({length: 10}, (_, i) => i + 1); //인분 선택 dropdown
    let imgPostApi = ``;

    const dispatch = useDispatch();

    // 전체 레시피 데이터
    const recipe = useSelector((state) => {
        return state.recipe;
    });
    // console.log(recipe);

    // 전체 레시피 데이터
    const files = useSelector((state) => {
        return state.images;
    });
    console.log(files);

    // 재료 유효성 경고 창 뜬 후 재작성 했을 때 유효하다면 경고창 없애기 
    useEffect(() => {
        if (recipe.ingredients[0].food.length > 0) {
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

    const handleSaveClick = () => {
        // 필수 데이터 유효성 검사
        recipe.title.length > 0 ? setIsTitleEmpty(false) : setIsTitleEmpty(true);
        recipe.time.length > 0 ? setIsTimeEmpty(false) : setIsTimeEmpty(true);
        recipe.ingredients[0].food.length > 0 && recipe.ingredients[0].amount.length > 0 
            ? setIsIngrEmpty(false) 
            : setIsIngrEmpty(true);
        recipe.steps[0].content.length > 0 ? setIsStepsEmpty(false) : setIsStepsEmpty(true);

        // 유효성 검사 통과시 서버에 데이터 전달
        // 재료 부분 구현 후
        // if (!isTitleEmpty && !isTimeEmpty && !isIngrEmpty && !isStepsEmpty) {
        // 재료 부분 구현 전
        if (!isTitleEmpty && !isTimeEmpty && !isStepsEmpty) {
            const formData = new FormData();

            for (let i = 0; i < files.length; i++) {
                formData.append('files', files[i]);
            }

            formData.append('requestBody', new Blob([JSON.stringify(recipe)], {
                type: "application/json"
            }))

            axios({
                method: 'post',
                url: '/api/recipes/image',
                headers: {
                    'Authorization': token,
                    'Content-Type': 'multipart/form-data',
                },
                data: formData,
            })
            .then((response) => {
                // 응답 처리
                console.log(response);
            })
            .catch((error) => {
                // 예외 처리
                console.log(error.response);
            })

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
                    <h1>새 레시피 작성하기</h1>
                    <Input 
                        placeholder="제목을 입력해주세요" 
                        className="large" 
                        type='text' 
                        maxLength='24'
                        onChange={(e) => {
                            dispatch(setTitle({title: e.target.value}));
                            e.target.value.length > 0 ? setIsTitleEmpty(false) : setIsTitleEmpty(true);
                        }}
                    />
                    <Warning className={isTitleEmpty? null : "invisible"}>제목을 입력해주세요</Warning>
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
                            onChange={(e) => {
                                dispatch(setTime({time: e.target.value}));
                                e.target.value.length > 0 ? setIsTimeEmpty(false) : setIsTimeEmpty(true);
                            }}
                            />
                            분
                        </div>
                        <Warning className={isTimeEmpty? null : "invisible"}>소요시간을 입력해주세요</Warning>
                    </Time>
                </Main>
                <ImageWrap>
                    <ImageUploader imgPostApi={imgPostApi} size={`big`} mode={`main`} />
                </ImageWrap>
            </Header>
            <Ingredients>
                <h2>재료 (계량)</h2>
                <InputList 
                    titlesArr={titlesArr} 
                    placeholders={placeholders}
                />
                <Warning className={isIngrEmpty? null : "invisible"}>재료와 계량을 모두 입력해주세요</Warning>
            </Ingredients>
            <Steps>
                <h2>요리 순서</h2>
                <ImageInputList  stepFiles={stepFiles} setStepFiles={setStepFiles}/>
                <Warning className={isStepsEmpty? null : "invisible"}>요리 순서를 최소 하나 이상 입력해주세요</Warning>
            </Steps>
            <ButtonWrap>
                {/* 작성페이지에서 취소시 메인페이지로 연결 */}
                <Button className="large" >취소하기</Button>
                <Button className="large" onClick={handleSaveClick}>게시하기</Button>
            </ButtonWrap>
            <ButtonWrap>
                {/* 수정페이지에서 취소시 레시피 상세 페이지로 연결  */}
                <Button className="large" >취소하기</Button>
                <Button className="large" onClick={handleSaveClick}>수정하기</Button>
            </ButtonWrap>
        </ Container>
    )
};

export default RecipeEditor;
