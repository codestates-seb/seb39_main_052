import { useState, useEffect } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import axios from "axios";
import { Heading, Ingredients, RecipeWrapper, SubHeading, Extra, Head, HeadLeft, Image, ButtonLike, HeadLeftTop, ButtonLikeWrapper, HeadLeftBottom, Info, PortionAndTime, RecipeId, LikeViewWrapper, View, Ingredient } from "./RecipeDetailStyle";
import RecipeStep from "../../components/layout/RecipeStep/RecipeStep";
import Comments from "../../components/layout/Comments/Comments";
import UserName from "../../components/common/UserName/UserName";
import LikeHeart from "../../components/common/LikeHeart/LikeHeart";
import { loadRecipe } from "../../features/recipeSlice";
import { useDispatch, useSelector } from "react-redux";
import useConfirm from "../../hooks/useConfirm";
import { setWarningToast, setNoticeToast } from "../../features/toastSlice";
import Footer from "../../components/layout/Footer/Footer";

const RecipeDetail = () => {

    const [isLoading, setIsLoading] = useState(false);
    const [isMyRecipe, setIsMyRecipe] = useState(false);
    // console.log("내 레시피니?", isMyRecipe);

    const { id } = useParams();
    // console.log("id", id);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    // 로그인 시 리덕스에 저장한 내 아이디
    const myId = useSelector((state) => {
        return state.user.userId;
    })

    // date 표기 (YYYY-MM-DD) 
    const dateConverter = (createdAt) => {
        const date = new Date(+new Date(createdAt) + 3240 * 10000).toISOString().split("T")[0]
        // const time = new Date().toTimeString().split(" ")[0];
        return date;
    }
    // 레시피 상세 데이터 (글)
    const recipe = useSelector((state) => {
        return state.recipe;
    });
    // console.log(`redux 레시피`, recipe);

    // 로그인 상태 가져와서 변수에 저장
    const isLoggedIn = useSelector((state) => {
        return state.user.isLoggedIn;
    });

    // access token 상태 가져와서 변수에 저장 (새로고침시 통신 header에 바로 저장되지 않는 에러로 인한 임시 방편)
    const userToken = useSelector((state) => {
        return state.user.userToken;
    });

    // 레시피 상세 데이터 불러오기
    const getRecipe = async () => {
        if (isLoggedIn) {
            try {
                const { data } = await axios.get(`/api/recipes/${id}`, { headers: { Authorization: `Bearer ${userToken}` }})
                dispatch(loadRecipe({
                    recipeId: data.id,
                    memberName: data.member.name,
                    memberId: data.member.id,
                    profileImagePath: data.member.profileImagePath,
                    createdAt: data.createdAt,
                    heartCounts: data.heartCounts,
                    heartExist: data.heartExist,
                    view: data.view,
                    title: data.title,
                    portion: data.portion,
                    time: data.time,
                    mainImage: data.imageInfo.imagePath,
                    ingredients: data.ingredients,
                    steps: data.steps,
                }))
                // 해당 레시피가 내 레시피인지 확인 후 상태 변경
                data.member.id === myId ? setIsMyRecipe(true) : setIsMyRecipe(false);
            }
            catch (error) {
                console.log(error);
            }
        }
        else {
            try {
                const { data } = await axios.get(`/api/recipes/${id}`)
                dispatch(loadRecipe({
                    recipeId: data.id,
                    memberName: data.member.name,
                    memberId: data.member.id,
                    profileImagePath: data.member.profileImagePath,
                    createdAt: data.createdAt,
                    heartCounts: data.heartCounts,
                    heartExist: data.heartExist,
                    view: data.view,
                    title: data.title,
                    portion: data.portion,
                    time: data.time,
                    mainImage: data.imageInfo.imagePath,
                    ingredients: data.ingredients,
                    steps: data.steps,
                }))
                // 해당 레시피가 내 레시피인지 확인 후 상태 변경
                data.member.id === myId ? setIsMyRecipe(true) : setIsMyRecipe(false);
            }
            catch (error) {
                console.log(error);
            }
        }
        
    }

    useEffect(() => {
        getRecipe();
    }, [id])


    const confirm = (id) => {console.log("삭제 했습니다"); handleDelete(id)};
    const cancel = () => console.log("취소");

    // 댓글을 지울 때 (댓글 id 전달 필수)
    const handleDelete = (id) => {
        axios({
            method: `delete`,
            url: `/api/recipes/${id}`,
        })
        .then((response) => {
            console.log(response);
            // alert창 대체
            dispatch(setNoticeToast({ message: `레시피를 삭제했어요` }));
            navigate("/search");
        })
        .catch((error) => {
            // 예외 처리
            console.log(error.response);
            // alert창 대체
            dispatch(setWarningToast({ message: `레시피 삭제에 실패했습니다ㅠㅠ` }));
        })
    }

    return (
        <>
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
                                <LikeHeart
                                    heartCounts={recipe.heartCounts}
                                    heartExist={recipe.heartExist}
                                    idx={recipe.id}
                                />
                                <View>조회수 {recipe.view}</View>
                            </LikeViewWrapper>
                        </HeadLeftTop>
                        <HeadLeftBottom>
                            <Info>
                                <UserName image={recipe.member.profileImagePath} name={recipe.member.name} className="large" />
                                <PortionAndTime>{recipe.portion} 인분</PortionAndTime>
                                <PortionAndTime>{recipe.time} 분 &nbsp;소요</PortionAndTime>
                            </Info>
                            <ButtonLikeWrapper>
                                <ButtonLike className={!isMyRecipe && "invisible"}>
                                    <Link to="/recipes/edit">수정</Link>
                                </ButtonLike>
                                <ButtonLike
                                    onClick={useConfirm("정말 삭제하시겠습니까?", confirm, cancel, id)}
                                    className={!isMyRecipe && "invisible"}
                                >
                                    삭제
                                </ButtonLike>
                            </ButtonLikeWrapper>
                        </HeadLeftBottom>
                    </HeadLeft>
                    <Image src={recipe.imageInfo.imagePath} alt={'main'} />
                </Head>
                <SubHeading>재료(계량)</SubHeading>
                <Ingredients>
                    {recipe.ingredients.map((item, idx) => {
                        return (
                            <div key={idx}>
                                <Ingredient className="darker">{item.name}</Ingredient>
                                <Ingredient>{item.quantity}</Ingredient>
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
                <Comments id={id} />
            </RecipeWrapper>
            <Footer />
        </>
    )
};

export default RecipeDetail;