import axios from "axios";
import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import GeneralButton from "../../components/common/Button/GeneralButton";
import InputList from "../../components/common/InputList/InputList";
import { clearFridge, setDDay } from "../../features/fridgeSlice";
import useConfirm from "../../hooks/useConfirm";
import { Head, ColumnHeads, Container, Title, Fridge, InputWrapper, InnerContainer, ButtonWrap } from "./MyFridgeStyle";

const MyFridge = () => {
    const [dateArr, setDateArr] = useState([]);
    const titlesArr = ["name", "amount", "date", "dDay", "note"]; //재료 입력에서 각 column의 키값 배열
    const placeholders = ["예) 계란", "예) 30알", "예) 2100/01/01", "", "기타 정보를 작성하세요"];
    const dispatch = useDispatch();

    // const dDayCalculator = (date) => {
    //     const setDate = new Date (date);
    //     const setDateDay = setDate.getDate();

    //     // 현재 날짜를 new 연산자를 사용해서 Date 객체를 생성
    //     const now = new Date();

    //     const gap = setDateDay- now.getDate();
    //     console.log(gap);

    //     return gap;
    // } 
    
    // const fridgeData = useSelector((state) => {
    //     return state.fridge.ingredients;
    // });
    // console.log(fridgeData);

    // useEffect(() => {
    //     for (let i = 0; i < fridgeData.length; i++) {
    //         setDateArr(...dateArr, fridgeData[i].date);
    //         console.log(`요기여 포문`)
    //     }  
    // }, [fridgeData])
    
    // console.log(dateArr);

    // useEffect (() => {
    //     for (let i = 0; i < dateArr.length; i++) {
    //         let dDay = dDayCalculator(fridgeData[i]);
    //         dispatch(setDDay({index: 0, key: "dDay", value: dDay}))
    //         console.log(`dday`, dDay)
    //     }
    // }, [dateArr])

    // 냉장고 비우기
    const confirm = (id) => {handleDeleteAll(id)};
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

    const handleCancel = () => {
        window.location.replace("/myfridge");
    }

    const handleSave = () => [
        alert("냉장고를 정리하였습니다!")
    ]

    return (
        <Container>
            <Title>나의 냉장고</Title>
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