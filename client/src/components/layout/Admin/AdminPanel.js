import { useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import styled from "styled-components";
import GeneralButton from "../../common/Button/GeneralButton";
import Pagination from "../../common/Pagination/Pagination";
import AdminSearchBar from "./AdminSearchBar";

const AdminPanel = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const recipeIdSearchParam = searchParams.get("recipe_id");

  //레시피서치 파람 바뀔때 요청보내기
  useEffect(() => {}, [recipeIdSearchParam]);

  const dummy = [
    {
      id: 1,
      title:
        "나는타이틀이야1 바닐라바닐라바닐라바닐라바닐라바닐라바닐라바닐라바닐라바닐라바닐라바닐라",
    },
    {
      id: 2,
      title: "abc나는타이틀이야2",
      userName: "바닐라",
    },
    {
      id: 3,
      title: "나는타이틀이야3",
      userName: "바닐라",
    },
  ];
  return (
    <>
      <AdminSearchBar />
      <Panel>
        <span>레시피ID</span>
        <span>레시피제목</span>
        <span>삭제</span>
        <Contents>
          {dummy.map((el, idx) => (
            <Row key={idx} idx={idx} title={el.title} recipeId={el.id}></Row>
          ))}
        </Contents>
        <Pagination></Pagination>
      </Panel>
    </>
  );
};

const Row = ({ idx, title, recipeId }) => {
  const handleRowDelete = () => {};
  return (
    <RowWrapper className="RowWrapper">
      {/* <span>{idx + 1}</span> //넘버링 번호*/}
      <span>{recipeId}</span>
      <span className="title">{title}</span>
      <GeneralButton onClick={handleRowDelete} className="xsmall">
        삭제
      </GeneralButton>
    </RowWrapper>
  );
};
export default AdminPanel;

const RowWrapper = styled.div`
  display: flex;
  align-items: center;
  justify-items: flex-start;
  height: 30px;
  max-width: 25rem;

  & > span {
    padding-right: 8px;
  }
  > span.title {
    text-overflow: ellipsis;
    overflow: hidden;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    white-space: normal;
  }
`;

const Panel = styled.div`
  background-color: turquoise;
  width: fit-content;
  > span {
    padding: 8px;
  }
`;
const Contents = styled.div``;
