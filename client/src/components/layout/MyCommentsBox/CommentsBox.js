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

const CommentsBox = () => {
  const [commentsList, setCommentsList] = useState([]); //서버에서 받아오는 댓글 리스트 저장

  const [page, setPage] = useState(1); // 페이지네이션으로 바뀔 현 페이지 위치
  const [total, setTotal] = useState(0); //전체 댓글 수
  const [totalPages, setTotalPages] = useState(0); //전체 댓글 페이지 수

  useEffect(() => {
    getCommentsList();
  }, [page]); //page넣어줘야 page바뀔때마다 리스트 띄워줌

  // date 표기 (YYYY-MM-DD)
  const dateConverter = (createdAt) => {
    const date = new Date(+new Date(createdAt) + 3240 * 10000)
      .toISOString()
      .split("T")[0];
    return date;
  };

  //서버에서 받은 댓글 리스트 조회 통신 받아오기
  const getCommentsList = async () => {
    try {
      const { data } = await axios.get(`/api/comments/received?page=${page}`); //axios promise객체 찍어보면 {data: {…}, status: 200, statusText: 'OK', headers: {…}, config: {…}, …} 나옴. response body에 해당되는 data만 구조분해할당
      console.log(data); //{data: Array(4), pageInfo: {…}}
      setTotal(data.pageInfo.totalElements);
      setTotalPages(data.pageInfo.totalPages);
      setCommentsList([...data.data]); //[{…}, {…}, {…}, {…}]
    } catch (err) {
      alert(err);
    }
  };
  // console.log("서버에서받아온 댓글리스트", commentsList);

  //Don't forget 리턴!!!
  return (
    <CommentsBoxContainer className="CommentsBoxContainer">
      <CommentsCollection className="CommentsCollection">
        <div>
          {commentsList.map((eachComment, idx) => (
            <div key={idx}>
              <span>
                <UserImg src={eachComment.member.profileImagePath} />
                <UserNameTag>{eachComment.member.name}</UserNameTag>
                {/* <UserName
                  image={eachComment.member.profileImagePath}
                  name={eachComment.member.name}
                ></UserName> */}
              </span>
              <span className="comment">{eachComment.content}</span>
              <span className="date">
                {dateConverter(eachComment.createdAt)}
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
  );
};

export default CommentsBox;
