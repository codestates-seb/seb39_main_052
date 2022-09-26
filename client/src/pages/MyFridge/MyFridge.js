import axios from "axios";
import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import GeneralButton from "../../components/common/Button/GeneralButton";
import InputList from "../../components/common/InputList/InputList";
import { clearFridge, setDDay } from "../../features/fridgeSlice";
import useConfirm from "../../hooks/useConfirm";
import { Head, ColumnHeads, Container, Title, Fridge, InputWrapper, InnerContainer, ButtonWrap, SortWrapper, Option } from "./MyFridgeStyle";

const MyFridge = () => {
    const [dateArr, setDateArr] = useState([]);
    const [sortMode, setSortMode] = useState(['date']);
    const titlesArr = ["name", "amount", "date", "dDay", "note"]; //재료 입력에서 각 column의 키값 배열
    const placeholders = ["예) 계란", "예) 30알", "예) 2100/01/01", "", "기타 정보를 작성하세요"];
    const dispatch = useDispatch();

    // const fridgeData = useSelector((state) => {
    //     return state.fridge.ingredients;
    // });
    // console.log(fridgeData);

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
    const handleSave = () => [
        alert("냉장고를 정리하였습니다!")
    ]

    // 냉장고 재료 정렬
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
                        // className="selected"
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