import { useState, useEffect, useRef } from "react";
import { useSearchParams } from "react-router-dom";
import { faMagnifyingGlass, faXmark } from "@fortawesome/free-solid-svg-icons";
import { Container, DropDown, SearchBar, SearchInput, StyledFaXmark, StyledFontAwesomeIcon, Suggestion, Tag, TagWrapper } from "./TagSearchBarStyle";
import axios from "axios";

const TagSearchBar = () => {

    const [searchParams, setSearchParams] = useSearchParams();
    const [searchValue, setSearchValue] = useState(""); // 확정된 검색 태그 하나
    const [searchTags, setSearchTags] = useState([]); // 확정된 검색 태그 전체 배열
    const [suggestedValue, setSuggestedValue] = useState([]); // 연관 검색어
    const [isDropDownOpen, setIsDropDownOpen] = useState(false);
    const [cursor, setCursor] = useState(-1);

    // 어떤 컴포넌트에서든 searchParams의 키워드 값을 가져와 관련 http 요청을 보낼 수 있다.
    const searchTerm = searchParams.get('tag');

    const searchBarRef = useRef(); // 서치바+드롭다운 창 밖 클릭을 인식하기 위한 ref
    const suggestionRef = useRef(null); // 스크롤이 드롭다운 내 선택된 요소를 따라가게 하기 위한 ref

    // const suggestedValue = []
    // const suggestedValue = ["순두부", "감자"]
    // const dummyData = ["순두부", "감자", "미역", "간장", "계란", "밥", "돼지고기", "칼국수면", "고구마", "김치", "닭고기", "소고기", "두부"]

    // 레시피 상세 데이터 불러오기
    const getDropDownValue = async (keyword) => {
        if (keyword.length > 0) {
            try {
                const { data } = await axios.get(`/api/ingredients/names?word=${keyword}`);
                setSuggestedValue([...data.data]);
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

    // 마우스로 클릭시 드롭다운 닫기
    const handleClickOutside = (e) => {
        if (isDropDownOpen && !searchBarRef.current.contains(e.target)) {
            setIsDropDownOpen(false);
            setCursor(-1);
            setSearchValue("");
        }
    }

    // 태그 입력시 연관 태그 보여주는 함수
    const handleInput = (e) => {
        setCursor(-1); // 새 검색어 입력시 커서 위치 초기화
        // "입력값에 대한 연관 검색어가 있으면" 으로 변경 예정
        if (e.target.value.length <= 0) {
            setIsDropDownOpen(false);
            setSuggestedValue([]);
        }
        setSearchValue(e.target.value);
        getDropDownValue(e.target.value);
    };

    // 엔터키 또는 방향키에 따라 드롭다운 내 이동
    const handleKey = (e) => {
        // if (!e.isComposing) { //한글 입력 후 드롭다운 내릴 때 처음만 커서 두번 움직임
        if (isDropDownOpen) {
            if (e.key === 'ArrowDown') {
                // 아래 suggestedValue를 추후 연관검색어로 변경 예정
                isDropDownOpen &&
                    setCursor((prev) => (prev < suggestedValue.length - 1 ? prev + 1 : prev));
            }
            if (e.key === 'ArrowUp') {
                isDropDownOpen &&
                    setCursor((prev) => (prev > 0 ? prev - 1 : 0));
            }
            if (e.key === 'Escape') {
                setCursor(-1);
                setIsDropDownOpen(false);
                setSuggestedValue([]);
            }
            if (e.key === 'Enter' && cursor > -1) {
                handleSearch();
                setSuggestedValue([]);
            }
            // if (e.key === 'Backspace') {
            //     e.preventDefault();
            //     console.log("백스페이스 눌렀어?")
            // }
        }
        else {
            if (e.key === 'Enter') {
                handleSearch();
                setSuggestedValue([]);
            }
            if (e.key === 'ArrowDown') {
                setIsDropDownOpen(true);
            }
        }

    }

    // 엔터/서치 아이콘 클릭 시 하단에 태그 결과 레시피 카드를 보여주는 함수
    const handleSearch = (el) => {
        // 마우스로 드롭다운 요소 클릭 시 바로 검색 태그에 추가
        if (typeof el === "string") {
            // 중복 태그 방지
            if (!searchTags.includes(el)) {
                setSearchTags([...searchTags, el]);
            }
            else {
                alert(`이미 등록한 재료입니다`);
            }
            setIsDropDownOpen(false);
            setCursor(-1);
            setSearchValue("");
        }
        // 엔터 또는 돋보기 아이콘으로 검색했을 때
        else {
            // 중복 태그 방지
            if (!searchTags.includes(searchValue)) {
                // 마우스로 드롭다운 요소 클릭 시 바로 검색 태그에 추가
                setSearchTags([...searchTags, searchValue]);
            }
            else {
                alert(`이미 등록한 재료입니다`)
            }
            setIsDropDownOpen(false);
            setCursor(-1);
            setSearchValue("");
        }
    };
    // console.log(searchTags, `검색했어요`)

    const handleDeleteTag = (idx) => {
        let tmp = [...searchTags];
        tmp.splice(idx, 1);
        setSearchTags(tmp);
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
                    onKeyUp={handleKey}
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
            <TagWrapper>
                {searchTags.map((tag, index) => {
                    return (
                        <Tag key={index}>
                            {tag}
                            <StyledFaXmark icon={faXmark} onClick={() => handleDeleteTag(index)} />
                        </Tag>
                    )
                })}
            </TagWrapper>
        </Container>
    )
}

export default TagSearchBar