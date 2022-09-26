import { useState, useEffect, useRef } from "react";
import { useNavigate, useSearchParams, useLocation } from "react-router-dom";
import RecipeCard from "../../components/layout/RecipeCard/RecipeCard";
import { Container, Heading, Loader, Option, RecipeWrapper, StyledFontAwesomeIcon } from "./FridgeDiggingStyle";
import NameSearchBar from "../../components/layout/NameSearchBar/NameSearchBar";
import TagSearchBar from "../../components/layout/TagSearchBar/TagSearchBar";
import ModalSearchBar from "../../components/layout/ModalSearchBar.js/ModalSearchBar";
import SortingTab from "../../components/common/SortingTab/SortingTab";
import { faEgg, faCarrot, faFish, faPizzaSlice, faBowlRice } from "@fortawesome/free-solid-svg-icons";
import axios from "axios";

const FridgeDigging = () => {

    const location = useLocation();
    console.log(location.state)

    useEffect(() => {
        // 상세보기에서 뒤로가기 한게 아닌 이상 검색 상태는 초기화
        if (!location.state) {
            console.log(`검색 상태 지우기`);
        }
    }, [])

    const navigate = useNavigate();
    
    const [sortMode, setSortMode] = useState("like");
    const [searchParams, setSearchParams] = useSearchParams();
    // 어떤 컴포넌트에서든 searchParams의 키워드 값을 가져와 관련 http 요청을 보낼 수 있다.
    const searchTerm = searchParams.get('keyword');


    const dummyArr = Array(16).fill(0);

    // 무한 스크롤 관련 (Intersection Observer 사용)
    const [data, setData] = useState([]);
    const [pageNum, setPageNum] = useState(1);
    const [isLoading, setIsLoading] = useState(false);

    const fetchData = async(pageNum) => {
        // try {
        //     const response = await axios.get(`/api/recipes?page=${pageNum}&size=16`);
        //     console.log(response);
        //     const jsonData = await response.json();
        //     console.log(jsonData);
        //     setData(data => [...data, ...jsonData]);
        //     setIsLoading(true);
        // }
        // catch (error) {
        //     console.log(error);
        // }
        console.log(`새로 fetch 합니다!`)
        setIsLoading(true);
    };
    // 페이지 넘버가 바뀔 때마다 새로 데이터 fetch
    useEffect(() => {
        fetchData(pageNum);
    }, [pageNum])
    
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
        memberName: "들깨러버들깨러버버버",
        likes: 221,
        views: 1200,
    }

    return (
        <Container>
            <Heading>
                요리로 레시피 검색하기
            </Heading>
            <NameSearchBar />
            <Heading>
                재료로 레시피 검색하기
            </Heading>
                <TagSearchBar />
            <Option>
                <SortingTab sortMode={sortMode} setSortMode={setSortMode}/>
            </Option>
            <RecipeWrapper>
                {dummyArr.map((e, i) => {
                    return (
                        <RecipeCard
                            key={i}
                            imagePath={dummyData.imagePath}
                            title={dummyData.title}
                            memberName={dummyData.memberName}
                            memberImage={dummyData.memberImage}
                            likes={dummyData.likes}
                            views={dummyData.views}
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