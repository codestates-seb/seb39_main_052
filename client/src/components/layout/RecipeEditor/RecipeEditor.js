import { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import axios from "axios";
import ImageUploader from "../../common/ImageUploader/ImageUploader";
import InputList from "../../common/InputList/InputList";
import ImageInputList from "../ImageInputList/ImageInputList";
import { Container, Header, Warning, Main, ImageWrap, Input, Select, Ingredients, Steps, Portion, Time, Button } from "./RecipeEditorStyle";
import { setTitle, setImagePath, setPortion, setTime, clearRecipe } from "../../../features/recipeSlice";

const RecipeEditor = () => {
    const [isTitleEmpty, setIsTitleEmpty] = useState(false);
    const [isTimeEmpty, setIsTimeEmpty] = useState(false);
    const [isIngrEmpty, setIsIngrEmpty] = useState(false);
    const [isStepsEmpty, setIsStepsEmpty] = useState(false);

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
        if (!isTitleEmpty && !isTimeEmpty && !isIngrEmpty && !isStepsEmpty) {
            axios.post('/api/recipes', recipe, {
                // headers: {
                //     'Content-Type': 'multipart/form-data'
                // }
            })
            .then((response) => {
                // 응답 처리
                console.log(response);
            })
            .catch((error) => {
                // 예외 처리
                console.log(error);
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
                    <ImageUploader imgPostApi={imgPostApi} size={`big`} />
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
                <ImageInputList />
                <Warning className={isStepsEmpty? null : "invisible"}>요리 순서를 최소 하나 이상 입력해주세요</Warning>
            </Steps>
            <Button className="large" >취소하기</Button>
            <Button className="large" onClick={handleSaveClick}>저장하기</Button>
        </ Container>
    )
};

export default RecipeEditor;
