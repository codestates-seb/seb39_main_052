import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrashCan } from "@fortawesome/free-solid-svg-icons";
import {
  RecipeFrameOuterContainer,
  NoticeMsgBox,
} from "../MyRecipeBox/MyRecipeBoxStyle";
import RecipeFrame from "../RecipeFrame/RecipeFrame";
import UserName from "../../common/UserName/UserName";
import Pagination from "../../common/Pagination/Pagination";
import { useEffect, useState } from "react";
import axios from "axios";
import { useSelector } from "react-redux";

const MyRecipeLikedBox = ({ timeSince }) => {
  const [myLikedRecipeList, setMyLikedRecipeList] = useState([]);
  const [page, setPage] = useState(1); // 페이지네이션으로 바뀔 현재 페이지 위치
  const [total, setTotal] = useState(0); //전체 게시글 수
  const [totalPages, setTotalPages] = useState(0); //전체 페이지 수
  const [isUpdatedForLikedList, setIsUpdatedForLikedList] = useState(false); //좋아요한 레시피 삭제시 업뎃여부로 화면에 재렌더링 하려고

  const userToken = useSelector((state) => {
    return state.user.userToken;
  });

  //내가 좋아한 레시피 목록 조회하기
  const getMyLikedRecipeList = async () => {
    try {
      const { data } = await axios.get(`/api/recipes/favorite?page=${page}`, {
        headers: { Authorization: `Bearer ${userToken}` },
      });
      console.log(data);
      setTotal(data.pageInfo.totalElements);
      setTotalPages(data.pageInfo.totalPages);
      setMyLikedRecipeList([...data.data]);
    } catch (err) {
      console.log(err);
    }
  };

  //페이지 바뀔때, 좋아하는 레시피 목록에서 삭제할때마다 재렌더링
  useEffect(() => {
    getMyLikedRecipeList();
    setIsUpdatedForLikedList(false);
  }, [page, isUpdatedForLikedList]);

  return (
    <>
      {myLikedRecipeList.length === 0 && (
        <NoticeMsgBox>아직 내가 좋아한 레시피가 없어요</NoticeMsgBox>
      )}
      {myLikedRecipeList.length > 0 && (
        <div>
          <RecipeFrameOuterContainer>
            {myLikedRecipeList.map((data) => (
              <RecipeFrame
                key={data.id}
                recipeIdForLikedList={data.id}
                setIsUpdatedForLikedList={setIsUpdatedForLikedList}
                imagePath={data.imagePath}
                title={data.title}
                date={timeSince(Date.parse(data.lastModifiedAt))}
                icon2={
                  <FontAwesomeIcon
                    icon={faTrashCan}
                    mode={"my_liked_recipe"}
                  ></FontAwesomeIcon>
                }
              >
                {/* <SpanWrapper>
            by <span>{data.memberName}</span>
          </SpanWrapper> */}
                <UserName
                  image={data.member.profileImagePath}
                  name={data.member.name}
                ></UserName>
              </RecipeFrame>
            ))}
          </RecipeFrameOuterContainer>

          <Pagination
            page={page}
            setPage={setPage}
            totalPages={totalPages}
          ></Pagination>
        </div>
      )}
    </>
  );
};

export default MyRecipeLikedBox;
