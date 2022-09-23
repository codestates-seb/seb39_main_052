import { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { faMagnifyingGlass, faXmark } from "@fortawesome/free-solid-svg-icons";
import { DropDown, SearchBar, SearchInput, StyledFaXmark, StyledFontAwesomeIcon, Suggestion, Tag, TagWrapper } from "./TagSearchBarStyle";

const TagSearchBar = () => {

    const [searchParams, setSearchParams] = useSearchParams();
    const [searchValue, setSearchValue] = useState("");
    const [searchTags, setSearchTags] = useState([]);

    const searchTerm = searchParams.get('tag');

    // const dummyData = []
    // const dummyData = ["순두부", "감자"]
    const dummyData = ["순두부", "감자", "미역", "간장", "계란", "밥", "돼지고기", "칼국수면"]
    
    useEffect(() => {
        // setSearchResult(["hi"])
    }, []);

    // 태그 입력시 연관 태그 보여주는 함수
    const handleInput = (e) => {
        setSearchValue(e.target.value);
    }; 
    // 엔터/서치 아이콘 클릭 시 하단에 태그 결과 레시피 카드를 보여주는 함수
    const handleSearch = () => {
        // 중복 태크 방지
        if (!searchTags.includes(searchValue)) {
            setSearchTags([...searchTags, searchValue]);
        }
        else {
            alert(`이미 등록한 재료입니다`)
        }
    };
    // console.log(searchTags, `검색했어요`)

    const handleDeleteTag = (idx) => {
        let tmp = [...searchTags];
        tmp.splice(idx, 1);
        setSearchTags(tmp);
    }
    
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
            <TagWrapper>
                {searchTags.map((tag, index) => {
                    return (
                        <Tag key={index}>
                            {tag}
                            <StyledFaXmark icon={faXmark} onClick={() => handleDeleteTag(index)}/>
                        </Tag>
                    )
                })}
            </TagWrapper>
        </>
    )
}

export default TagSearchBar