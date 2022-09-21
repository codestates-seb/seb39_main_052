import axios from "axios";
import { Comment, CommentRow, Container, Input, InputWrapper } from "./CommentsStyle";
import blankImage from "../../../assets/blankImage.webp";
import UserName from "../../common/UserName/UserName";
import GeneralButton from "../../common/Button/GeneralButton";
import { useState } from "react";

const Comments = () => {
    const [comment, setComment] = useState("");
    const recipeId = 1;

    const dummy = {
        id: 1,
        username: `바닐라봉봉`,
        profile: blankImage,
        content: `맛있어보여요~`,
    }

    const handleChange = (e) => {
        setComment(e.target.value);
    }

    const handleSubmit = () => {
        // axios({
        //     method: `post`,
        //     url: `/api/recipes/${dummy.id}/comments`,
        //     headers: {
        //     },
        //     data: comment,
        // })
        // .then((response) => {
        //     console.log(response)
        // })
        // .catch((error) => {
        //     // 예외 처리
        //     console.log(error.response);
        // })
    }

    return (
        <Container>
            <h2>댓글</h2>
            <InputWrapper>
                <Input placeholder="댓글을 입력하세요" onChange={handleChange}></Input>
                <GeneralButton className={`small`} onClick={handleSubmit}>등록</GeneralButton>
            </InputWrapper>
            <CommentRow>
                <UserName image={dummy.profile} name={dummy.username} className="bold"></UserName>
                <Comment>{dummy.content}</Comment>
            </CommentRow>
        </Container>
    )
};

export default Comments;