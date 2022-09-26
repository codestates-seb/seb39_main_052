import {
  CommentsBoxContainer,
  CommentsCollection,
  UserImg,
  UserNameTag,
} from "./CommentsBoxStyle";

const CommentsBox = () => {
  const dummyData = [
    {
      id: 1,
      memberName: "들깨러버들깨러버",
      memberImage:
        "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",

      comment: "완전 맛있겠다 완전 맛있겠다 완전 맛있겠다 완전 맛있겠다",

      date: "2022/09/26",
    },
    {
      id: 2,
      memberName: "2들깨러버들깨러버",
      memberImage:
        "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
      comment: "냠냠 ",
      date: "2022/08/11",
    },
  ];

  //Don't forget 리턴!!!
  return (
    <CommentsBoxContainer className="CommentsBoxContainer">
      <CommentsCollection className="CommentsCollection">
        <p>
          {dummyData.map((data, idx) => (
            <div>
              <span>
                <UserImg src={data.memberImage} />
                <UserNameTag>{data.memberName}</UserNameTag>
              </span>
              <span className="comment">{data.comment}</span>
              <span className="date">{data.date}</span>
            </div>
          ))}
        </p>
      </CommentsCollection>
      {/* 페이지네이션 들어갈 자리 */}
    </CommentsBoxContainer>
  );
};

export default CommentsBox;
