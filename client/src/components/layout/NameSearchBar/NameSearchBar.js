import { useState, useEffect, useRef } from "react";
import { useSearchParams } from "react-router-dom";
import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";
import { Container, DropDown, SearchBar, SearchInput, StyledFontAwesomeIcon, Suggestion } from "./NameSearchBarStyle";
import axios from "axios";

const NameSearchBar = () => {
    
    const [searchParams, setSearchParams] = useSearchParams();
    const [searchValue, setSearchValue] = useState("");
    const [suggestedValue, setSuggestedValue] = useState([]);
    const [isDropDownOpen, setIsDropDownOpen] = useState(false);
    const [cursor, setCursor] = useState(-1);
    
    // console.log("드롭다운은 켜졌니?", isDropDownOpen);
    // console.log("서치파람스 값은?", searchParams);
    // console.log("검색어 예정은?", searchValue);
    // if (searchValue !== "" && searchValue !== null) {    console.log("검색어 예정길이는?", searchValue.length);}
    // console.log("추천 검색어는? ", suggestedValue);
    // console.log("커서는? ", cursor);

    const searchBarRef = useRef(); // 서치바+드롭다운 창 밖 클릭을 인식하기 위한 ref
    const suggestionRef = useRef(null); // 스크롤이 드롭다운 내 선택된 요소를 따라가게 하기 위한 ref

    // 관련 검색어 불러오기
    const getDropDownValue = async (keyword) => {
        if (keyword.length > 0) {
            try {
                const { data } = await axios.get(`/api/recipes/titles?word=${keyword}`);
                let tmp = [...data.data]; // 정렬을 위한 tmp 배열 선언
                tmp.sort((a,b) => a.length - b.length); // 문자열 길이 오름차순 정렬
                setSuggestedValue([...tmp]);
                // 연관 검색어가 있다면 드랍다운을 열고 없다면 닫기
                data.data.length > 0 ? setIsDropDownOpen(true) : setIsDropDownOpen(false);
            }
            catch (error) {
                console.log(error);
            }
        }
    }

    // 드랍다운에서 위치 바뀌면 검색 값 변경 + 스크롤이 드롭다운 내 선택된 요소를 따라가게 하기
    useEffect(() => {
        if (isDropDownOpen) {
            setSearchValue(suggestedValue[cursor]);
            if (cursor > -1) {
                suggestionRef.current.scrollIntoView({
                    behavior: 'smooth',
                    block: 'center',
                    inline: 'nearest',
                });
            }
        }
    }, [cursor])

    // 외부 클릭 인식할 수 있는 Event Listener 추가/삭제
    useEffect(() => {
        if (isDropDownOpen) {
            document.addEventListener('mousedown', handleClickOutside);
        }
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        }
    });

    // 검색어 입력시 연관 검색어 보여주는 함수
    const handleInput = (e) => {
        setCursor(-1); // 새 검색어 입력시 커서 위치 초기화
        if (e.target.value.length <= 0) {
            setIsDropDownOpen(false);
            setSuggestedValue([]);
        }
        setSearchValue(e.target.value);
        getDropDownValue(e.target.value);
    };

    // 방향키에 따라 드롭다운 내 이동
    const handleKeyUp = (e) => {
        if (isDropDownOpen) {
            if (e.key === 'ArrowDown') {
                // 아래 suggestedValue를 추후 연관검색어로 변경 예정
                isDropDownOpen &&
                    setCursor((prev) => (prev < suggestedValue.length - 1 ? prev + 1 : prev));
            }
            else if (e.key === 'ArrowUp') {
                isDropDownOpen &&
                    setCursor((prev) => (prev > 0 ? prev - 1 : 0));
            }
            else if (e.key === 'Escape') {
                setCursor(-1);
                setIsDropDownOpen(false);
                setSuggestedValue([]);
            }
        }
        else {
            if (e.key === 'ArrowDown') {
                setIsDropDownOpen(true); 
            }
        }
    }

    // 엔터키에 대한 핸들러 (엔터 두번 눌러지는걸 방지하기 위해 onKeyUp과 분리)
    const handleKeyPress = (e) => {
        if (e.key === 'Enter') {
            handleSearch();
            setSuggestedValue([]);
            e.target.blur(); // 엔터 검색 후 input 박스에서 커서 제거
        }
    }

    // 엔터/서치 아이콘 클릭 시 하단에 키워드 결과 레시피 카드를 보여주는 함수
    const handleSearch = (el) => {
        // 마우스로 드롭다운 요소 클릭 시 바로 검색 키워드 추가
        if (typeof el === "string") {
            searchParams.set("keyword", el);
            setSearchParams(searchParams);
            // setSearchParams({ "keyword": el });

        }
        // 엔터 또는 돋보기 아이콘으로 검색했을 때
        else if (searchValue.length > 0){
            searchParams.set("keyword", searchValue);
            setSearchParams(searchParams);
            // setSearchParams({ "keyword": searchValue });
        }
        // 빈 검색어에 엔터 누를 경우 이전 검색어 지우기
        else {
            searchParams.set("keyword", "");
            setSearchParams(searchParams);
        }
        setIsDropDownOpen(false);
        setCursor(-1);
    };

    // 마우스로 외부 클릭시 드롭다운 닫기
    const handleClickOutside = (e) => {
        if (isDropDownOpen && !searchBarRef.current.contains(e.target)) {
            setIsDropDownOpen(false);
            setCursor(-1);
            setSearchValue("");
            setSuggestedValue([]);
        }
    }

    // 외부 클릭 후 다시 Input창 클릭시 연관 검색어가 있으면 드롭다운을 다시 보여주는 함수
    const handleClickInside = () => {
        // "입력값에 대한 연관 검색어가 있으면" 으로 변경 예정
        if (suggestedValue.length > 0) {
            setIsDropDownOpen(true);
        }
    }
    
    return (
        <Container ref={searchBarRef}>
            <SearchBar>
                <SearchInput
                    value={searchValue || ""} //uncontrolled input 관련 에러 해결 방법
                    onChange={handleInput}
                    onKeyUp={handleKeyUp}
                    onKeyPress={handleKeyPress}
                    onClick={handleClickInside}
                />
                <StyledFontAwesomeIcon
                    icon={faMagnifyingGlass}
                    onClick={handleSearch}
                />
            </SearchBar>
            {isDropDownOpen &&
                <DropDown>
                    {suggestedValue.map((el, idx) => {
                        return (
                            <Suggestion
                                key={idx}
                                onClick={() => { setSearchValue(el); setCursor(idx); handleSearch(el); }}
                                selected={cursor === idx}
                                ref={idx === cursor ? suggestionRef : null}
                            >
                                {el}
                            </Suggestion>
                        )
                    })}
                </DropDown>
            }
        </Container>
    )
};

export default NameSearchBar