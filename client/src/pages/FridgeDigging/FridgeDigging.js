import { useState } from "react";
import { useSearchParams } from "react-router-dom";
import RecipeCard from "../../components/layout/RecipeCard/RecipeCard";
import { Container, Heading, RecipeWrapper } from "./FridgeDiggingStyle";
import NameSearchBar from "../../components/layout/NameSearchBar/NameSearchBar";
import TagSearchBar from "../../components/layout/TagSearchBar/TagSearchBar";
import ModalSearchBar from "../../components/layout/ModalSearchBar.js/ModalSearchBar";
import SortingTab from "../../components/common/SortingTab/SortingTab";

const FridgeDigging = () => {

    const [sortMode, setSortMode] = useState("like");
    const [searchParams, setSearchParams] = useSearchParams();
    // 어떤 컴포넌트에서든 searchParams의 키워드 값을 가져와 관련 http 요청을 보낼 수 있다.
    const searchTerm = searchParams.get('keyword');

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
            <SortingTab sortMode={sortMode} setSortMode={setSortMode}/>
            <ModalSearchBar />
            <RecipeWrapper>
                <RecipeCard
                    imagePath={dummyData.imagePath}
                    title={dummyData.title}
                    memberName={dummyData.memberName}
                    memberImage={dummyData.memberImage}
                    likes={dummyData.likes}
                    views={dummyData.views}
                />
                <RecipeCard
                    imagePath={dummyData.imagePath}
                    title={dummyData.title}
                    memberName={dummyData.memberName}
                    memberImage={dummyData.memberImage}
                    likes={dummyData.likes}
                    views={dummyData.views}
                />
                <RecipeCard
                    imagePath={dummyData.imagePath}
                    title={dummyData.title}
                    memberName={dummyData.memberName}
                    memberImage={dummyData.memberImage}
                    likes={dummyData.likes}
                    views={dummyData.views}
                />
                <RecipeCard
                    imagePath={dummyData.imagePath}
                    title={dummyData.title}
                    memberName={dummyData.memberName}
                    memberImage={dummyData.memberImage}
                    likes={dummyData.likes}
                    views={dummyData.views}
                />
            </RecipeWrapper>
        </Container>
    )
};

export default FridgeDigging;