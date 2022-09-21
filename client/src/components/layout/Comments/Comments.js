import axios from "axios";
import { Comment, CommentRow, Container, Input, InputWrapper } from "./CommentsStyle";
import blankImage from "../../../assets/blankImage.webp";
import UserName from "../../common/UserName/UserName";
import GeneralButton from "../../common/Button/GeneralButton";
import useConfirm from "../../../hooks/useConfirm";
import { useState } from "react";

const Comments = () => {
    const [comment, setComment] = useState("");
    const recipeId = 1;

    const dummy = {
        id: 1,
        username: `바닐라봉봉`,
        profile: blankImage,
        content: `맛맛맛맛맛맛맛맛맛맛맛맛맛맛맛맛맛맛맛맛맛맛맛맛맛맛맛맛`,
    }

    const handleChange = (e) => {
        setComment(e.target.value);
    }

    console.log(`hi`)

    const confirm = () => console.log("Deleting the world...");
    const cancel = () => console.log("Aborted");
    const handleSubmit = useConfirm("정말 삭제하시겠습니까?", confirm, cancel);

    // const handleSubmit = () => {
    //     confirmation();
    //     // axios({
    //     //     method: `post`,
    //     //     url: `/api/recipes/${dummy.id}/comments`,
    //     //     headers: {
    //     //     },
    //     //     data: comment,
    //     // })
    //     // .then((response) => {
    //     //     console.log(response)
    //     // })
    //     // .catch((error) => {
    //     //     // 예외 처리
    //     //     console.log(error.response);
    //     // })
    // }

    return (
        <Container>
            <h2>댓글</h2>
            <InputWrapper>
                <Input
                    placeholder="댓글을 입력하세요"
                    onChange={handleChange}
                    type='text'
                    maxLength='28'
                />
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