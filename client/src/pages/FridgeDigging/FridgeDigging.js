import { useState } from "react";
import RecipeCard from "../../components/layout/RecipeCard/RecipeCard";
import { Container, Heading, RecipeWrapper } from "./FridgeDiggingStyle";
import NameSearchBar from "../../components/layout/NameSearchBar/NameSearchBar";
import TagSearchBar from "../../components/layout/TagSearchBar/TagSearchBar";

const FridgeDigging = () => {

    const [searchResult, setSearchResult] = useState(null);
    console.log(searchResult);

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
            <NameSearchBar setSearchResult={setSearchResult}/>
            <Heading>
                재료로 레시피 검색하기
            </Heading>
            <TagSearchBar SearchBar setSearchResult={setSearchResult}/>
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