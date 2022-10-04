import { Link } from "react-router-dom";
import {
  Container,
  ContentWrapper,
  Doornob,
  Image,
  LikesAndViews,
  StyledFontAwesomeIcon,
  Title,
} from "./RecipeCardStyle";
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
  heartExist,
  views,
  detectOnClick, //서치 모달에서 레시피 카드 제목, 사진부분 눌렀을때 모달 닫혀야되어서 props 만듬
}) => {
  const recipeLink = `/recipes/${id}`;
  // if (title.length > 10) {
  //     title.slice(0, 11);
  //     title.concat("...");
  //     console.log(title);
  // }

  return (
    <Container>
      <Link to={recipeLink}>
        <Image src={imagePath} alt="food" onClick={detectOnClick} />
      </Link>
      <ContentWrapper>
        <Title onClick={detectOnClick}>
          <Link to={recipeLink}>{title}</Link>
        </Title>
        <Doornob>
          <UserName image={memberImage} name={memberName} />
          <StyledFontAwesomeIcon icon={faGripLines} />
        </Doornob>
        <LikesAndViews>
          <LikeHeart
            idx={id}
            heartCounts={heartCounts}
            heartExist={heartExist}
          />
          <div>조회수 {views}</div>
        </LikesAndViews>
      </ContentWrapper>
    </Container>
  );
};

export default RecipeCard;
