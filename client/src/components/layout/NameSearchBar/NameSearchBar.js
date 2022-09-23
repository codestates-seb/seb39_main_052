import { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";
import { DropDown, SearchBar, SearchInput, StyledFontAwesomeIcon, Suggestion } from "./NameSearchBarStyle";

const NameSearchBar = () => {

    const [searchParams, setSearchParams] = useSearchParams();
    const [searchValue, setSearchValue] = useState("");

    // 어떤 컴포넌트에서든 searchParams의 키워드 값을 가져와 관련 http 요청을 보낼 수 있다.
    const searchTerm = searchParams.get('keyword');

    // 드랍다운 컴포넌트가 요소 수에 따라 어떻게 바뀌는지 보기 위한 변수들
    // const dummyData = []
    // const dummyData = ["순두부찌개", "감자탕"]
    const dummyData = ["순두부찌개", "감자탕", "미역국", "까르보나라", "간장계란밥", "반찬", "백종원", "칼국수"]

    // 검색어 입력시 연관 검색어 보여주는 함수
    const handleInput = (e) => {
        setSearchValue(e.target.value);
    }; 
    // 엔터/서치 아이콘 클릭 시 하단에 키워드 결과 레시피 카드를 보여주는 함수
    const handleSearch = () => {
        console.log(searchValue, `검색했어요`)
        setSearchParams({"keyword" : searchValue});
    };
    
    return (
        <>
            <SearchBar>
                <SearchInput
                    value={searchValue}
                    onChange={handleInput}
                    onKeyUp={(e) => { if (e.key === "Enter") handleSearch() }}
                />
                <StyledFontAwesomeIcon 
                    icon={faMagnifyingGlass}
                    onClick={handleSearch}
                />
            </SearchBar>
            {dummyData.length > 0 &&
                <DropDown>
                    {dummyData.map((el, idx) => {
                        return (
                            <Suggestion 
                                key={idx}
                                onClick={() => setSearchValue(el)}
                            >
                                {el}
                            </Suggestion>
                        )
                    })}
                </DropDown>
            }
        </>
    )
};

export default NameSearchBar