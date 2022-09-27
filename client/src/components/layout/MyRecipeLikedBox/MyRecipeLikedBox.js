import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrashCan } from "@fortawesome/free-solid-svg-icons";
import {
  RecipeFrameOuterContainer,
  SpanWrapper,
} from "../MyRecipeBox/MyRecipeBoxStyle";
import RecipeFrame from "../RecipeFrame/RecipeFrame";

const MyRecipeLikedBox = () => {
  const dummyData = [
    {
      id: 1,
      imagePath:
        "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
      title: "백종원의 들깨칼국수칼국수",
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
      memberName: "웰시코기궁둥",
      likes: 10,
      views: 120,
      numComments: 10,
      date: "2022.9.26.",
    },
    {
      id: 3,
      imagePath:
        "https://www.tocanvas.net/wp-content/uploads/2022/02/food-aesthetic.jpeg",
      title: "퐁신퐁신 팬케이크 만들기",
      memberName: "마치라잌웰시코기궁둥",
      likes: 50,
      views: 2220,
      numComments: 20,
      date: "2022.9.26.",
    },
    {
      id: 4,
      imagePath:
        "https://i.pinimg.com/564x/d5/e0/ac/d5e0ac981b650a5731cd268bdbb42397.jpg",
      title: "집에서 로투스 크림라떼 만들기",
      memberName: "홈카페대마왕",
      likes: 40,
      views: 50,
      numComments: 50,
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
          icon2={<FontAwesomeIcon icon={faTrashCan}></FontAwesomeIcon>}
        >
          <SpanWrapper>
            by <span>{data.memberName}</span>
          </SpanWrapper>
        </RecipeFrame>
      ))}
    </RecipeFrameOuterContainer>
  );
};

export default MyRecipeLikedBox;
