import axios from "axios";
import { useState, useEffect, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import GeneralButton from "../../components/common/Button/GeneralButton";
import InputList from "../../components/common/InputList/InputList";
import { addFrigIngrInput, clearFridge, editFrigIngredients, loadFridge, setDDay, sortByAlphabet, sortByDate } from "../../features/fridgeSlice";
import useConfirm from "../../hooks/useConfirm";
import { Head, ColumnHeads, Container, Title, Fridge, InputWrapper, InnerContainer, ButtonWrap, SortWrapper, Option } from "./MyFridgeStyle";

const MyFridge = () => {
    const [isEmpty, setIsEmpty] = useState(true); 
    const [sortMode, setSortMode] = useState("date");
    const [serverData, setServerData] = useState([]);
    const titlesArr = ["name", "quantity", "expiration", "dDay", "note"]; //재료 입력에서 각 column의 키값 배열
    const placeholders = ["예) 계란", "예) 30알", "예) 2100/01/01", "", "기타 정보를 작성하세요"];
    
    const mountRef = useRef(true); // 두번 마운팅 방지 (첫 요청 실패, 두번째는 성공이라 두번째 요청만 살림)
    const dispatch = useDispatch();
    const navigate = useNavigate();

    // 로그인 상태 가져와서 변수에 저장
    const isLoggedIn = useSelector((state) => {
        return state.user.isLoggedIn;
    });

    useEffect(() => {
        // 첫 마운트
        if (mountRef.current) {
            if (!isLoggedIn) {
                navigate("/login");
                alert("로그인이 필요한 서비스입니다");
            }
        }
        // 두번째 마운트
        else {
            getFridge();
        }
        return () => { mountRef.current = false; }
    }, [isLoggedIn])

    // 냉장고 재료
    const fridgeData = useSelector((state) => {
        return state.fridge.ingredients;
    });
    // console.log("지금 냉장고 슬라이스", fridgeData)

    // 냉장고 정보 서버에서 받아오기
    const getFridge = async () => {
        try {
            const { data } = await axios.get(`/api/fridge`);
            const { fridgeIngredients } = data;
            console.log(data);
            // console.log("서버 냉장고재료", fridgeIngredients);
            // 서버에 재료 추가
            // 냉장고 초기 상태 또는 청소 후 빈 객체를 받아오는 경우 상태 변경하지 않도록
            if (fridgeIngredients.length > 0 && fridgeIngredients[0].name.length > 0) {
                dispatch(loadFridge({ ingredients: [...fridgeIngredients]}));
            }
            // console.log("성공");

        }
        catch (error) {
            console.log(error);
            dispatch(clearFridge());
            // console.log("실패");
            // alert("냉장고 정보를 가져오는데 실패했어요ㅠㅠ")
        }
    }

    // 냉장고 비우기
    const confirm = (id) => { handleDeleteAll(id) };
    const cancel = () => console.log("취소");
    const handleDeleteAll = async (id) => {
        const payload = { fridgeIngredients: []}
        try {
            const data = await axios.post(`/api/fridge`, payload);
            console.log(data);
            alert("냉장고를 청소했어요!")
            dispatch(clearFridge());
        }
        catch (error) {
            console.log(error);
            alert("냉장고를 비우는데 실패했어요ㅠㅠ")
        }
    }
    // 냉장고 취소 (새로고침)
    const handleCancel = () => {
        window.location.replace("/myfridge");
    }
    // 냉장고 정리 끝
    const handleSave = async() => {
        let count = 0; // 모든 재료 이름 값이 있는지 확인하기 위한 카운트
        for (let i = 0; i < fridgeData.length; i++) {
            if (fridgeData[i].name.length <= 0) {
                alert(`재료 이름은 빈칸이 될 수 없어요ㅠㅠ`);
                break;
            }
            count++
        }
        // 유효성 통과 후 업데이트된 냉장고 정보 POST
        if (count === fridgeData.length) {
            const payload = { fridgeIngredients: fridgeData }
            console.log("리퀘스트바디", payload);
            try {
                const data = await axios.post(`/api/fridge`, payload);
                console.log(data);
                alert("냉장고를 정리했어요!")
                getFridge();
            }
            catch (error) {
                console.log(error);
                alert("냉장고 정리에 실패했어요ㅠㅠ")
            }
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
            if (mode === "alphabet") {
                dispatch(sortByAlphabet());
            }
            else {
                dispatch(sortByDate());
            }
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
                <GeneralButton className="medium" onClick={useConfirm("정말 비우는건가요? 확인시 냉장고 상태를 되돌릴 수 없어요.", confirm, cancel)}>냉장고 비우기</GeneralButton>
                <GeneralButton className="medium" onClick={handleSave}>냉장고 정리 끝</GeneralButton>
            </ButtonWrap>
        </Container>
    )
}

export default MyFridge