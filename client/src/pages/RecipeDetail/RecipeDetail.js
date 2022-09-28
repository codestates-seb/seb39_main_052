import { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import { useCookies } from 'react-cookie';
import axios from "axios";
import { Heading, Ingredients, RecipeWrapper, SubHeading, Extra, Head, HeadLeft, Image, ButtonLike, HeadLeftTop, ButtonLikeWrapper, HeadLeftBottom, Info, PortionAndTime, RecipeId, LikeViewWrapper, View, Ingredient } from "./RecipeDetailStyle";
import RecipeStep from "../../components/layout/RecipeStep/RecipeStep";
import Comments from "../../components/layout/Comments/Comments";
import UserName from "../../components/common/UserName/UserName";
import LikeHeart from "../../components/common/LikeHeart/LikeHeart";
import { loadRecipe } from "../../features/recipeSlice";
import { useDispatch, useSelector } from "react-redux";

const RecipeDetail = () => {

    const [isMyRecipe, setIsMyRecipe] = useState(false);
    const { id } = useParams();
    // console.log("id", id);
    const [cookies] = useCookies(["id"]); // 쿠키에 저장된 내 유저 id값 (type은 string)
    const dispatch = useDispatch();

    // date 표기 (YYYY-MM-DD) 
    const dateConverter = (createdAt) => {
        const date = new Date(+new Date(createdAt) + 3240 * 10000).toISOString().split("T")[0]
        // const time = new Date().toTimeString().split(" ")[0];
        return date;
    }
    // 상세 레시피 데이터
    const recipe = useSelector((state) => {
        return state.recipe;
    })
    // console.log(recipe);

    // 레시피 상세 데이터 불러오기
    const getRecipe = async() => {
        try {
            const { data } = await axios.get(`/api/recipes/${id}`);
            console.log("방금 받아왔어욤", data);
            dispatch(loadRecipe({
                recipeId: data.id,
                memberName: data.member.name,
                memberId: data.member.id,
                profileImagePath: data.member.profileImagePath,
                createdAt: data.createdAt,
                heartCounts: data.heartCounts,
                view: data.view,
                title: data.title,
                portion: data.portion,
                time: data.time,
                mainImage: data.imageInfo.imagePath,
                ingredients: data.ingredients,
                steps: data.steps,
            }))
            // 해당 레시피가 내 레시피인지 확인 후 상태 변경
            data.member.id === Number(cookies.id) ? setIsMyRecipe(true) : setIsMyRecipe(false);
        }
        catch (error) {
            console.log(error);
        }
    }

    useEffect(() => {
        getRecipe();
    }, [id])

    return (
        <RecipeWrapper>
            <Extra>
                <RecipeId>등록일 &nbsp;{dateConverter(recipe.createdAt)} &nbsp; 게시글 #{recipe.id}</RecipeId>
                <ButtonLike className="dark" >
                    <Link to={"/search"} state={{ prevPath: "/recipes" }}>목록으로 돌아가기</Link>
                </ButtonLike>
            </Extra>
            <Head>
                <HeadLeft>
                    <HeadLeftTop>
                        <Heading>{recipe.title}</Heading>
                        <LikeViewWrapper>
                            <LikeHeart heartCounts={recipe.heartCounts} />
                            <View>조회수 {recipe.view}</View>
                        </LikeViewWrapper>
                    </HeadLeftTop>
                    <HeadLeftBottom>
                        <Info>
                            <UserName image={recipe.member.profileImagePath} name={recipe.member.name} className="large" />
                            <PortionAndTime>{recipe.portion} 인분</PortionAndTime>
                            <PortionAndTime>{recipe.time} 분 &nbsp;소요</PortionAndTime>
                        </Info>
                        {isMyRecipe &&
                            <ButtonLikeWrapper>
                                <ButtonLike><Link to="/recipes/edit">수정</Link></ButtonLike>
                                <ButtonLike>삭제</ButtonLike>
                            </ButtonLikeWrapper>
                        }
                    </HeadLeftBottom>
                </HeadLeft>
                <Image src={recipe.imageInfo.imagePath} alt={'main'} />
            </Head>
            <SubHeading>재료(계량)</SubHeading>
            <Ingredients>
                {recipe.ingredients.map((item, idx) => {
                    return (
                        <div key={idx}>
                            <Ingredient>{item.name}</Ingredient>
                            <Ingredient>{item.amount}</Ingredient>
                        </div>
                    )
                })}
            </Ingredients>
            <SubHeading>요리 순서</SubHeading>
            {recipe.steps.map((step, idx) => {
                return (
                    <RecipeStep
                        key={idx}
                        index={step.sequence}
                        image={step.imageInfo.imagePath}
                        content={step.content}
                    />
                )
            })}
            <Comments />
        </RecipeWrapper>
    )
};

export default RecipeDetail;