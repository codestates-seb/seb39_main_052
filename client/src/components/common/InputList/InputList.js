import { useState, useEffect } from "react";
import { faPlus, faMinus } from "@fortawesome/free-solid-svg-icons";
import { Block, Input, ButtonWrapper, Button, StyledFontAwesomeIcon } from "./InputListStyle";

// props로 input의 제목이 될 요소들을 배열의 형태로 받아온다. 예) titlesArr = ["food", "amount", "expirationDate"]
const Ingredients = ({ titlesArr }) => {

    // props로 받아온 제목 요소가 배열의 key가 되어 value에 input 값을 할당할 수 있는 객체를 만든다. 예) titlesObj = {food: "", amount: "", expirationDate: ""}
    let titlesObj = {};
    titlesArr.map((title) => {
        return titlesObj[title] = "";
    })

    const [inputList, setInputList] = useState([titlesObj]);

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
                    <Block key={idx}>
                        {titlesArr.map((title, index) => {
                            return (
                                <Input
                                    key={index}
                                    name={title}
                                    value={el[title]}
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