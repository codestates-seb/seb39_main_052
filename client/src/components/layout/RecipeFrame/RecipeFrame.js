import {
  RecipeFrameContainer,
  DisplayImg,
  Description,
  Title,
  Spec,
  Date,
  Icon,
} from "./RecipeFrameStyle";

const RecipeFrame = ({ imagePath, title, children, date, icon1, icon2 }) => {
  //데이터는 RecipeFrame 상위 컴포넌트에서 불러와야함 그래야 레시피 프레임을 map으로 여러개 나타낼수있음

  //컴포넌트로 refactoring
  return (
    <RecipeFrameContainer>
      <DisplayImg src={imagePath}></DisplayImg>
      <Description>
        <Title>{title}</Title>
        {/* <h2>{dummyData.title}</h2> */}
        {/* Spec은 내레시피 컴포넌트에는 조회수, 좋아요, 댓글수 div 한줄 / 내좋아요 컴포넌트에는 by 작성자 한줄 */}
        <Spec className="spec">{children} </Spec>
        <Date>{date}</Date>
      </Description>
      <Icon>
        <span>{icon1}</span>
        <span>{icon2}</span>
      </Icon>
    </RecipeFrameContainer>
  );
};

export default RecipeFrame;
