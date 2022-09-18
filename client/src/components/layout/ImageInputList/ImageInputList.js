// 사진과 Input 한 항목에 대해 값을 입력할 수 있는 컴포넌트 (요리순서에 쓰인다)
import { useState, useEffect } from "react";
import ImageUploader from "../../common/ImageUploader/ImageUploader";
import { faPlus, faMinus } from "@fortawesome/free-solid-svg-icons";
import { Block, Order, Input, ButtonWrapper, Button, StyledFontAwesomeIcon } from "./ImageInputListStyle";

const ImageInputList = () => {
    const [inputList, setInputList] = useState([{content: ""}]);

    const handleInputChange = (e, i) => {
        const { name, value } = e.target;
        const list = [...inputList];
        list[i][name] = value;
        setInputList(list);
        console.log(inputList)
    };

    const handleAddClick = () => {
        setInputList([...inputList, {content: ""}]);
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
                        {/* 순서 번호 */}
                        <Order htmlFor="row">{idx+1}</Order>
                        {/* 요리 순서별 이미지 */}
                        <ImageUploader imgPostApi={``} size={`small`} />
                        {/* 요리 순서 내용 */}
                        <Input 
                            name="content"
                            placeholder="예) 김치를 잘게 썰어 중불의 팬에 기름을 두르고 5분정도 볶는다"
                            onChange={e => handleInputChange(e, idx)}
                        />
                        {/* 추가 삭제 버튼 */}
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
                                    <StyledFontAwesomeIcon icon={faPlus} className="add" />
                                </Button>}
                        </ButtonWrapper>
                    </Block>
                )
            })}
        </>
    )
};

export default ImageInputList;