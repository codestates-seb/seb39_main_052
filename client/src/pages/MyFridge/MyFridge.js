import axios from "axios";
import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import GeneralButton from "../../components/common/Button/GeneralButton";
import InputList from "../../components/common/InputList/InputList";
import { clearFridge, setDDay } from "../../features/fridgeSlice";
import useConfirm from "../../hooks/useConfirm";
import { Head, ColumnHeads, Container, Title, Fridge, InputWrapper, InnerContainer, ButtonWrap, SortWrapper, Option } from "./MyFridgeStyle";

const MyFridge = () => {
    const [isEmpty, setIsEmpty] = useState(true); 
    const [sortMode, setSortMode] = useState("date");
    const titlesArr = ["name", "quantity", "date", "dDay", "note"]; //재료 입력에서 각 column의 키값 배열
    const placeholders = ["예) 계란", "예) 30알", "예) 2100/01/01", "", "기타 정보를 작성하세요"];
    const dispatch = useDispatch();

    // 냉장고 재료
    const fridgeData = useSelector((state) => {
        return state.fridge.ingredients;
    });

    // 냉장고 비우기
    const confirm = (id) => { handleDeleteAll(id) };
    const cancel = () => console.log("취소");
    const handleDeleteAll = async (id) => {
        // axios({
        //     method: `patch`,
        //     url: `/api/comments/${id}`,
        //     data: {comment: comment},
        // })
        // .then((response) => {
        //     console.log(response)
        //     alert(`냉장고를 비웠습니다.`)
        // })
        // .catch((error) => {
        //     // 예외 처리
        //     console.log(error.response);
        // })
        dispatch(clearFridge());
        alert("냉장고를 비웠습니다.")
    }
    // 냉장고 취소
    const handleCancel = () => {
        window.location.replace("/myfridge");
    }
    // 냉장고 정리 끝
    const handleSave = () => {
        let count = 0; // 모든 재료 이름 값이 있는지 확인하기 위한 카운트
        for (let i = 0; i < fridgeData.length; i++) {
            if (fridgeData[i].name.length <= 0) {
                alert(`재료 이름은 빈칸이 될 수 없습니다.`);
                break;
            }
            count++
        }
        if (count === fridgeData.length) {
            alert("냉장고를 정리하였습니다!")
        }
    }

    // 냉장고 재료 정렬 컴포넌트
    const SortingTab = () => {
        const sortOption = [{
            mode: "date",
            button: "유통기한순"
        }, {
            mode: "alphabet",
            button: "가나다순"
        }]

        const handleClick = (mode) => {
            setSortMode(mode);
        }

        return (
            <SortWrapper>
                {sortOption.map((option, idx) => {
                    return (
                        <Option
                            key={idx}
                            onClick={() => { handleClick(option.mode) }}
                            className={sortMode === option.mode && "selected"}
                        >
                            {option.button}
                        </Option>
                    )
                })}
            </SortWrapper>
        )
    }

    return (
        <Container>
            <Title>나의 냉장고</Title>
            <SortingTab />
            <Fridge>
                <InnerContainer>
                    <ColumnHeads>
                        <Head>재료</Head>
                        <Head>양</Head>
                        <Head>유통기한</Head>
                        <Head>D-Day</Head>
                        <Head>비고</Head>
                    </ColumnHeads>
                    <InputWrapper>
                        <InputList titlesArr={titlesArr} placeholders={placeholders} />
                    </InputWrapper>
                </InnerContainer>
            </Fridge>
            <ButtonWrap>
                {/* 아래 onClick 13값은 변경 예정 */}
                <GeneralButton className="medium gray" onClick={handleCancel}>취소</GeneralButton>
                <GeneralButton className="medium" onClick={useConfirm("정말 비우시겠습니까? 확인시 냉장고 정보를 되돌릴 수 없습니다.", confirm, cancel, 13)}>냉장고 비우기</GeneralButton>
                <GeneralButton className="medium" onClick={handleSave}>냉장고 정리 끝</GeneralButton>
            </ButtonWrap>
        </Container>
    )
}

export default MyFridge