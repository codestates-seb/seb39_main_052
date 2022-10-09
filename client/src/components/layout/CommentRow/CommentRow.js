import { useState, useEffect } from "react"
import { useSelector, useDispatch } from "react-redux";
import { CommentWrapper, Input, Comment, ButtonLikeWrapper, ButtonLike, StyledFontAwesomeIcon, Time, TextArea } from "./CommentRowStyle";
import UserName from "../../common/UserName/UserName";
import GeneralButton from "../../common/Button/GeneralButton";
import useConfirm from "../../../hooks/useConfirm";
import { faXmark, faCheck } from "@fortawesome/free-solid-svg-icons";
import axios from "axios";
import { setWarningToast, setNoticeToast } from "../../../features/toastSlice";

const CommentRow = ({ comment, setIsUpdated, page }) => {
    const [isEditable, setIsEditable] = useState(false);
    const [editedComment, setEditedComment] = useState("");

    const dispatch = useDispatch();

    // 댓글 수정 모드에서 페이지 변경 시 수정 모드 끄기
    useEffect(() => {
        setIsEditable(false);
    }, [page])

    // 로그인 시 리덕스에 저장한 내 아이디
    const myId = useSelector((state) => {
        return state.user.userId;
    })

    // 시간을 ago 기준으로 표시하는 함수
    function timeSince (createdAt) {
        const date = Date.parse(createdAt);
    
        let seconds = Math.floor((new Date() - date) / 1000);
    
        let interval = seconds / 31536000;
    
        if (interval > 1) {
            return Math.floor(interval) + "년 전";
        }
        interval = seconds / 2592000;
        if (interval > 1) {
            return Math.floor(interval) + "달 전";
        }
        interval = seconds / 86400;
        if (interval > 1) {
            return Math.floor(interval) + "일 전";
        }
        interval = seconds / 3600;
        if (interval > 1) {
            return Math.floor(interval) + "시간 전";
        }
        interval = seconds / 60;
        if (interval > 1) {
            return Math.floor(interval) + "분 전";
        }
        return "방금";
    };


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
            // alert 창 대체
            dispatch(setNoticeToast({ message: `댓글을 삭제했어요 :)` }))
            setIsUpdated(true);
        })
        .catch((error) => {
            // 예외 처리
            console.log(error.response);
            // alert 창 대체
            dispatch(setWarningToast({ message: `댓글 삭제에 실패했어요ㅠㅠ` }))
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
            // alert 창 대체
            dispatch(setNoticeToast({ message: `댓글을 수정했어요 :)` }))
            setIsEditable(false);
            setIsUpdated(true);
        })
        .catch((error) => {
            // 예외 처리
            console.log(error.response);
            // alert 창 대체
            dispatch(setWarningToast({ message: `댓글 수정에 실패했어요ㅠㅠ` }))
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
                // 작성글 보여주기
                <CommentWrapper>
                    <UserName
                        image={comment.member.profileImagePath}
                        name={comment.member.name}
                        className="short"
                    />
                    <Comment>{comment.content}</Comment>
                    <Time>{timeSince(comment.createdAt)}</Time>
                    {/* <Time>23시간 전</Time> */}
                    <EditAndDelete isMine={comment.member.id === myId}/>
                </CommentWrapper>
                :
                // 작성글을 input 창에 넣어 수정 가능하게 하기
                <CommentWrapper className="editMode">
                    <UserName
                        image={comment.member.profileImagePath}
                        name={comment.member.name}
                        className="short"
                    />
                    <TextArea 
                        type='text' 
                        maxLength='46'
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