import GeneralModal from "./GeneralModal";

import ModalSearchBar from "../ModalSearchBar.js/ModalSearchBar";
import RecipeCard from "../RecipeCard/RecipeCard";
import {
  RecipeCardWrapper,
  ModalSearchBarWrapper,
  GeneralButtonWrapper,
} from "./SearchModalStyle";
import GeneralButton from "../../common/Button/GeneralButton";

// 나중에 모달창에 검색어 불러올때 아래 코드로 불러오시면 됩니당
// const searchTerm = searchParams.get('keyword');
// 예) axios(`api/search?keyword=${searchTerm}`)

const SearchModal = ({ handleClose, width }) => {
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
    <GeneralModal handleClose={handleClose} width="864px" height={"490px"}>
      {/* <ModalContainerWrapper> */}{" "}
      {/* GeneralModal 부모 컴포넌트에서 정의된 ModalContainer css 바꾸려면? GeneralModal에서 props 만들고 GeneralModal스타일에서 변경. 자식인 SearchModal에서 props받아와서 쓰기 */}
      <ModalSearchBarWrapper>
        <ModalSearchBar />
      </ModalSearchBarWrapper>
      <RecipeCardWrapper>
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
      </RecipeCardWrapper>
      <GeneralButtonWrapper>
        <GeneralButton
          className="small"
          backgroundColor="var(--mint-400)"
          hoverBackgroundColor={"var(--mint-500)"}
          color="var(--gray-700)"
        >
          더보기
        </GeneralButton>
      </GeneralButtonWrapper>
      {/* </ModalContainerWrapper> */}
    </GeneralModal>
  );
};

export default SearchModal;
