import axios from "axios";
import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import styled from "styled-components";
import GeneralButton from "../../common/Button/GeneralButton";
import Pagination from "../../common/Pagination/Pagination";
import AdminSearchBar from "./AdminSearchBar";

const AdminPanel = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [recipeId, setRecipeId] = useState(); //레시피id
  const [recipeTitle, setRecipeTitle] = useState(""); //레시피 제목
  const [recipeMemberName, setRecipeMemberName] = useState(""); //작성자 이름
  const [isUpdatedDone, setIsUpdatedDone] = useState(false); //삭제되어서 업데이트되었다는 상태
  // const [recipeRow, setRecipeRow] = useState([]);
  //서버에서 받는 개별 레시피 {} 데이터를 배열에 저장. 배열이 비면 업데이트된거니까.. 삭제하면 빈 리스트 띄워주려나? -no effect

  const recipeIdSearchParam = searchParams.get("recipe_id");
  console.log("서치파람값?", recipeIdSearchParam);

  //레시피 상세 조회 get 요청 보내기
  const getRecipeId = async () => {
    try {
      const { data } = await axios.get(`/api/recipes/${recipeIdSearchParam}`);
      console.log("데이터", data); //{id: 108, title: '햄버거', portion: 1, view: 111, time: '2', …}
      setRecipeId(data.id);
      setRecipeTitle(data.title);
      setRecipeMemberName(data.member.name);
      // setRecipeRow([data]); //[{}]
    } catch (err) {
      console.log(err);
      alert(err);
    }
  };

  const handleDelete = async () => {
    try {
      await axios.delete(`/api/recipes/${recipeIdSearchParam}`);
      alert("레시피 삭제 성공!");
      setSearchParams({ recipe_id: "" }); //성공하면 서치파람 비우기
      // setIsUpdatedDone(true); //삭제 완료되면 업데이트 상태 true로 바꾸기
      setRecipeTitle(""); //아예 레시피 타이틀, 아이디, 작성자 칸을 비우기 그래야 삭제후 화면에 렌더링될때 안보임
      setRecipeId("");
      setRecipeMemberName("");
    } catch (err) {
      alert(err);
    }
  };

  //검색어 바뀌고 난 이후, 삭제되고 난 이후 화면에 렌더링
  useEffect(() => {
    //처음 렌더링될때 검색어 있어야지만 레시피 받아오는 요청 보내기
    if (recipeIdSearchParam) {
      getRecipeId();
    }
  }, [recipeIdSearchParam]);
  // }, [recipeIdSearchParam, isUpdatedDone]);
  //상태에 저장되는건 검색을 하고나서만 일어나기때문에.. delete할때 목록에서 삭제해도 새로받아오는 리스트가 없기때문에 목록이 달라지는 렌더링은 일어나지않는다.
  //업데이트 상태 isUpdatedDone이 false였다가 삭제하고 난후 true로 바뀌면 다시 렌더링하긴하지만 화면에 띄워주는데는 아무런 효과가없다.

  return (
    <>
      <AdminSearchBar />
      <GeneralButton onClick={handleDelete} className="small">
        삭제
      </GeneralButton>
      <Panel>
        <span>레시피ID</span>
        <span>레시피제목</span>
        <span>작성자</span>
        {/* <Contents>
          {recipeRow.map((aRow) => (
            <Row
              recipeId={aRow.id}
              recipeTitle={aRow.title}
              recipeMemberName={aRow.member.name}
            ></Row>
          ))}
        </Contents> */}

        <RowWrapper className="RowWrapper">
          {/* //<span>{idx + 1}</span> */}
          <span>{recipeId}</span>
          <span className="title">{recipeTitle}</span>
          <span>{recipeMemberName}</span>
        </RowWrapper>
      </Panel>
    </>
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
