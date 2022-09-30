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

    console.log("서치리저트", searchResult.length)
    console.log("토털넘버", totalNum)
    console.log("로딩중이니?", isLoading)

    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        // 상세보기에서 뒤로가기 한게 아닌 이상 검색 상태는 초기화
        if (!location.state) {
            // console.log(`검색 상태 지우기`);
            setSearchParams("");
        }
    }, [])

    // 현재 보여지는 검색 결과 수가 총 결과 수보다 작을 때만 로딩 화면 뜨기
    useEffect(() => {
        if (searchResult.length < totalNum) {
            setIsLoading(true);
        }
    }, [searchResult, totalNum])

    const fetchData = async (pageNum) => {
        // 검색 요청 바디
        const payload = {
            title: nameSearchTerm ? nameSearchTerm : "", // 서치값 없을 때 null 요청 방지
            ingredients: tagSearchArr,
            page: pageNum,
            sort: sortMode
        }
        console.log("리퀘 바디", payload)

        if (tagSearchArr.length > 0 || payload.title !== "") {
            setIsThereSearch(true);
            try {
                // 결과 안에 
                const { data } = await axios.post(`/api/recipes/search`, payload);
                // console.log(response);
                // console.log(response.data.data);
                console.log("서버 데이터", data.data)
                console.log("서버 데이터 몇개", data.pageInfo.totalElements);
                data.pageInfo.totalElements > 0 ? setIsThereResult(true) : setIsThereResult(false);
                setTotalNum(data.pageInfo.totalElements);
                setSearchResult([...searchResult, ...data.data])
                // const jsonData = await response.json();
                // console.log(jsonData);
                // setData(data => [...data, ...jsonData]);
            }
            catch (error) {
                console.log(error);
            }
            console.log(`새로 fetch 합니다!`)
        }
        else {
            setIsThereSearch(false);
        }
    };
    // 페이지 넘버, 검색 값, 정렬모드 바뀔 때마다 새로 데이터 fetch
    useEffect(() => {
        fetchData(pageNum);
    }, [pageNum, nameSearchTerm, tagSearchTerm, sortMode])
    
    const loadMore = () => {
        setPageNum(prevPageNum => prevPageNum + 1);
    }

    const pageEnd = useRef();

    useEffect(() => {
        if (isLoading) {
            const observer = new IntersectionObserver(entries => {
                if (entries[0].isIntersecting) {
                    loadMore();
                }
            },{threshold: 0});

            observer.observe(pageEnd.current);
        }
    }, [isLoading]);

    const dummyData = {
        id: 1,
        imagePath: "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
        title: "백종원의 들깨칼국수칼국수",
        memberImage: "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
        memberName: "들깨러버",
        likes: 221,
        views: 1200,
    }

    return (
        <Container>
            <SearchWrapper>
                <Heading>
                    제목으로 레시피 검색하기
                </Heading>
                <NameSearchBar />
                <Heading>
                    재료로 레시피 검색하기
                </Heading>
                <TagSearchBar />
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
                <SortingTab sortMode={sortMode} setSortMode={setSortMode} />
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