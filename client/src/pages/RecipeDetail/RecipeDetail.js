import Comments from "../../components/layout/Comments/Comments";
import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import axios from "axios";
import RecipeStep from "../../components/layout/RecipeStep/RecipeStep";
import { Heading, Ingredients, RecipeWrapper, SubHeading, Extra, Head, HeadLeft, Image, ButtonLike, HeadLeftTop, ButtonLikeWrapper, HeadLeftBottom, Info, PortionAndTime, RecipeId } from "./RecipeDetailStyle";
import UserName from "../../components/common/UserName/UserName";
import LikeHeart from "../../components/common/LikeHeart/LikeHeart";

const RecipeDetail = () => {
    const { id } = useParams();
    console.log(id);

    const dummyData = {
        id: 1,
        memberName: `아몬드봉봉`,
        memberId: 1,
        createdAt: null,
        title: "김치볶음밥 레시피",
        imagePath: "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
        portion: "1",
        time: "240",
        ingredients: [{
          sequence: 1,
          food: "",
            amount: ""
        }],
        steps: [{
            sequence: 1,
            content: "김치를 잘게 썬다",
            imagePath: "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
        },
        {
            sequence: 2,
            content: "김치를 기름에 볶는다",
            imagePath: "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
        },
        {
            sequence: 3,
            content: "밥을 넣는다",
            imagePath: "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
        }],
    }

    useEffect(()=> {
        console.log(`hi`)
        axios
            .get(`/api/recipes/${id}`)
            .then((response) => {
                console.log(response.status);
                console.log(response.data);
            })
            .catch((e) => console.log(e.response));
    }, [id])

    return (
        <RecipeWrapper>
            <Extra>
                <RecipeId>게시글 #{dummyData.id}</RecipeId>
                <ButtonLike className="dark">목록으로 돌아가기</ButtonLike>
            </Extra>
            <Head>
                <HeadLeft>
                    <HeadLeftTop>
                        <Heading>{dummyData.title}</Heading>
                        <LikeHeart />
                    </HeadLeftTop>
                    <HeadLeftBottom>
                        <Info>
                            <UserName image={dummyData.imagePath} name={dummyData.memberName} className="large"/>
                            <PortionAndTime>{dummyData.portion} 인분</PortionAndTime>
                            <PortionAndTime>{dummyData.time} 분 &nbsp;소요</PortionAndTime>
                        </Info>
                        <ButtonLikeWrapper>
                            <ButtonLike>수정</ButtonLike>
                            <ButtonLike>삭제</ButtonLike>
                        </ButtonLikeWrapper>
                    </HeadLeftBottom>
                </HeadLeft>
                <Image src={dummyData.imagePath} alt={'main'}/>
            </Head>
            <SubHeading>재료(계량)</SubHeading>
            <Ingredients>
                <div>쌀밥</div>
                <div>100g</div>
            </Ingredients>
            <SubHeading>요리 순서</SubHeading>
            {dummyData.steps.map((step, idx) => {
                return (
                    <RecipeStep
                        key={idx}
                        index={step.sequence}
                        image={step.imagePath}
                        content={step.content}
                    />
                )
            })}
            <Comments />
        </RecipeWrapper>
    )
};

export default RecipeDetail;