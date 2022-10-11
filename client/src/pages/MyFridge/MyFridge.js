import axios from "axios";
import { useState, useEffect, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import GeneralButton from "../../components/common/Button/GeneralButton";
import InputList from "../../components/common/InputList/InputList";
import { addFrigIngrInput, clearFridge, editFrigIngredients, loadFridge, setDDay, sortByAlphabet, sortByDate } from "../../features/fridgeSlice";
import useConfirm from "../../hooks/useConfirm";
import usePreventLeave from "../../hooks/usePreventLeave";
import { Head, ColumnHeads, Container, Title, Fridge, InputWrapper, InnerContainer, ButtonWrap, SortWrapper, Option, Guide, StyledQuestionMark, SubHead, Help, HelpMessage, StyledBulletPoint } from "./MyFridgeStyle";
import { setWarningToast, setNoticeToast } from "../../features/toastSlice";
import { faCircleQuestion, faUtensils, faBowlFood } from "@fortawesome/free-solid-svg-icons";

const MyFridge = () => {
    const [isEmpty, setIsEmpty] = useState(true); // 재료 없음
    const [sortMode, setSortMode] = useState("date");
    const [serverData, setServerData] = useState([]);
    const titlesArr = ["name", "quantity", "expiration", "dDay", "note"]; //재료 입력에서 각 column의 키값 배열
    const placeholders = ["예) 계란", "예) 30알", "예) 2100/01/01", "", "기타 정보를 작성하세요"]; // 페이지 나갈 때 경고창
    
    const mountRef = useRef(false); // 두번 마운팅 방지 (첫 요청 실패, 두번째는 성공이라 두번째 요청만 살림)
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { enablePrevent, disablePrevent } = usePreventLeave(); // 페이지 나갈 때 경고창 훅

    // 로그인 상태 가져와서 변수에 저장
    const isLoggedIn = useSelector((state) => {
        return state.user.isLoggedIn;
    });

    // access token 상태 가져와서 변수에 저장 (새로고침시 통신 header에 바로 저장되지 않는 에러로 인한 임시 방편)
    const userToken = useSelector((state) => {
        return state.user.userToken;
    });

    useEffect(() => {
        console.log("마운트");
        // 첫 마운트
        if (!mountRef.current) {
            if (!isLoggedIn) {
                // alert창 대체
                dispatch(setWarningToast({ message: "로그인이 필요한 서비스입니다" }));
                navigate("/login");
            }
        }
        if (isLoggedIn) {
            console.log("유저토큰", userToken)
            getFridge();
            enablePrevent(); // 페이지 나가는거 인식해서 경고창 띄우는 함수 실행
        }
        return () => { 
            mountRef.current = true;
            disablePrevent();
        }
    }, [isLoggedIn])

    // 냉장고 재료
    const fridgeData = useSelector((state) => {
        return state.fridge.ingredients;
    });
    // console.log("지금 냉장고 슬라이스", fridgeData)

    // 냉장고 정보 서버에서 받아오기
    const getFridge = async () => {
        try {
            const { data } = await axios.get(`/api/fridge`, {headers: {Authorization: `Bearer ${userToken}`}});
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
            // alert창 대체
            dispatch(setNoticeToast({ message: "냉장고를 청소했어요!" }));
            dispatch(clearFridge());
        }
        catch (error) {
            console.log(error);
            // alert창 대체
            dispatch(setWarningToast({ message: "냉장고를 비우는데 실패했어요ㅠㅠ" }));
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
                // alert창 대체
                dispatch(setWarningToast({ message: `재료 이름은 빈칸이 될 수 없어요ㅠㅠ` }));
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
                // alert창 대체
                dispatch(setNoticeToast({ message: "냉장고를 정리했어요!" }));
                getFridge();
            }
            catch (error) {
                console.log(error);
                // alert창 대체
                dispatch(setWarningToast({ message: "냉장고 정리에 실패했어요ㅠㅠ" }));
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
            <SubHead>
                <Help>
                    <StyledQuestionMark icon={faCircleQuestion}/>
                    <span>도움말</span>
                    <HelpMessage className="modal">
                        <ul>
                            <li>냉장고에 있는 재료를 기록하고 언제 어디서든 재료를 관리해보세요!</li>
                            <li>재료 옆 아이콘( <StyledBulletPoint icon={faUtensils} /> ) 을 클릭하여 해당 재료로 만들 수 있는 레시피를 바로 검색해보세요!</li>
                            <li>우측의 + / - 버튼을 통해 재료를 추가하고 삭제할 수 있습니다</li>
                            <li>재료의 색상은 유통기한에 따라 변합니다.
                                <ul>
                                    <li>
                                        <StyledBulletPoint icon={faBowlFood} className="green"/> &nbsp;
                                        유통기한이 <em>넉넉히</em> 남은 재료입니다 (8일 이상)
                                    </li>
                                    <li>
                                        <StyledBulletPoint icon={faBowlFood} className="yellow"/> &nbsp;
                                        유통기한이 <em>얼마 남지 않았으니</em> 빠른 섭취를 권장합니다 (1일 이상 ~ 7일 이하)
                                    </li>
                                    <li>
                                        <StyledBulletPoint icon={faBowlFood} className="yellow blink"/> &nbsp;
                                        유통기한이 <em>오늘까지</em> 입니다
                                    </li>
                                    <li>
                                        <StyledBulletPoint icon={faBowlFood} className="red"/> &nbsp;
                                        유통기한이 <em>지난</em> 재료입니다
                                    </li>
                                </ul>
                            </li>
                            <li>재료를 수정했다면 하단의 "냉장고 정리" 버튼을 눌러 재료를 저장해주세요!</li>
                        </ul>
                    </HelpMessage>
                </Help>
                <SortingTab />
            </SubHead>
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
                <GeneralButton className="medium" onClick={handleSave}>냉장고 정리</GeneralButton>
            </ButtonWrap>
        </Container>
    )
}

export default MyFridge