import { Link } from "react-router-dom";
import { Container, ContentWrapper, Doornob, Image, LikesAndViews, StyledFontAwesomeIcon, Title } from "./RecipeCardStyle";
import UserName from "../../common/UserName/UserName";
import LikeHeart from "../../common/LikeHeart/LikeHeart";
import { faGripLines } from "@fortawesome/free-solid-svg-icons";

const RecipeCard = ({
    id,
    imagePath,
    title,
    memberName,
    memberImage,
    heartCounts,
    views
}) => {
    
    const recipeLink = `/recipes/${id}`;
    // if (title.length > 10) {
    //     title.slice(0, 11);
    //     title.concat("...");
    //     console.log(title);
    // }

    return (
        <Container>
            <Link to={recipeLink}><Image src={imagePath} alt="food"/></Link>
            <ContentWrapper>
                <Title><Link to={recipeLink}>{title}</Link></Title>
                <Doornob>
                    <UserName image={memberImage} name={memberName} />
                    <StyledFontAwesomeIcon icon={faGripLines}/>
                </Doornob>
                <LikesAndViews>
                    <LikeHeart heartCounts={heartCounts} />
                    <div>조회수 {views}</div>
                </LikesAndViews>
            </ContentWrapper>
        </Container>
    )
};

export default RecipeCard;