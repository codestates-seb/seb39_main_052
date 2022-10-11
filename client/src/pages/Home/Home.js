import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useSelector } from "react-redux";
import axios from "axios";
import logoface from "../../assets/small_logoface.png";
import { Container, EmptyBox, Header, Image, Name, RecipeWrapper } from "./HomeStyle";
import Carousel from "../../components/layout/Carousel/Carousel";
import IntroCarousel from "../../components/layout/Carousel/IntroCarousel";

const Home = () => {

  //로그인 상태 가져와서 변수에 저장
  const isLoggedIn = useSelector((state) => {
    return state.user.isLoggedIn;
  });

  // access 토큰 가져와서 변수에 저장 (새로고침시 통신 header에 바로 저장되지 않는 에러로 인한 임시 방편)
  const userToken = useSelector((state) => {
    return state.user.userToken;
  });

  const [isEmptyResult, setIsEmptyResult] = useState(false);
  const [popularRecipes, setPopularRecipes] = useState([]); // 인기 레시피
  const [recentRecipes, setRecentRecipes] = useState([]); // 최신 레시피
  const [recipesForMe, setRecipesForMe] = useState([]);

  useEffect(() => {
    if (isLoggedIn) {
      fetchRecipesForMe();
    }
    else {
      fetchRecentRecipes();
    }
    fetchPopularRecipes();
  }, [isLoggedIn])

  // 인기 레시피
  const fetchPopularRecipes = async() => {
    try {
      const { data } = await axios.get(`/api/recipes/recommend/popular`);
      // console.log("인기레시피", data);
      setPopularRecipes([...data.data]);
    }
    catch (err) {
      console.log(err);
    }
  }
  // 최신 레시피
  const fetchRecentRecipes = async() => {
    try {
      const { data } = await axios.get(`/api/recipes/recommend/recent`);
      // console.log("최신레시피", data);
      setRecentRecipes([...data.data]);
    }
    catch (err) {
      console.log(err);
    }
  }
  // 내가 만들 수 있는 레시피
  const fetchRecipesForMe = async() => {
    try {
      const { data } = await axios.get(`/api/recipes/recommend/fridge`, {headers: {Authorization: `Bearer ${userToken}`}});
      // console.log("내가 만들 수 있는 레시피", data);
      setRecipesForMe([...data.data]);
    }
    catch (err) {
      console.log(err);
    }
  }

  return (
    <>
      <Container>
        {isLoggedIn
          ? <>
            <IntroCarousel />
            <Header>내가 만들 수 있는 레시피</Header>
            {recipesForMe.length > 0
              ? <Carousel recipes={recipesForMe} />
              : <EmptyBox>
                <div>
                  나의 냉장고에 재료를 추가하고 추천 레시피를 받아보세요!
                </div>
                <p><Link to="/myfridge">나의 냉장고 바로가기</Link></p>
              </EmptyBox>
            }
            <Header>인기 레시피</Header>
            <Carousel recipes={popularRecipes} />
          </>
          : <>
            <IntroCarousel />
            <Header>인기 레시피</Header>
            <Carousel recipes={popularRecipes} />
            <Header>최신 레시피</Header>
            <Carousel recipes={recentRecipes} />
          </>
        }
      </Container>
    </>
  );
};

export default Home;
