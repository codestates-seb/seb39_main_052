import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrashCan } from "@fortawesome/free-solid-svg-icons";
import {
  RecipeFrameOuterContainer,
  SpanWrapper,
} from "../MyRecipeBox/MyRecipeBoxStyle";
import RecipeFrame from "../RecipeFrame/RecipeFrame";
import UserName from "../../common/UserName/UserName";
import Pagination from "../../common/Pagination/Pagination";
import { useEffect, useState } from "react";
import axios from "axios";

const MyRecipeLikedBox = ({ timeSince }) => {
  const [myLikedRecipeList, setMyLikedRecipeList] = useState([]);
  const [page, setPage] = useState(1); // 페이지네이션으로 바뀔 현재 페이지 위치
  const [total, setTotal] = useState(0); //전체 게시글 수
  const [totalPages, setTotalPages] = useState(0); //전체 페이지 수
  const [isUpdated, setIsUpdated] = useState(false); //좋아요한 레시피 삭제시 업뎃여부로 화면에 재렌더링 하려고

  //내가 좋아한 레시피 목록 조회하기
  const getMyLikedRecipeList = async () => {
    try {
      const { data } = await axios.get(`/api/recipes/favorite?page=${page}`);
      console.log(data);
      setTotal(data.pageInfo.totalElements);
      setTotalPages(data.pageInfo.totalPages);
      setMyLikedRecipeList([...data.data]);
    } catch (err) {
      console.log(err);
    }
  };

  //페이지 바뀔때, 좋아하는 레시피 목록에서 삭제할때마다 재렌더링
  useEffect(() => {
    getMyLikedRecipeList();
  }, [page, isUpdated]);

  const dummyData = [
    {
      id: 1,
      imagePath:
        "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
      title:
        "백종원의 들깨칼국수칼국수 들꺠는 뭘로 만들어지는건가요 들꺠는 참기름인가요",
      memberName: "들깨러버들깨러버버버",
      likes: 221,
      views: 1200,
      numComments: 3,
      lastModifiedAt: "2022-09-29T18:54:46.996434",
      memberImage:
        "https://www.tocanvas.net/wp-content/uploads/2022/02/food-aesthetic.jpeg",
    },
    {
      id: 2,
      imagePath:
        "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
      title: "웰시코기코기코기콜기 what do i call u 넌 남이잖아",
      memberName: "웰시코기궁둥",
      likes: 10,
      views: 120,
      numComments: 10,
      lastModifiedAt: "2022-09-29T18:54:46.996434",
      memberImage:
        "https://www.tocanvas.net/wp-content/uploads/2022/02/food-aesthetic.jpeg",
    },
    {
      id: 3,
      imagePath:
        "https://www.tocanvas.net/wp-content/uploads/2022/02/food-aesthetic.jpeg",
      title: "퐁신퐁신 팬케이크 만들기 퐁신퐁신 팬케이크 만들기",
      memberName: "마치라잌웰시코기궁둥",
      likes: 50,
      views: 2220,
      numComments: 20,
      lastModifiedAt: "2022-09-29T18:54:46.996434",
      memberImage:
        "https://www.tocanvas.net/wp-content/uploads/2022/02/food-aesthetic.jpeg",
    },
    {
      id: 4,
      imagePath:
        "https://i.pinimg.com/564x/d5/e0/ac/d5e0ac981b650a5731cd268bdbb42397.jpg",
      title:
        "집에서 로투스 크림라떼 만들기 집에서 로투스 크림라떼 만들기 집에서 로투스 크림라떼 만들기",
      memberName: "홈카페대마왕",
      likes: 40,
      views: 50,
      numComments: 50,
      lastModifiedAt: "2022-09-29T18:54:46.996434",
      memberImage:
        "https://www.tocanvas.net/wp-content/uploads/2022/02/food-aesthetic.jpeg",
    },
    {
      id: 5,
      imagePath:
        "https://i.pinimg.com/564x/d5/e0/ac/d5e0ac981b650a5731cd268bdbb42397.jpg",
      title:
        "집에서 로투스 크림라떼 만들기 집에서 로투스 크림라떼 만들기 집에서 로투스 크림라떼 만들기",
      memberName: "홈카페대마왕",
      likes: 40,
      views: 50,
      numComments: 50,
      lastModifiedAt: "2022-09-29T18:54:46.996434",
      memberImage:
        "https://www.tocanvas.net/wp-content/uploads/2022/02/food-aesthetic.jpeg",
    },
  ];

  return (
    <>
      <RecipeFrameOuterContainer>
        {myLikedRecipeList.map((data) => (
          <RecipeFrame
            key={data.id}
            imagePath={data.imagePath}
            title={data.title}
            date={timeSince(Date.parse(data.lastModifiedAt))}
            icon2={<FontAwesomeIcon icon={faTrashCan}></FontAwesomeIcon>}
          >
            {/* <SpanWrapper>
            by <span>{data.memberName}</span>
          </SpanWrapper> */}
            <UserName
              image={data.member.profileImagePath}
              name={data.member.name}
            ></UserName>
          </RecipeFrame>
        ))}
      </RecipeFrameOuterContainer>
      <Pagination
        page={page}
        setPage={setPage}
        totalPages={totalPages}
      ></Pagination>
    </>
  );
};

export default MyRecipeLikedBox;
// {dummyData.map((data) => (
//   <RecipeFrame
//     key={data.id}
//     imagePath={data.imagePath}
//     title={data.title}
//     date={timeSince(Date.parse(data.lastModifiedAt))}
//     icon2={<FontAwesomeIcon icon={faTrashCan}></FontAwesomeIcon>}
//   >
//     {/* <SpanWrapper>
//     by <span>{data.memberName}</span>
//   </SpanWrapper> */}
//     <UserName
//       image={data.memberImage}
//       name={data.memberName}
//     ></UserName>
//   </RecipeFrame>
// ))}
