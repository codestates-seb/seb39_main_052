import { useState } from "react"
import { CommentWrapper, Input, Comment, ButtonLikeWrapper, ButtonLike, StyledFontAwesomeIcon } from "./CommentRowStyle";
import UserName from "../../common/UserName/UserName";
import GeneralButton from "../../common/Button/GeneralButton";
import useConfirm from "../../../hooks/useConfirm";
import { faXmark, faCheck } from "@fortawesome/free-solid-svg-icons";
import axios from "axios";
import { useCookies } from 'react-cookie';

const CommentRow = ({ comment, setIsUpdated }) => {
    const [isEditable, setIsEditable] = useState(false);
    const [editedComment, setEditedComment] = useState("");
    const [cookies, setCookie, removeCookie] = useCookies(['token', "id"]);

    const confirm = (id) => {console.log("삭제 했습니다"); handleDelete(id)};
    const cancel = () => console.log("취소");

    // 수정 버튼 누를 경우
    const handleEditButtonClick = () => {
        setIsEditable(true);
        setEditedComment(comment.content);
    }

    // 댓글을 지울 때 (댓글 id 전달 필수)
    const handleDelete = () => {
        axios({
            method: `delete`,
            url: `/api/comments/${comment.commentId}`,
        })
        .then((response) => {
            console.log(response);
            alert(`댓글이 삭제되었습니다`);
            setIsUpdated(true);
        })
        .catch((error) => {
            // 예외 처리
            console.log(error.response);
        })
    }

    // 엔터로 댓글 등록 가능하도록
    const handleEnter = (e) => {
        if (e.key === "Enter") {
            handleEdit();
        }
    }

    // 댓글을 수정할 때 (댓글 id 전달 필수)
    const handleEdit = (id) => {
        axios({
            method: `patch`,
            url: `/api/comments/${comment.commentId}`,
            data: { content: editedComment },
        })
        .then((response) => {
            console.log(response)
            alert(`댓글을 수정하였습니다.`)
            setIsEditable(false);
            setIsUpdated(true);
        })
        .catch((error) => {
            // 예외 처리
            console.log(error.response);
        })
    }

    // useConfirm을 map 안에서 바로 호출할 수 없어 수정 삭제를 별로 컴포넌트로 분리함
    const EditAndDelete = ({ isMine }) => {
        return(
            <ButtonLikeWrapper>
                <ButtonLike 
                    className={!isMine && "invisible"}
                    onClick={handleEditButtonClick}
                >
                    수정
                </ButtonLike>
                <ButtonLike 
                    onClick={useConfirm("정말 삭제하시겠습니까?", confirm, cancel, 13)}
                    className={!isMine && "invisible"}
                >
                    삭제
                </ButtonLike>
            </ButtonLikeWrapper>
        )
    } 

    return (
        <>
            {!isEditable
                ?
                <CommentWrapper>
                    <UserName
                        image={comment.member.profileImagePath}
                        name={comment.member.name}
                        className="bold"
                    />
                    <Comment>{comment.content}</Comment>
                    <EditAndDelete isMine={comment.member.id === Number(cookies.id)}/>
                </CommentWrapper>
                :
                <CommentWrapper>
                    <UserName
                        image={comment.member.profileImagePath}
                        name={comment.member.name}
                        className="bold"
                    />
                    <Input 
                        className="small" 
                        type='text' 
                        maxLength='28'
                        onChange={(e)=> {setEditedComment(e.target.value)}}
                        onKeyUp={handleEnter}
                        value={editedComment}
                    />
                    <GeneralButton className={`xsmall gray`} onClick={()=> setIsEditable(false)}>
                        <StyledFontAwesomeIcon icon={faXmark} />
                    </GeneralButton>
                    <GeneralButton className={`xsmall`} margin={"0 0 0 6px"} onClick={() => handleEdit(13)}>
                        <StyledFontAwesomeIcon icon={faCheck} />
                    </GeneralButton>
                </CommentWrapper>
            }
        </>
    )
}

export default CommentRow