import axios from "axios";
import Pagination from "../../common/Pagination/Pagination";
import UserName from "../../common/UserName/UserName";
import {
  CommentsBoxContainer,
  CommentsCollection,
  UserImg,
  UserNameTag,
} from "./CommentsBoxStyle";

const CommentsBox = () => {
  // const dummyData = [
  //   {
  //     id: 1,
  //     memberName: "들깨러버들깨러버",
  //     memberImage:
  //       "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",

  //     comment: "완전 맛있겠다 완전 맛있겠다 완전 맛있겠다 완전 맛있겠다",

  //     date: "2022.9.26.",
  //   },
  //   {
  //     id: 2,
  //     memberName: "멋쟁이토마토르먹으면",
  //     memberImage:
  //       "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
  //     comment:
  //       "냠냠 멋쟁이토마토르먹으면 쩝쩝박사가 혼내주러옵니다 곶감하나만 주면 안잡아먹지",
  //     date: "2022.8.11",
  //   },
  //   {
  //     id: 3,
  //     memberName: "웰시코기궁둥궁둥궁둥궁",
  //     memberImage:
  //       "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
  //     comment: "뽀송뽀송 ",
  //     date: "2022.9.1.",
  //   },
  //   {
  //     id: 10,
  //     memberName: "웰시코기궁둥궁둥궁둥궁",
  //     memberImage:
  //       "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
  //     comment: "뽀송뽀송 ",
  //     date: "2022.9.1.",
  //   },
  //   {
  //     id: 4,
  //     memberName: "웰시코기궁둥궁둥궁둥궁",
  //     memberImage:
  //       "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
  //     comment: "뽀송뽀송 ",
  //     date: "2022.9.1.",
  //   },
  //   {
  //     id: 5,
  //     memberName: "웰시코기궁둥궁둥궁둥궁",
  //     memberImage:
  //       "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
  //     comment: "뽀송뽀송 ",
  //     date: "2022.9.1.",
  //   },
  //   {
  //     id: 6,
  //     memberName: "웰시코기궁둥궁둥궁둥궁",
  //     memberImage:
  //       "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
  //     comment: "뽀송뽀송 ",
  //     date: "2022.9.1.",
  //   },
  //   {
  //     id: 7,
  //     memberName: "웰시코기궁둥궁둥궁둥궁",
  //     memberImage:
  //       "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
  //     comment: "뽀송뽀송 ",
  //     date: "2022.9.1.",
  //   },
  //   {
  //     id: 8,
  //     memberName: "웰시코기궁둥궁둥궁둥궁",
  //     memberImage:
  //       "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
  //     comment: "뽀송뽀송 ",
  //     date: "2022.9.1.",
  //   },
  //   {
  //     id: 9,
  //     memberName: "웰시코기궁둥궁둥궁둥궁",
  //     memberImage:
  //       "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
  //     comment: "뽀송뽀송 ",
  //     date: "2022.9.1.",
  //   },
  // ];

  //서버에서 받은 댓글 리스트 조회 통신 받아오기
  // const getCommentsList = async () => {
  //   try{
  //       await axios.get(`/api/comments/received?page=${})`)
  //   }
  //   catch (err) {
  //     alert(err)
  //   }
  // }

  //Don't forget 리턴!!!
  return (
    <CommentsBoxContainer className="CommentsBoxContainer">
      <CommentsCollection className="CommentsCollection">
        <div>
          {dummyData.map((data, idx) => (
            <div key={idx}>
              <span>
                <UserImg src={data.memberImage} />
                <UserNameTag>{data.memberName}</UserNameTag>
                {/* <UserName
                  image={data.memberImage}
                  name={data.memberName}
                ></UserName> */}
              </span>
              <span className="comment">{data.comment}</span>
              <span className="date">{data.date}</span>
            </div>
          ))}
        </div>
      </CommentsCollection>
      {/* 페이지네이션 들어갈 자리 */}
      <Pagination total="10" limit="10"></Pagination>
    </CommentsBoxContainer>
  );
};

export default CommentsBox;
