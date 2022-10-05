import axios from "axios";
import { useParams } from "react-router-dom";
import useConfirm from "../../../hooks/useConfirm";
import {
  RecipeFrameContainer,
  DisplayImg,
  Description,
  Title,
  Spec,
  Date,
  Icon,
} from "./RecipeFrameStyle";

const RecipeFrame = ({
  imagePath,
  title,
  children,
  date,
  icon1,
  icon2,
  onClickRecipeDetail,
  onClickIconEdit,
  onClickIconDelete,
  recipeIdProp,
  setIsUpdated,
  mode,
  recipeIdForLikedList,
  setIsUpdatedForLikedList,
}) => {
  //데이터는 RecipeFrame 상위 컴포넌트에서 불러와야함 그래야 레시피 프레임을 map으로 여러개 나타낼수있음

  const { id } = useParams();
  // 내레시피 id = 1 , mode = "my_recipe"
  // 내좋아요 id = 2 , mode = "my_liked_recipe" 에 따라서 삭제핸들러 다르게 처리

  //useConfirm은 레시피 프레임을 map으로 loop하는 상위 컴포넌트에서 쓸 수 없어서 RecipeFrame 자체에서 불러오기
  //useConfirm hook 사용
  const confirm = (recipeId) => {
    if (mode === "my_recipe") {
      //내 레시피 삭제할때
      handleRecipeDelete(recipeId);
    } else {
      //내 좋아요 목록에서 삭제할때
      handleRemoveFromLikedList(recipeId);
    }
  };
  const cancel = () => {
    console.log("취소");
  };

  //내 레시피 삭제하기
  // const handleRecipeDelete = async (recipeId) => {
  //   try {
  //     await axios.delete(`/api/recipes/${recipeId}`);
  //     console.log("레시피 삭제 성공");
  //     setIsUpdated(true);
  //   } catch (err) {
  //     console.log(err);
  //     alert(`${recipeId}번 레시피를 삭제할 수 없어요 ㅠㅠ`);
  //   }
  // };

  //useConfirm용 내 레시피 삭제하기
  const handleRecipeDelete = async () => {
    try {
      await axios.delete(`/api/recipes/${recipeIdProp}`);
      console.log("레시피 삭제 성공");
      setIsUpdated(true);
    } catch (err) {
      console.log(err);
      alert(`${recipeIdProp}번 레시피를 삭제할 수 없어요 ㅠㅠ`);
    }
  };

  //내 좋아요 목록에서 삭제
  const handleRemoveFromLikedList = async () => {
    try {
      await axios.delete(`/api/recipes/${recipeIdForLikedList}/heart`);
      console.log(`${recipeIdForLikedList}번 레시피 하트 삭제`);
      setIsUpdatedForLikedList(true);
    } catch (err) {
      console.log(err);
      alert(`좋아요 목록에서 삭제할 수 없어요`);
    }
  };

  //컴포넌트로 refactoring
  return (
    <RecipeFrameContainer>
      <DisplayImg onClick={onClickRecipeDetail} src={imagePath}></DisplayImg>
      <Description onClick={onClickRecipeDetail}>
        <Title>{title}</Title>
        {/* <h2>{dummyData.title}</h2> */}
        {/* Spec은 내레시피 컴포넌트에는 조회수, 좋아요, 댓글수 div 한줄 / 내좋아요 컴포넌트에는 by 작성자 한줄 */}
        <Spec className="spec">{children} </Spec>
        <Date>{date}</Date>
      </Description>
      <Icon>
        <span onClick={onClickIconEdit}>{icon1}</span>
        <span
          // onClick={onClickIconDelete} //상위컴포넌트 MyRecipeBox에 삭제함수있고 prop으로 받아올때 -useConfirm불가
          onClick={
            // () => handleRecipeDelete(recipeIdProp)//useConfirm 없이
            useConfirm("정말 삭제할까요?", confirm, cancel)
            // console.log({ recipeIdProp }) //MyRecipeBox에서 받아온 prop {recipeIdProp: 29}
          } //onClick 안에서 내 레시피, 내 좋아요 모드 조건 분기해서 쓰려고했더니 useConfirm은 조건문에서 쓸수없는 에러
        >
          {icon2}
        </span>
      </Icon>
    </RecipeFrameContainer>
  );
};

export default RecipeFrame;
