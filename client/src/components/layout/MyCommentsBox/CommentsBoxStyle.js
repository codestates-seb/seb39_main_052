import styled from "styled-components";

export const CommentsBoxContainer = styled.div`
  /* width: 90%; */
  background-color: var(--gray-100);
  border-radius: 10px;
  padding: 16px;
`;
export const CommentsCollection = styled.div`
  display: flex;
  justify-content: center;
  font-size: 14px;

  //each 댓글 박스
  & > div > div {
    min-width: 464px; //적용하면.. 흠 크기는 고정되는데.. 반응형은 안됨
    padding: 8px 0;
    display: flex;
    justify-content: center;
    flex-direction: row;
    :not(:last-of-type) {
      border-bottom: solid 1px var(--gray-300);
    }
    //박스 높이 넘치면 ... 으로 나타내려고 height 지정 2칸 height 56px 넘어가면 ...으로 나타내기
    /* height: 56px; */
  }

  & > div > div > span {
    padding-right: 16px;
    flex-basis: 25%;
    display: flex;
    cursor: pointer;

    &.comment {
      /* margin-bottom: 8px; */
      flex-basis: 50%;
      //2줄 넘어가면 ellipsis ...으로 보여주기
      text-overflow: ellipsis;
      overflow: hidden;
      // Addition lines for 2 line or multiline ellipsis
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      white-space: normal;
    }
    &.date {
      justify-content: flex-end;
      flex-basis: 20%;
      padding: 0;
      font-size: 12px;
    }
  }
`;

export const UserImg = styled.img`
  width: 20px;
  height: 20px;
  border-radius: 50%;
  /* margin: 5px 0 0 5px; */
`;

export const UserNameTag = styled.div`
  padding-left: 8px;
  margin-bottom: 8px;
  font-size: 12px;
  //2줄 넘어가면 ellipsis ...으로 보여주기
  text-overflow: ellipsis;
  overflow: hidden;
  // Addition lines for 2 line or multiline ellipsis
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  white-space: normal;
`;
