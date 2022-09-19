// 주어진 제목 배열에 대한 다수의 값을 입력할 수 있는 Input 컴포넌트 (열 추가, 삭제 가능)
// 레시피 재료, 냉장고 재료 등에 쓰일 수 있다.
import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { faPlus, faMinus } from "@fortawesome/free-solid-svg-icons";
import { editIngredients, addIngrInput, deleteIngrInput } from "../../../features/recipeSlice";
import { Block, Order, Input, ButtonWrapper, Button, StyledFontAwesomeIcon } from "./InputListStyle";

// props로 input의 제목이 될 요소들을 배열의 형태로 받아온다. 
// 예) titlesArr = ["food", "amount", "expirationDate"] 
// 예) placeholders = ["예) 감자", "예) 100g", "예) 2022/10/10"]
const Ingredients = ({ titlesArr, placeholders }) => {

    const [showOrder, setShowOrder] = useState(false); //input 앞에 순서 숫자 필요하면 true로 변경

    const dispatch = useDispatch();

    const ingredients = useSelector((state) => {
        return state.recipe.ingredients;
    });
    // console.log(`ingr`, ingredients)

    const handleInputChange = (e, i) => {
        const { name, value } = e.target;
        dispatch(editIngredients({index: i, key: name, value: value}));
    };

    const handleAddClick = (i) => {
        dispatch(addIngrInput({index: i}));
    };

    const handleRemoveClick = (i) => {
        dispatch(deleteIngrInput({index: i}));
    };

    return (
        <>
            {ingredients.map((el, idx) => {
                return (
                    <Block key={idx} id="row">
                        {showOrder && <Order htmlFor="row">{idx+1}</Order>}
                        {titlesArr.map((title, index) => {
                            return (
                                <Input
                                    type='text'
                                    maxLength='20'
                                    key={index}
                                    name={title}
                                    value={el[title]}
                                    placeholder={placeholders[index]}
                                    onChange={e => handleInputChange(e, idx)}
                                    className="small"
                                />
                            )
                        })}
                        <ButtonWrapper>
                            {/* input이 하나만 있을 때는 지우는 버튼이 나타나지 않는다 */}
                            {ingredients.length > 1 &&
                                <Button
                                    onClick={() => handleRemoveClick(idx)}
                                >
                                    <StyledFontAwesomeIcon icon={faMinus} className="delete" />
                                </Button>}
                            {/* 마지막 input에만 추가 옵션이 생긴다 */}
                            {ingredients.length - 1 === idx &&
                                <Button
                                    
                                    onClick={() => handleAddClick(idx)}
                                >
                                    <StyledFontAwesomeIcon icon={faPlus} className="add"/>
                                </Button>}
                        </ButtonWrapper>
                    </ Block>
                )
            })}
        </>
    )
};

export default Ingredients;