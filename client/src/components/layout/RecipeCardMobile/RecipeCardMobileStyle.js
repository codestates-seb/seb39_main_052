import styled from "styled-components";

export const Container = styled.div`
  width: 300px;
  background-color: var(--fridge-100);
  /* opacity: 70%; */
  box-shadow: var(--shadow-medium);
  padding: 16px;
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  border-radius: 10px;
`;

export const Image = styled.img`
  width: 100px;
  height: 100px;
  object-fit: cover;
`;

export const ContentWrapper = styled.div`
  margin: 8px;
  width: 168px;
`;

export const Title = styled.div`
  font-size: 14px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  margin-bottom: 8px;
`;

export const Doornob = styled.div``;

export const LikesAndViews = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: flex-start;
  margin-top: 10px;
  padding: 0 0 0 1px;
  // 조회수
  > div:nth-child(2) {
    padding-left: 16px;
    font-size: 11px;
    color: var(--fridge-800);
  }
`;
