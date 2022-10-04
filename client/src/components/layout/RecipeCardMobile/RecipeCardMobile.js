import { Link } from "react-router-dom";
import LikeHeart from "../../common/LikeHeart/LikeHeart";
import UserName from "../../common/UserName/UserName";
import {
  Container,
  ContentWrapper,
  Doornob,
  Image,
  LikesAndViews,
  Title,
} from "./RecipeCardMobileStyle";

const RecipeCardMobile = ({
  id,
  imagePath,
  title,
  memberName,
  memberImage,
  heartCounts,
  views,
  detectOnClick, //서치 모달에서 레시피 카드 제목, 사진부분 눌렀을때 모달 닫혀야되어서 props 만듬
}) => {
  const recipeLink = `/recipes/${id}`;
  return (
    <Container>
      <Link to={recipeLink}>
        <Image src={imagePath} alt="food" onClick={detectOnClick} />
      </Link>
      <ContentWrapper>
        <Title onClick={detectOnClick}>
          <Link to={recipeLink}>{title}</Link>
        </Title>

        <UserName image={memberImage} name={memberName} />

        <LikesAndViews>
          <LikeHeart heartCounts={heartCounts} />
          <div>조회수 {views}</div>
        </LikesAndViews>
      </ContentWrapper>
    </Container>
  );
};

export default RecipeCardMobile;
