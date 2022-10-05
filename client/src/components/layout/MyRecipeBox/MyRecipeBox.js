import RecipeFrame from "../RecipeFrame/RecipeFrame";
import {
  RecipeFrameOuterContainer,
  SpanWrapper,
  SortingTabWrapper,
  NoticeMsgBox,
} from "./MyRecipeBoxStyle";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faPencil,
  faTrashCan,
  faEye,
  faHeart,
  faCommentDots,
} from "@fortawesome/free-solid-svg-icons";
import Pagination from "../../common/Pagination/Pagination";
import { useEffect, useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import SortingTab from "../../common/SortingTab/SortingTab";

import { useDispatch, useSelector } from "react-redux";
import { loadRecipe } from "../../../features/recipeSlice";
import GeneralButton from "../../common/Button/GeneralButton";

const MyRecipeBox = ({ timeSince }) => {
  const [myRecipeList, setMyRecipeList] = useState([]);
  const [page, setPage] = useState(1); // 페이지네이션으로 바뀔 현재 페이지 위치
  const [total, setTotal] = useState(0); //전체 게시글 수
  const [totalPages, setTotalPages] = useState(0); //전체 페이지 수
  const [isUpdated, setIsUpdated] = useState(false); //레시피 삭제,수정시 업뎃여부로 화면에 재렌더링 하려고

  const [sortMode, setSortMode] = useState("RECENT"); //정렬탭
  const navigate = useNavigate();
  const dispatch = useDispatch();

  // date 표기 (YYYY-MM-DD)
  // const dateConverter = (createdAt) => {
  //   const date = new Date(+new Date(createdAt) + 3240 * 10000)
  //     .toISOString()
  //     .split("T")[0];
  //   return date;
  // };

  //최신순 내 레시피 목록 조회하기
  const getMyRecipeList = async () => {
    try {
      const { data } = await axios.get(
        `/api/recipes/my?page=${page}&sort=${sortMode}`
      );
      console.log(data);
      console.log("sort모드?", sortMode);
      setTotal(data.pageInfo.totalElements);
      setTotalPages(data.pageInfo.totalPages);
      setMyRecipeList([...data.data]);
    } catch (err) {
      alert(err);
    }
  };

  //페이지 바뀔때, 레시피 삭제로 업데이트시, 정렬 기준 바뀔때 화면에 내 레시피 목록 재 렌더링
  useEffect(() => {
    getMyRecipeList();
    setIsUpdated(false); //명시적으로 넣어주어야 RecipeFrame 컴포넌트에서 처음 삭제하고 setIsUpdated 상태 true가 된 이후에 기본 상태 false로 돌아간다.
  }, [page, isUpdated, sortMode]);

  //레시피 상세페이지로 navigate 시키기
  const clickRecipeDetail = (recipeId) => {
    navigate(`/recipes/${recipeId}`);
  };

  //레시피 수정페이지로 보내기 이전에 해당 레시피 상세 데이터 불러오고 리덕스에 저장해놓기
  const getRecipe = async (recipeId) => {
    try {
      const { data } = await axios.get(`/api/recipes/${recipeId}`);
      dispatch(
        loadRecipe({
          recipeId: data.id,
          memberName: data.member.name,
          memberId: data.member.id,
          profileImagePath: data.member.profileImagePath,
          createdAt: data.createdAt,
          heartCounts: data.heartCounts,
          view: data.view,
          title: data.title,
          portion: data.portion,
          time: data.time,
          mainImage: data.imageInfo.imagePath,
          ingredients: data.ingredients,
          steps: data.steps,
        })
      );
    } catch (err) {
      console.log("레시피 상세데이터 불러오기 실패", err);
    }
  };

  //내 레시피 수정페이지로 navigate 시키기
  const clickRecipeEdit = (recipeId) => {
    getRecipe(recipeId); //먼저 상세 데이터 불러오고 리덕스에 저장
    navigate(`/recipes/edit`);
  };

  return (
    <>
      {myRecipeList.length === 0 && (
        <NoticeMsgBox>
          아직 작성한 레시피가 없어요
          <GeneralButton width={"180px"} className={"shadow"}>
            <FontAwesomeIcon
              icon={faPencil}
              style={{ marginRight: 5 }}
            ></FontAwesomeIcon>
            <Link to="/recipes/new">레시피 작성하러가기</Link>
          </GeneralButton>
        </NoticeMsgBox>
      )}
      {myRecipeList.length > 0 && (
        <div>
          <SortingTabWrapper>
            <SortingTab sortMode={sortMode} setSortMode={setSortMode} />
          </SortingTabWrapper>
          <RecipeFrameOuterContainer>
            {myRecipeList.map((data) => (
              <RecipeFrame
                onClickRecipeDetail={() => {
                  clickRecipeDetail(data.id);
                }}
                onClickIconEdit={() => clickRecipeEdit(data.id)}
                // onClickIconDelete={
                //   () => handleRecipeDelete(data.id)
                //   // useConfirm("정말 삭제할까요?", confirm, cancel) //리액트훅은 콜백 안에서 쓰일 수 없다 에러
                // }
                key={data.id}
                recipeIdProp={data.id}
                setIsUpdated={setIsUpdated}
                imagePath={data.imagePath}
                title={data.title}
                // date={dateConverter(data.lastModifiedAt)}
                date={timeSince(Date.parse(data.lastModifiedAt))}
                icon1={<FontAwesomeIcon icon={faPencil}></FontAwesomeIcon>}
                icon2={<FontAwesomeIcon icon={faTrashCan}></FontAwesomeIcon>}
                mode={"my_recipe"}
              >
                {/* children props 로 자식 컴포넌트로 전달되는 요소들 */}
                <SpanWrapper>
                  <span>
                    <FontAwesomeIcon icon={faEye}></FontAwesomeIcon>
                  </span>
                  <span>{data.view}</span>
                </SpanWrapper>
                <SpanWrapper>
                  <span>
                    <FontAwesomeIcon icon={faHeart}></FontAwesomeIcon>
                  </span>
                  <span>{data.heartCounts}</span>
                </SpanWrapper>
                <SpanWrapper>
                  <span>
                    <FontAwesomeIcon icon={faCommentDots}></FontAwesomeIcon>
                  </span>
                  <span>{data.commentCounts}</span>
                </SpanWrapper>
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

export default MyRecipeBox;
