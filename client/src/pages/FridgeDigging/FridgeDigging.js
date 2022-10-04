import { useState, useEffect, useRef, useMemo } from "react";
import { useNavigate, useSearchParams, useLocation } from "react-router-dom";
import RecipeCard from "../../components/layout/RecipeCard/RecipeCard";
import { Alert, Container, Heading, Loader, Option, RecipeWrapper, ResultNum, SearchWrapper, StyledFontAwesomeIcon } from "./FridgeDiggingStyle";
import NameSearchBar from "../../components/layout/NameSearchBar/NameSearchBar";
import TagSearchBar from "../../components/layout/TagSearchBar/TagSearchBar";
import SortingTab from "../../components/common/SortingTab/SortingTab";
import { faEgg, faCarrot, faFish, faPizzaSlice, faBowlRice } from "@fortawesome/free-solid-svg-icons";
import axios from "axios";

const FridgeDigging = () => {

    // 무한 스크롤 관련 (Intersection Observer 사용)
    const [pageNum, setPageNum] = useState(1);
    const [isLoading, setIsLoading] = useState(false);

    const [searchResult, setSearchResult] = useState([]); // 서버 통신 후 레시피 목록을 할당할 상태
    const [sortMode, setSortMode] = useState("HEART"); // 정렬 default 값은 인기순
    const [totalNum, setTotalNum] = useState(""); // 총 레시피 순
    const [isThereSearch, setIsThereSearch] = useState(false); // 검색어 여부
    const [isThereResult, setIsThereResult] = useState(false); // 결과 값 여부
    const [isRefreshNeeded, setIsRefreshNeeded] = useState(false); // 검색 후 새로운 검색어가 추가되었는지, 정렬 모드가 바뀌었는지 확인

    const [searchParams, setSearchParams] = useSearchParams();
    // 어떤 컴포넌트에서든 searchParams의 키워드 값을 가져와 관련 http 요청을 보낼 수 있다.
    const nameSearchTerm = searchParams.get('keyword'); // 제목으로 레시피 검색한 값
    const tagSearchTerm = searchParams.get('tags'); // 태그로 레시피 검색한 값
    // string으로 된 태그 값을 배열로 변환
    let tagSearchArr = [];
    if (tagSearchTerm !== null) {
        // 태그 삭제 후 아무 값이 없을 때 빈문자열 방지
        if (tagSearchTerm.length > 0) {
            tagSearchArr = tagSearchTerm.split(",");
        }
    }

    const location = useLocation();
    const navigate = useNavigate();

    // 현재 보여지는 검색 결과 수가 총 결과 수보다 작을 때만 로딩 화면 뜨기
    useEffect(() => {
        if (searchResult.length < totalNum) {
            setIsLoading(true);
        }
        else {
            setIsLoading(false);
        }
    }, [searchResult, totalNum])

    // 페이지 넘버, 정렬모드, 검색어 바뀔 때마다 새로 데이터 fetch
    useEffect(() => {
        fetchData(pageNum);
    }, [pageNum, sortMode, searchParams])

    const fetchData = async (pageNum) => {

        // 검색 요청 바디
        const payload = {
            title: nameSearchTerm ? nameSearchTerm : "", // 서치값 없을 때 null 요청 방지
            ingredients: tagSearchArr,
            page: isRefreshNeeded ? 1 : pageNum, // 검색어 바뀔 때 마다 페이지 넘버 초기화
            sort: sortMode
        }
        // console.log("리퀘스트 바디", payload)

        // 검색어 바뀔 때 마다 정렬모드, 페이지 넘버 초기화
        if (isRefreshNeeded) {
            setPageNum(1);
        }

        if (tagSearchArr.length > 0 || payload.title !== "") {
            setIsThereSearch(true);
            try {
                const { data } = await axios.post(`/api/recipes/search`, payload);
                data.pageInfo.totalElements > 0 ? setIsThereResult(true) : setIsThereResult(false); // 결과 값 여부 확인
                setTotalNum(data.pageInfo.totalElements); // 총 결과 갯수 상태 저장
                // input 값/정렬모드 업데이트 된 경우 결과 처음부터 쌓기, 페이지 네이션으로 불러와진 결과는 이전 결과에 쌓기
                isRefreshNeeded ? setSearchResult([...data.data]) : setSearchResult([...searchResult, ...data.data])
            }
            catch (error) {
                console.log(error);
            }
        }
        else {
            setIsThereSearch(false);
        }
        // 추후 새로운 검색 input이 들어오는 것을 인식하기 위해 상태 초기화
        setIsRefreshNeeded(false);
    };

    // 더 불러오기 (페이지 수 1개 올리기);
    const loadMore = () => {
        setPageNum(pageNum + 1);
    }

    const pageEnd = useRef();
    // 무한 스크롤
    useEffect(() => {
        if (isLoading) {
            const observer = new IntersectionObserver(entries => {
                if (entries[0].isIntersecting) {
                    loadMore();
                }
            }, { threshold: 0 });

            observer.observe(pageEnd.current);
        }
    }, [isLoading]);

    return (
        <Container>
            <SearchWrapper>
                <Heading>
                    제목으로 레시피 검색하기
                </Heading>
                <NameSearchBar setIsRefreshNeeded={setIsRefreshNeeded} />
                <Heading>
                    재료로 레시피 검색하기
                </Heading>
                <TagSearchBar setIsRefreshNeeded={setIsRefreshNeeded} />
            </SearchWrapper>
            {/* 아무 검색어도 입력하지 않았을 때 */}
            <Alert className={isThereSearch && "invisible"}>
                검색어를 입력하여 원하는 레시피를 찾아보세요!
            </Alert>
            {/* 일치하는 검색 결과가 없을 때 */}
            <Alert className={!isThereSearch || isThereResult ? "invisible" : null}>일치하는 결과가 없습니다. 검색 범위를 넓혀보는건 어떨까요?</Alert>
            {/* 일치하는 검색 결과가 있을 때 */}
            <Option className={!isThereSearch || !isThereResult ? "invisible" : null}>
                <ResultNum>총 <strong>{totalNum}</strong>개</ResultNum>
                <SortingTab sortMode={sortMode} setSortMode={setSortMode} setIsRefreshNeeded={setIsRefreshNeeded}/>
            </Option>
            <RecipeWrapper className={!isThereSearch || !isThereResult ? "invisible" : null}>
                {searchResult.map((el, i) => {
                    return (
                        <RecipeCard
                            key={i}
                            id={el.id}
                            imagePath={el.imagePath}
                            title={el.title}
                            memberName={el.member.name}
                            memberImage={el.member.profileImagePath}
                            heartCounts={el.heartCounts}
                            heartExist={el.heartExist}
                            views={el.view}
                        />
                    )
                })}
                {isLoading && <Loader ref={pageEnd}
                >
                    <StyledFontAwesomeIcon icon={faEgg} spin />
                    <StyledFontAwesomeIcon icon={faCarrot} spin />
                    <StyledFontAwesomeIcon icon={faFish} spin />
                    <StyledFontAwesomeIcon icon={faPizzaSlice} spin />
                    <StyledFontAwesomeIcon icon={faBowlRice} spin />
                </Loader>}
                {/* <p ref={pageEnd}>Loading...</p> */}
            </RecipeWrapper>



        </Container>
    )
};

export default FridgeDigging;