import styled from "styled-components";

export const CommentsBoxContainer = styled.div`
  width: 80%;
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
    padding: 8px 0;
    display: flex;
    flex-direction: row;
    border-bottom: solid 1px var(--gray-300);
  }

  & > div > div > span {
    padding-right: 16px;
    flex-basis: 30%;
    display: flex;

    &.comment {
      padding: 0;
      flex-basis: 50%;
    }
    &.date {
      justify-content: flex-end;
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
`;
