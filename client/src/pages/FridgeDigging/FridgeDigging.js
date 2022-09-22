import RecipeCard from "../../components/layout/RecipeCard/RecipeCard";
import { Container } from "./FridgeDiggingStyle";

const FridgeDigging = () => {
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
        </Container>
    )
};

export default FridgeDigging;