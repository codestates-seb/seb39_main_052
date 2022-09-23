import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faXmark } from "@fortawesome/free-solid-svg-icons";
import ModalSearchBar from "../ModalSearchBar.js/ModalSearchBar";
import RecipeCard from "../RecipeCard/RecipeCard";
import {
  Overlay,
  ModalContainer,
  CloseButton,
  Contents,
} from "./RecipeSearchModalStyle";
import GeneralButton from "../../common/Button/GeneralButton";
// 나중에 모달창에 검색어 불러올때 아래 코드로 불러오시면 됩니당
// const searchTerm = searchParams.get('keyword');
// 예) axios(`api/search?keyword=${searchTerm}`)

const RecipeSearchModal = ({ handleClose }) => {
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
    },
    {
      id: 2,
      imagePath:
        "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
      title: "2백종원의 들깨칼국수칼국수",
      memberImage:
        "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
      memberName: "2들깨러버들깨러버버버",
      likes: 221,
      views: 1200,
    },
    {
      id: 3,
      imagePath:
        "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
      title: "3백종원의 들깨칼국수칼국수",
      memberImage:
        "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
      memberName: "3들깨러버들깨러버버버",
      likes: 221,
      views: 1200,
    },
    {
      id: 4,
      imagePath:
        "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
      title: "4백종원의 들깨칼국수칼국수",
      memberImage:
        "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
      memberName: "4들깨러버들깨러버버버",
      likes: 221,
      views: 1200,
    },
  ];

  return (
    <Overlay onClick={(e) => e.stopPropagation()}>
      <ModalContainer onClick={(e) => e.stopPropagation()}>
        {" "}
        {/* stopPropagation 안쓰면 이벤트 버블링으로 엑스버튼말고도 화면 모두 누르면 닫힘 */}
        <CloseButton onClick={handleClose}>
          <FontAwesomeIcon icon={faXmark} size="2xl" />
        </CloseButton>
        <Contents>
          <div>
            <ModalSearchBar />
          </div>
          <div>
            {dummyData.map((data) => (
              <RecipeCard
                imagePath={data.imagePath}
                title={data.title}
                memberName={data.memberName}
                memberImage={data.memberImage}
                likes={data.likes}
                views={data.views}
              />
            ))}
          </div>
        </Contents>
        <i>
          <GeneralButton
            className="small"
            backgroundColor="var(--mint-400)"
            hoverBackgroundColor={"var(--mint-500)"}
            color="var(--gray-700)"
          >
            더보기
          </GeneralButton>
        </i>
      </ModalContainer>
    </Overlay>
  );
};

export default RecipeSearchModal;

//RecipeCard component props
// {
//     imagePath,
//     title,
//     memberName,
//     memberImage,
//     likes,
//     views
// }