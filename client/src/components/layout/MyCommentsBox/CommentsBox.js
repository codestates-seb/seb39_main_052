import axios from "axios";
import { useEffect, useState } from "react";
import Pagination from "../../common/Pagination/Pagination";
import UserName from "../../common/UserName/UserName";
import {
  CommentsBoxContainer,
  CommentsCollection,
  UserImg,
  UserNameTag,
} from "./CommentsBoxStyle";
import logoface from "../../../assets/small_logoface.png";
import { useNavigate } from "react-router-dom";
import { NoticeMsgBox } from "../MyRecipeBox/MyRecipeBoxStyle";
import { useDispatch, useSelector } from "react-redux";
import { setWarningToast } from "../../../features/toastSlice";

const CommentsBox = ({ timeSince }) => {
  const [commentsList, setCommentsList] = useState([]); //서버에서 받아오는 댓글 리스트 저장

  const [page, setPage] = useState(1); // 페이지네이션으로 바뀔 현 페이지 위치
  const [total, setTotal] = useState(0); //전체 댓글 수
  const [totalPages, setTotalPages] = useState(0); //전체 댓글 페이지 수

  const navigate = useNavigate();
  const dispatch = useDispatch();

  // const date = "2022-09-29T18:54:46.996434";
  // console.log("함수실험", timeSince(Date.parse(date)));

  const userToken = useSelector((state) => {
    return state.user.userToken;
  });

  useEffect(() => {
    getCommentsList();
  }, [page]); //page넣어줘야 page바뀔때마다 리스트 띄워줌

  //서버에서 받은 댓글 리스트 조회 통신 받아오기
  const getCommentsList = async () => {
    try {
      const { data } = await axios.get(`/api/comments/received?page=${page}`, {
        headers: { Authorization: `Bearer ${userToken}` },
      }); //axios promise객체 찍어보면 {data: {…}, status: 200, statusText: 'OK', headers: {…}, config: {…}, …} 나옴. response body에 해당되는 data만 구조분해할당
      console.log(data); //{data: Array(4), pageInfo: {…}}
      setTotal(data.pageInfo.totalElements);
      setTotalPages(data.pageInfo.totalPages);
      setCommentsList([...data.data]); //[{…}, {…}, {…}, {…}]
    } catch (err) {
      // alert 창 대체
      dispatch(setWarningToast({ message: err }))
    }
  };
  // console.log("서버에서받아온 댓글리스트", commentsList);

  //댓글 누르면 해당 레시피 상세페이지로 navigate 해서 댓글 볼수있게
  const clickRecipeDetail = (recipeId) => {
    navigate(`/recipes/${recipeId}`);
  };

  //Don't forget 리턴!!!
  return (
    <>
      {commentsList.length === 0 && (
        <NoticeMsgBox>아직 받은 댓글이 없어요</NoticeMsgBox>
      )}
      {commentsList.length > 0 && (
        <CommentsBoxContainer className="CommentsBoxContainer">
          <CommentsCollection className="CommentsCollection">
            <div>
              {commentsList.map((eachComment, idx) => (
                <div key={idx}>
                  <span>
                    <UserImg
                      src={
                        eachComment.member.profileImagePath === null
                          ? logoface
                          : eachComment.member.profileImagePath
                      }
                    />
                    <UserNameTag>{eachComment.member.name}</UserNameTag>
                    {/* <UserName
                      image={eachComment.member.profileImagePath}
                      name={eachComment.member.name}
                    ></UserName> */}
                  </span>
                  <span
                    className="comment"
                    onClick={() => clickRecipeDetail(eachComment.recipeId)}
                  >
                    {eachComment.content}
                  </span>
                  <span className="date">
                    {/* {dateConverter(eachComment.createdAt)} */}
                    {timeSince(Date.parse(eachComment.createdAt))}
                  </span>
                </div>
              ))}
            </div>
          </CommentsCollection>
          {/* 페이지네이션 들어갈 자리 */}
          {total > 10 && (
            <Pagination
              page={page}
              setPage={setPage}
              totalPages={totalPages}
            ></Pagination>
          )}
        </CommentsBoxContainer>
      )}
    </>
  );
};

export default CommentsBox;
