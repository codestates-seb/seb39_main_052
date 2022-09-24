import { useState, useEffect, useRef } from "react";
import { useSearchParams } from "react-router-dom";
import { faMagnifyingGlass, faXmark } from "@fortawesome/free-solid-svg-icons";
import { Container, DropDown, SearchBar, SearchInput, StyledFaXmark, StyledFontAwesomeIcon, Suggestion, Tag, TagWrapper } from "./TagSearchBarStyle";

const TagSearchBar = () => {

    const [searchParams, setSearchParams] = useSearchParams();
    const [searchValue, setSearchValue] = useState("");
    const [searchTags, setSearchTags] = useState([]);
    const [isDropDownOpen, setIsDropDownOpen] = useState(false);
    const [cursor, setCursor] = useState(-1);

    // 어떤 컴포넌트에서든 searchParams의 키워드 값을 가져와 관련 http 요청을 보낼 수 있다.
    const searchTerm = searchParams.get('tag');

    const searchBarRef = useRef(); // 서치바+드롭다운 창 밖 클릭을 인식하기 위한 ref
    const suggestionRef = useRef(null); // 스크롤이 드롭다운 내 선택된 요소를 따라가게 하기 위한 ref

    // const dummyData = []
    // const dummyData = ["순두부", "감자"]
    const dummyData = ["순두부", "감자", "미역", "간장", "계란", "밥", "돼지고기", "칼국수면", "고구마", "김치", "닭고기", "소고기", "두부"]

    // console.log(searchValue);
    // 드랍다운에서 위치 바뀌면 검색 값 변경 + 스크롤이 드롭다운 내 선택된 요소를 따라가게 하기
    useEffect(() => {
        if (isDropDownOpen) {
            setSearchValue(dummyData[cursor]);
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
        setSearchValue(e.target.value);
        // "입력값에 대한 연관 검색어가 있으면" 으로 변경 예정
        if (dummyData.length > 0) {
            setIsDropDownOpen(true);
        }
    };

    // 엔터키 또는 방향키에 따라 드롭다운 내 이동
    const handleKey = (e) => {

        if (isDropDownOpen) {
            if (e.key === 'ArrowDown') {
                // 아래 dummyData를 추후 연관검색어로 변경 예정
                isDropDownOpen &&
                    setCursor((prev) => (prev < dummyData.length - 1 ? prev + 1 : prev));
            }
            if (e.key === 'ArrowUp') {
                isDropDownOpen &&
                    setCursor((prev) => (prev > 0 ? prev - 1 : 0));
            }
            if (e.key === 'Escape') {
                setCursor(-1);
                setIsDropDownOpen(false);
            }
            if (e.key === 'Enter' && cursor > -1) {
                handleSearch();
            }
        }
        else {
            if (e.key === 'Enter') {
                handleSearch();
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
        if (dummyData.length > 0) {
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
                    {dummyData.map((el, idx) => {
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