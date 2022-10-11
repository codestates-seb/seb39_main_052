import { useState, useEffect, useRef } from "react";
import { useSearchParams } from "react-router-dom";
import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";
import { DropDown, SearchBar, SearchInput, StyledFontAwesomeIcon, Suggestion } from "./ModalSearchBarStyle";

const ModalSearchBar = () => {

    // 나중에 모달창에 검색어 불러올때 아래 코드로 불러오시면 됩니당
    // const searchTerm = searchParams.get('keyword');
    // 예) axios(`api/search?keyword=${searchTerm}`)

    const [searchParams, setSearchParams] = useSearchParams();
    const [searchValue, setSearchValue] = useState("");

    // 돋보기 아이콘 클릭시 Input에 포커스
    const keywordInput = useRef();

    // 검색어 입력시 결과 검색어 보여주는 함수
    const handleInput = (e) => {
        setSearchValue(e.target.value);
        console.log( e.target.value, `검색했어요`)
        setSearchParams({"keyword" : e.target.value});
    }; 
    
    return (
        <>
            <SearchBar>
                <StyledFontAwesomeIcon 
                    icon={faMagnifyingGlass}
                    onClick={() => keywordInput.current.focus()}
                />
                <SearchInput
                    value={searchValue}
                    onChange={handleInput}
                    ref={keywordInput}
                />
            </SearchBar>
        </>
    )
}

export default ModalSearchBar