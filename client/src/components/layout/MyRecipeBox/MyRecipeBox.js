import RecipeFrame from "../RecipeFrame/RecipeFrame";
import { RecipeFrameOuterContainer, SpanWrapper } from "./MyRecipeBoxStyle";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faPencil,
  faTrashCan,
  faEye,
  faHeart,
  faCommentDots,
} from "@fortawesome/free-solid-svg-icons";

const MyRecipeBox = () => {
  const dummyData = [
    {
      id: 1,
      imagePath:
        "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
      title: "백종원의 들깨칼국수칼국수",
      memberImage:
        "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
      memberName: "들깨러버들깨러버버버",
      likes: 221,
      views: 1200,
      numComments: 3,
      date: "2022.9.27.",
    },
    {
      id: 2,
      imagePath:
        "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
      title: "웰시코기코기코기콜기",
      memberImage:
        "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
      memberName: "웰시코기궁둥",
      likes: 10,
      views: 120,
      numComments: 10,
      date: "2022.9.26.",
    },
  ];

  return (
    <RecipeFrameOuterContainer>
      {dummyData.map((data) => (
        <RecipeFrame
          key={data.id}
          imagePath={data.imagePath}
          title={data.title}
          date={data.date}
          icon1={<FontAwesomeIcon icon={faPencil}></FontAwesomeIcon>}
          icon2={<FontAwesomeIcon icon={faTrashCan}></FontAwesomeIcon>}
        >
          {/* children props 로 자식 컴포넌트로 전달되는 요소들 */}
          <SpanWrapper>
            <span>
              <FontAwesomeIcon icon={faEye}></FontAwesomeIcon>
            </span>
            <span>{data.views}</span>
          </SpanWrapper>
          <SpanWrapper>
            <span>
              <FontAwesomeIcon icon={faHeart}></FontAwesomeIcon>
            </span>
            <span>{data.likes}</span>
          </SpanWrapper>
          <SpanWrapper>
            <span>
              <FontAwesomeIcon icon={faCommentDots}></FontAwesomeIcon>
            </span>
            <span>{data.numComments}</span>
          </SpanWrapper>
        </RecipeFrame>
      ))}
    </RecipeFrameOuterContainer>
  );
};

export default MyRecipeBox;
