// 사진과 Input 한 항목에 대해 값을 입력할 수 있는 컴포넌트 (요리순서에 쓰인다)
import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import ImageUploader from "../../common/ImageUploader/ImageUploader";
import { faPlus, faMinus } from "@fortawesome/free-solid-svg-icons";
import { editSteps, addStepsInput, deleteStepsInput } from "../../../features/recipeSlice";
import { Block, Order, Input, ButtonWrapper, Button, StyledFontAwesomeIcon } from "./ImageInputListStyle";

const ImageInputList = () => {
    
    const dispatch = useDispatch();

    const steps = useSelector((state) => {
        return state.recipe.steps;
    });
    // console.log(`steps`, steps)

    const handleInputChange = (e, i) => {
        const { name, value } = e.target;
        dispatch(editSteps({index: i, key: name, value: value}))
    };

    const handleAddClick = (i) => {
        dispatch(addStepsInput({index: i}));
    };

    const handleRemoveClick = (i) => {
        dispatch(deleteStepsInput({index: i}));
    };

    return (
        <>
            {steps.map((el, idx) => {
                return (
                    <Block key={idx} id="row">
                        {/* 순서 번호 */}
                        <Order htmlFor="row">{idx+1}</Order>
                        {/* 요리 순서별 이미지 */}
                        <ImageUploader imgPostApi={``} size={`small`} />
                        {/* 요리 순서 내용 */}
                        <Input
                            type='text'
                            maxLength='200'
                            name="content"
                            placeholder="예) 김치를 잘게 썰어 중불의 팬에 기름을 두르고 5분정도 볶는다"
                            onChange={e => handleInputChange(e, idx)}
                        />
                        {/* 추가 삭제 버튼 */}
                        <ButtonWrapper>
                            {/* input이 하나만 있을 때는 지우는 버튼이 나타나지 않는다 */}
                            {steps.length > 1 &&
                                <Button
                                    onClick={() => handleRemoveClick(idx)}
                                >
                                    <StyledFontAwesomeIcon icon={faMinus} className="delete" />
                                </Button>}
                            {/* 마지막 input에만 추가 옵션이 생긴다 */}
                            {steps.length - 1 === idx &&
                                <Button
                                    onClick={() => handleAddClick(idx)}
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