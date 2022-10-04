import { useState, useEffect, useRef } from "react";
import { useSearchParams } from "react-router-dom";
import { faMagnifyingGlass, faXmark } from "@fortawesome/free-solid-svg-icons";
import { Container, DropDown, SearchBar, SearchInput, StyledFaXmark, StyledFontAwesomeIcon, Suggestion, Tag, TagWrapper } from "./TagSearchBarStyle";
import axios from "axios";

const TagSearchBar = ({ setIsRefreshNeeded }) => {

    const [searchParams, setSearchParams] = useSearchParams();
    const [searchValue, setSearchValue] = useState(""); // 확정된 검색 태그 하나
    const [searchTags, setSearchTags] = useState([]); // 확정된 검색 태그 전체 배열
    console.log("서치 태그 길이", searchTags.length)
    const [suggestedValue, setSuggestedValue] = useState([]); // 연관 검색어
    const [isDropDownOpen, setIsDropDownOpen] = useState(false);
    const [cursor, setCursor] = useState(-1);

    // console.log("태그 추천 검색어", suggestedValue);

    const searchBarRef = useRef(); // 서치바+드롭다운 창 밖 클릭을 인식하기 위한 ref
    const suggestionRef = useRef(null); // 스크롤이 드롭다운 내 선택된 요소를 따라가게 하기 위한 ref

    // const suggestedValue = []
    // const suggestedValue = ["순두부", "감자"]
    // const dummyData = ["순두부", "감자", "미역", "간장", "계란", "밥", "돼지고기", "칼국수면", "고구마", "김치", "닭고기", "소고기", "두부"]

    // 관련 검색어 불러오기
    const getDropDownValue = async (keyword) => {
        if (keyword.length > 0) {
            try {
                const { data } = await axios.get(`/api/ingredients/names?word=${keyword}`);
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

    // 마우스로 외부 클릭시 드롭다운 닫기
    const handleClickOutside = (e) => {
        if (isDropDownOpen && !searchBarRef.current.contains(e.target)) {
            setIsDropDownOpen(false);
            setCursor(-1);
            setSearchValue("");
            setSuggestedValue([]);
        }
    }

    // 태그 입력시 연관 태그 보여주는 함수
    const handleInput = (e) => {
        setCursor(-1); // 새 검색어 입력시 커서 위치 초기화
        // 입력 값이 없을 땐 드랍다운 닫기
        if (e.target.value.length <= 0) {
            setIsDropDownOpen(false);
            setSuggestedValue([]);
        }
        setSearchValue(e.target.value);
        getDropDownValue(e.target.value);
    };

    // 방향키에 따라 드롭다운 내 이동
    const handleKeyUp = (e) => {
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
        }
        else {
            if (e.key === 'ArrowDown') {
                setIsDropDownOpen(true);
            }
        }
    }

    // 엔터키에 대한 핸들러 (엔터 두번 눌러지는걸 방지하기 위해 onKeyUp과 분리)
    const handleKeyPress = (e) => {
        // if (!e.isComposing) { //한글 입력 후 드롭다운 내릴 때 처음만 커서 두번 움직임
        if (e.key === 'Enter') {
            handleSearch();
            setSuggestedValue([]);
            setIsRefreshNeeded(true);
        }
    }

    // 엔터/서치 아이콘 클릭 시 하단에 태그 결과 레시피 카드를 보여주는 함수
    const handleSearch = (el) => {
        setIsRefreshNeeded(true);
        // 마우스로 드롭다운 요소 클릭 시 바로 검색 태그에 추가
        if (typeof el === "string") {
            // 중복 태그 방지
            if (!searchTags.includes(el)) {
                // 태그 갯수 유효성 (10개 이하)
                if (searchTags.length >= 10) {
                    alert("태그는 10개 이하로만 추가할 수 있어요ㅠㅠ")
                }
                else {
                    searchParams.set("tags", [...searchTags, el])
                    setSearchParams(searchParams);
                    setSearchTags([...searchTags, el]);
                }
            }
            else {
                alert(`이미 등록한 재료예요`);
            }
            setIsDropDownOpen(false);
            setCursor(-1);
            setSearchValue("");
        }
        // 엔터 또는 돋보기 아이콘으로 검색했을 때 (빈 값은 인식하지 않기)
        else if (searchValue.length > 0){
            // 중복 태그 방지
            if (!searchTags.includes(searchValue)) {
                if (searchTags.length >= 10) {
                    alert("태그는 10개 이하로만 추가할 수 있어요ㅠㅠ")
                }
                else {
                    // 마우스로 드롭다운 요소 클릭 시 바로 검색 태그에 추가
                    searchParams.set("tags", [...searchTags, searchValue])
                    setSearchParams(searchParams);
                    setSearchTags([...searchTags, searchValue]);
                }
            }
            else {
                alert(`이미 등록한 재료예요`)
            }
            setIsDropDownOpen(false);
            setCursor(-1);
            setSearchValue("");
        }
        // 빈 검색어에 엔터
        else {
            alert(`검색어를 입력해주세요`)
        }
    };
    // console.log(searchTags, `검색했어요`)

    const handleDeleteTag = (idx) => {
        let tmp = [...searchTags];
        tmp.splice(idx, 1);
        searchParams.set("tags", tmp);
        setSearchParams(searchParams);
        setSearchTags(tmp);
        setIsRefreshNeeded(true);
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