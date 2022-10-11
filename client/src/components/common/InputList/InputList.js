// 주어진 제목 배열에 대한 다수의 값을 입력할 수 있는 Input 컴포넌트 (열 추가, 삭제 가능)
// 레시피 재료, 냉장고 재료 등에 쓰일 수 있다.
import React from "react";
import { useState, useEffect, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useLocation, useNavigate } from 'react-router-dom'
import { faPlus, faMinus, faUtensils } from "@fortawesome/free-solid-svg-icons";
import { editIngredients, addIngrInput, deleteIngrInput } from "../../../features/recipeSlice";
import { editFrigIngredients, addFrigIngrInput, deleteFrigIngrInput } from "../../../features/fridgeSlice";
import { Block, Order, Input, ButtonWrapper, Button, StyledFontAwesomeIcon, RecipeLinker } from "./InputListStyle";
import { setWarningToast } from "../../../features/toastSlice";

// props로 input의 제목이 될 요소들을 배열의 형태로 받아온다. 
// 예) titlesArr = ["food", "quantity", "expirationDate"] 
// 예) placeholders = ["예) 감자", "예) 100g", "예) 2022/10/10"]
const InputList = ({ titlesArr, placeholders }) => {

    const [showOrder, setShowOrder] = useState(false); //input 앞에 순서 숫자 필요하면 true로 변경
    
    const lastValueRef = useRef(null); // 추가되는 input 박스가 화면 안에 위치할 수 있도록 ref 설정

    const dispatch = useDispatch();
    const navigate = useNavigate();
    // 어느 페이지에서 해당 컴포넌트를 불러오는지 확인
    const { pathname } = useLocation();
    // row 값 (한 열의 데이터 객체를 가지고 있는 배열)
    const data = useSelector((state) => {
        if (pathname === "/recipes/new" || pathname === "/recipes/edit") {
            return state.recipe.ingredients;
        }
        if (pathname === "/myfridge") {
            return state.fridge.ingredients;
        }
    });

    const handleInputChange = (e, i) => {
        const { name, value } = e.target;
        console.log(name, value);

        if (pathname === "/recipes/new" || pathname === "/recipes/edit") {
            dispatch(editIngredients({ index: i, key: name, value: value }));
        }
        if (pathname === "/myfridge") {
            dispatch(editFrigIngredients({ index: i, key: name, value: value }));
        }
    };

    const handleAddClick = (i) => {
        if (pathname === "/recipes/new" || pathname === "/recipes/edit") {
            if (data.length < 15) {
                dispatch(addIngrInput({ index: i }));
            }
            else {
                // alert창 대체
                dispatch(setWarningToast({message: `15개 이하로만 등록할 수 있어요ㅠㅠ`}))
            }
        }
        if (pathname === "/myfridge") {
            if (data.length < 30) {
                dispatch(addFrigIngrInput({ index: i }));
            }
            else {
                // alert창 대체
                dispatch(setWarningToast({message: `30개 이하로만 등록할 수 있어요ㅠㅠ`}))
            }
        }
        // 나의 냉장고 스크롤는 조정되지 않음
        lastValueRef.current.scrollIntoView({
            behavior: 'smooth',
            // block: 'center',
            // inline: 'nearest',
        });
    };

    const handleRemoveClick = (i) => {
        if (pathname === "/recipes/new" || pathname === "/recipes/edit") {
            dispatch(deleteIngrInput({ index: i }));
        }
        if (pathname === "/myfridge") {
            dispatch(deleteFrigIngrInput({ index: i }));
        }
    };

    const classSetter = (el, title) => {

        // 날짜 값이 없는 경우 dDay column은 gray
        if (title === "dDay" && el.dDay === "") return "small gray";
        // dDay가 0일 미만인 경우 red row
        else if (el.dDay < 0) {
            if (title === "dDay") {
                return "small red center";
            }
            else {
                return "small red";
            }
        }
        // dDay가 0일인 경우 yellow row + blink
        else if (el.dDay !== "" && el.dDay === 0) {
            if (title === "dDay") {
                return "small yellow center blink";
            }
            else {
                return "small yellow blink";
            }
        }
        // dDay가 0일 이상 7일 이하인 경우 yellow row
        else if (el.dDay !== "" && el.dDay > 0 && el.dDay <= 7) {
            if (title === "dDay") {
                return "small yellow center";
            }
            else {
                return "small yellow";
            }
        }
        // dDay가 7일 이상인 경우 green row
        else if (el.dDay > 7) {
            if (title === "dDay") {
                return "small green center";
            }
            else {
                return "small green";
            }
        }
        // 그 외 경우의 수는 기본 색상
        else return "small";

    }

    // 디데이 글자 컨버터 (4 => D-4, -4 => D+4)
    const dDayDisplay = (dDay) => {
        let display;
        if (dDay === "") {
            display = "";
        }
        else if (dDay >= 0) {
            display = "D-" + dDay;
        }
        else {
            display = "D+" + Math.abs(dDay);
        }
        return display;
    }

    // input 창 내 아이콘 클릭시 해당 재료가 있는 레시피 검색
    const handleClickFridge = (tag) => {
        if (tag.length > 0) {
            navigate(`/search?tags=${tag}`)
        }
    }

    return (
        <>
            {data.map((el, idx) => {
                return (
                    <Block key={idx} id="row" ref={idx === data.length -1 ? lastValueRef : null}>
                        {showOrder && <Order htmlFor="row">{idx + 1}</Order>}
                        {titlesArr.map((title, index) => {
                            return (
                                <React.Fragment key={index}>
                                    {title === "name" && pathname === "/myfridge"
                                        && <RecipeLinker onClick={() => handleClickFridge(el[title])} title={el[title]+"(으)로 레시피 검색하기"}>
                                            <StyledFontAwesomeIcon icon={faUtensils} className={el[title].length > 0 ? "utensils hover" : "utensils"}/>
                                        </RecipeLinker>
                                    }
                                    <Input
                                        type={title === "expiration" ? "date" : "text"}
                                        maxLength='12'
                                        name={title}
                                        value={title === "dDay" ? dDayDisplay(el[title]) : el[title]}
                                        placeholder={placeholders[index]}
                                        onChange={e => handleInputChange(e, idx)}
                                        readOnly={title === "dDay" ? true : false} // D-Day 값은 입력/수정 안되도록
                                        // className={title === "dDay" ? "small gray" : "small"}
                                        // className={el.dDay > 0 ? "small red" : "small"}
                                        className={classSetter(el, title)}
                                    />
                                </React.Fragment>
                            )
                        })}
                        <ButtonWrapper>
                            {/* input이 하나만 있을 때는 지우는 버튼이 나타나지 않는다 */}
                            {data.length > 1 &&
                                <Button
                                    onClick={() => handleRemoveClick(idx)}
                                >
                                    <StyledFontAwesomeIcon icon={faMinus} className="delete" />
                                </Button>}
                            {/* 마지막 input에만 추가 옵션이 생긴다 */}
                            {data.length - 1 === idx &&
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

export default InputList;