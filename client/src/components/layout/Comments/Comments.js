import axios from "axios";
import { Comment, CommentRow, Container, Input, InputWrapper, StyledFontAwesomeIcon, Button, ButtonLike, ButtonLikeWrapper } from "./CommentsStyle";
import { faXmark, faCheck } from "@fortawesome/free-solid-svg-icons";
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
        content: `정말 맛있어 보여요! 정말 맛있어 보여요!`,
    }

    const handleChange = (e) => {
        setComment(e.target.value);
    }

    const confirm = (index) => {console.log("삭제 했습니다"); handleDelete(index)};
    const cancel = () => console.log("취소");

    const handleDelete = (index) => {
        console.log(index);
        // axios({
        //     method: `delete`,
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

    const handleEdit = () => {
        // axios({
        //     method: `patch`,
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
                <Input
                    placeholder="댓글을 입력하세요"
                    onChange={handleChange}
                    type='text'
                    maxLength='28'
                    className="large"
                />
                <GeneralButton className={`small`} onClick={handleSubmit}>등록</GeneralButton>
            </InputWrapper>
            <CommentRow>
                <UserName image={dummy.profile} name={dummy.username} className="bold"></UserName>
                <Comment>{dummy.content}</Comment>
                <ButtonLikeWrapper>
                    <ButtonLike>수정</ButtonLike>
                    <ButtonLike onClick={useConfirm("정말 삭제하시겠습니까?", confirm, cancel, 'yo')}>삭제</ButtonLike>
                    {/* <Button onClick={useConfirm("정말 삭제하시겠습니까?", confirm, cancel, 'yo')}>
                        <StyledFontAwesomeIcon icon={faXmark} />
                    </Button> */}
                </ButtonLikeWrapper>
            </CommentRow>
            <CommentRow>
                <UserName image={dummy.profile} name={dummy.username} className="bold"></UserName>
                <Input className="small" type='text' maxLength='28' onChange={handleChange}/>
                <GeneralButton className={`xsmall gray`}><StyledFontAwesomeIcon icon={faXmark} /></GeneralButton>
                <GeneralButton className={`xsmall`} margin={"0 0 0 6px"}>
                    <StyledFontAwesomeIcon icon={faCheck} />
                </GeneralButton>
            </CommentRow>
        </Container>
    )
};

export default Comments;