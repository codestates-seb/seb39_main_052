import { Container, ContentWrapper, Doornob, Image, LikesAndViews, StyledFontAwesomeIcon, Title } from "./RecipeCardStyle";
import UserName from "../../common/UserName/UserName";
import LikeHeart from "../../common/LikeHeart/LikeHeart";
import { faGripLines } from "@fortawesome/free-solid-svg-icons";

const RecipeCard = ({
    imagePath,
    title,
    memberName,
    memberImage,
    likes,
    views
}) => {
    
    // if (title.length > 10) {
    //     title.slice(0, 11);
    //     title.concat("...");
    //     console.log(title);
    // }

    return (
        <Container>
            <Image src={imagePath} alt="food"/>
            <ContentWrapper>
                <Title>{title}</Title>
                <Doornob>
                    <UserName image={memberImage} name={memberName} />
                    <StyledFontAwesomeIcon icon={faGripLines}/>
                </Doornob>
                <LikesAndViews>
                    <LikeHeart likes={likes} />
                    <div>조회수 {views}</div>
                </LikesAndViews>
            </ContentWrapper>
        </Container>
    )
};

export default RecipeCard;