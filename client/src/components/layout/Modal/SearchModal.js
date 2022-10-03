import GeneralModal from "./GeneralModal";
import ModalSearchBar from "../ModalSearchBar.js/ModalSearchBar";
import RecipeCard from "../RecipeCard/RecipeCard";
import {
  RecipeCardWrapper,
  RecipeCardWrapperMobile,
  ModalSearchBarWrapper,
  GeneralButtonWrapper,
  NoticeMsg,
} from "./SearchModalStyle";
import GeneralButton from "../../common/Button/GeneralButton";
import { useEffect, useState } from "react";
import {
  Link,
  useLocation,
  useNavigate,
  useSearchParams,
} from "react-router-dom";
import axios from "axios";
import RecipeCardMobile from "../RecipeCardMobile/RecipeCardMobile";

const SearchModal = ({ handleClose }) => {
  const [isThereInput, setIsThereInput] = useState(false); // 검색어 input 있는지
  const [isThereResult, setIsThereResult] = useState(false); // 검색 결과 값 존재하는지
  const [searchResult, setSearchResult] = useState([]); //서버에서 받아온 레시피 목록 데이터 저장
  const [searchParams, setSearchParams] = useSearchParams();
  const searchTerm = searchParams.get("keyword");
  const navigate = useNavigate();

  const { search } = useLocation();

  //레시피 검색 요청
  const SearchRequest = async () => {
    const payload = {
      title: searchTerm ? searchTerm : "", // 서치값 없을 때 null 요청 방지
      ingredients: [],
      page: 1,
      sort: "HEART",
    };
    console.log("리퀘스트 페이로드", payload);

    if (payload.title !== "") {
      // if (searchTerm !== "") {
      //검색어 input 있어야지만 서버에 통신하기
      setIsThereInput(true); // 검색 input 했는지 상태 true로 만들기
      try {
        const { data } = await axios.post(`/api/recipes/search`, payload);
        console.log("검색데이터", data.data); //1검색시 [{id: 1, title: '11',…}, {…}, {…}, {…}, {…}]
        // console.log("검색데이터개수", data.pageInfo.totalElements); //5개
        data.pageInfo.totalElements > 0
          ? setIsThereResult(true)
          : setIsThereResult(false);
        //서버에서 받는 검색데이터 존재해야지만 검색결과값 존재상태 true로 만들기
        //서버에서 받는 검색데이터 없으면 검색결과값 상태 false

        //어차피 검색모달창에서는 결과값 4개만 보여줄거니까 네개 이상이면 4개까지 자르기
        if (data.pageInfo.totalElements >= 4) {
          const initialSearchResult = [...data.data];
          const slicedSearchResult = initialSearchResult.slice(0, 4);
          setSearchResult([...slicedSearchResult]);
          console.log("4개이상일때 searchResult 저장된 상태?", searchResult);
        }
        //서버에서받는 데이터 4개이상아니면 모든 데이터를 상태에 저장
        else {
          setSearchResult([...data.data]);
          console.log("4개이하일때 searchResult 저장된 상태?", searchResult);
        }
      } catch (err) {
        console.log(err);
      }
    } else {
      //검색어 input 없으면 false
      setIsThereInput(false);
    }
  };

  //서버에 input searchTerm 바뀔때마다 요청 보내기
  useEffect(() => {
    SearchRequest();
  }, [searchTerm]);

  //더보기 버튼 누르면 모달 닫히기 + 검색어 있는 채로 navigate 하기
  const clickShowMore = () => {
    console.log("searchTerm값", searchTerm);
    navigate(`/search?keyword=${searchTerm}`); //냉장고파먹기 페이지에서 searchParams 초기화해주는 코드 지우니 잘됨!

    // navigate({
    //   pathname: "search",
    //   search: `?keyword=${searchTerm}`,
    // }); //똑같이 작동

    // const params = { keyword: searchTerm };
    // navigate({
    //   pathname: "search",
    //   search: `?${createSearchParams({ keyword: searchTerm })}`,
    // }); //얘도 잘 작동

    //location 정보로 연결하기
    //location 값 ?keyword=3 으로 연결
    // console.log("로케이션값", search);
    // navigate(`search${search}`); //잘 됨

    handleClose();
  };

  return (
    <GeneralModal handleClose={handleClose} width="864px" height={"490px"}>
      {/* <ModalContainerWrapper> */}{" "}
      {/* GeneralModal 부모 컴포넌트에서 정의된 ModalContainer css 바꾸려면? GeneralModal에서 props 만들고 GeneralModal스타일에서 변경. 자식인 SearchModal에서 props받아와서 쓰기 */}
      <ModalSearchBarWrapper>
        <ModalSearchBar />
      </ModalSearchBarWrapper>
      {/* 검색어 input 없을때 */}
      <NoticeMsg className={isThereInput && "invisible"}>
        검색어를 입력하여 원하는 레시피를 찾아보세요!
      </NoticeMsg>
      {/* 검색어 input은 했는데 검색 결과값 없을때 */}
      <NoticeMsg
        className={!isThereInput || isThereResult ? "invisible" : null}
      >
        {`검색어를 찾을 수 없어요 ㅠㅠ \n 검색 범위를 넓혀 보는건 어때요?`}
      </NoticeMsg>
      {/* 검색어 input했고 검색결과 있어야지 레시피카드 + 더보기 버튼 띄우기 */}
      {
        <RecipeCardWrapper
          className={!isThereInput || !isThereResult ? "invisible" : null}
        >
          {searchResult.map((el) => (
            <RecipeCard
              detectOnClick={handleClose}
              key={el.id}
              id={el.id}
              imagePath={el.imagePath}
              title={el.title}
              memberName={el.member.name}
              memberImage={el.member.profileImagePath}
              heartCounts={el.heartCounts}
              views={el.view}
            />
          ))}
        </RecipeCardWrapper>
      }
      {/* 모바일 버전 레시피 카드 */}
      <RecipeCardWrapperMobile
        className={!isThereInput || !isThereResult ? "invisible" : null}
      >
        {searchResult.map((el) => (
          <RecipeCardMobile
            detectOnClick={handleClose}
            key={el.id}
            id={el.id}
            imagePath={el.imagePath}
            title={el.title}
            memberName={el.member.name}
            memberImage={el.member.profileImagePath}
            heartCounts={el.heartCounts}
            views={el.view}
          />
        ))}
      </RecipeCardWrapperMobile>
      <GeneralButtonWrapper
        className={!isThereInput || !isThereResult ? "invisible" : null}
      >
        <GeneralButton
          onClick={clickShowMore}
          className="small"
          backgroundColor="var(--mint-400)"
          hoverBackgroundColor={"var(--mint-500)"}
          color="var(--gray-700)"
        >
          더보기
        </GeneralButton>
      </GeneralButtonWrapper>
      {/* </ModalContainerWrapper> */}
    </GeneralModal>
  );
};

export default SearchModal;
