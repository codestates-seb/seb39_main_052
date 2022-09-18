// 주어진 제목 배열에 대한 다수의 값을 입력할 수 있는 Input 컴포넌트 (열 추가, 삭제 가능)
// 레시피 재료, 냉장고 재료 등에 쓰일 수 있다.
import { useState, useEffect } from "react";
import { faPlus, faMinus } from "@fortawesome/free-solid-svg-icons";
import { Block, Order, Input, ButtonWrapper, Button, StyledFontAwesomeIcon } from "./InputListStyle";

// props로 input의 제목이 될 요소들을 배열의 형태로 받아온다. 
// 예) titlesArr = ["food", "amount", "expirationDate"] 
// 예) placeholders = ["예) 감자", "예) 100g", "예) 2022/10/10"]
const Ingredients = ({ titlesArr, placeholders }) => {

    // props로 받아온 제목 요소가 배열의 key가 되어 value에 input 값을 할당할 수 있는 객체를 만든다. 예) titlesObj = {food: "", amount: "", expirationDate: ""}
    let titlesObj = {};
    titlesArr.map((title) => {
        return titlesObj[title] = "";
    })

    const [inputList, setInputList] = useState([titlesObj]);
    const [showOrder, setShowOrder] = useState(false);

    // 첫번째 input 열을 비워둔 상태에서 새로운 input을 추가했을 때 둘의 값이 동기화 되는 에러를 임시로 고치기 위해 작성한 코드 (useState에 대한 공부가 추가적으로 필요할 듯)
    useEffect(() => {
        const list = [...inputList];
        setInputList(list);
    }, []);


    const handleInputChange = (e, i) => {
        const { name, value } = e.target;
        const list = [...inputList];
        list[i][name] = value;
        setInputList(list);
    };

    const handleAddClick = () => {
        setInputList([...inputList, titlesObj]);
    };

    const handleRemoveClick = (i) => {
        const list = [...inputList];
        list.splice(i, 1);
        setInputList(list);
    };

    return (
        <>
            {inputList.map((el, idx) => {
                return (
                    <Block key={idx} id="row">
                        {showOrder && <Order htmlFor="row">{idx+1}</Order>}
                        {titlesArr.map((title, index) => {
                            return (
                                <Input
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
                            {inputList.length > 1 &&
                                <Button
                                    onClick={() => handleRemoveClick(idx)}
                                >
                                    <StyledFontAwesomeIcon icon={faMinus} className="delete" />
                                </Button>}
                            {/* 마지막 input에만 추가 옵션이 생긴다 */}
                            {inputList.length - 1 === idx &&
                                <Button
                                    
                                    onClick={handleAddClick}
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