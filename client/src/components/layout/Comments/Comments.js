import axios from "axios";
import { Container, Input, InputWrapper } from "./CommentsStyle";
import GeneralButton from "../../common/Button/GeneralButton";
import Pagination from "../../common/Pagination/Pagination";
import { useEffect, useState } from "react";
import CommentRow from "../CommentRow/CommentRow";

const Comments = ({ id }) => {
    const [comment, setComment] = useState(""); // input에 내가 작성하는 댓글
    const [commentList, setCommentList] = useState([]);
    const [isUpdated, setIsUpdated] = useState(false); 

    const [limit, setLimit] = useState(10); // 한 페이지당 댓글 수
    const [page, setPage] = useState(1); // 페이지네이션으로 바뀔 현 페이지 위치
    const [total, setTotal] = useState(0); // 서버에서 받아올 전체 댓글 수
    const [totalPages, setTotalPages] = useState(0); // 서버에서 받아올 전체 댓글 수

    // 첫 화면 로딩 시, 댓글 업데이트시, 페이지 업데이트시 레시피 id에 따른 댓글 리스트 불러오기
    useEffect(() => {
        getCommentList();
        setIsUpdated(false);
    }, [id, isUpdated, page])

    const getCommentList = async() => {
        try {
            const { data } = await axios.get(`/api/recipes/${id}/comments?page=${page}`);
            // console.log(data.pageInfo);
            setTotal(data.pageInfo.totalElements);
            setTotalPages(data.pageInfo.totalPages);
            setCommentList([...data.data]);
        }
        catch (error) {
            console.log(error);
        }
    }

    // 엔터로 댓글 등록 가능하도록
    const handleEnter = (e) => {
        if (e.key === "Enter") {
            handleSubmit();
            // console.log("엔터")
        }
    }

    // 댓글을 작성할 때 (레시피 id 전달 필수)
    const handleSubmit = () => {
        axios({
            method: `post`,
            url: `/api/recipes/${id}/comments`,
            data: {content: comment},
        })
        .then((response) => {
            console.log(response);
            setComment("");
            alert(`댓글이 등록되었습니다.`)
            getCommentList();
        })
        .catch((error) => {
            // 예외 처리
            console.log(error.response);
        })
    }

    return (
        <Container>
            <h2>댓글 ({total})</h2>
            <InputWrapper>
                <Input
                    placeholder="댓글을 입력하세요"
                    onChange={(e) => setComment(e.target.value)}
                    onKeyPress={handleEnter}
                    type='text'
                    maxLength='28'
                    className="large"
                    value={comment}
                />
                <GeneralButton className={`small`} onClick={handleSubmit}>등록</GeneralButton>
            </InputWrapper>
            {commentList.map((comment, idx) => {
                return (
                    <CommentRow key={idx} comment={comment} setIsUpdated={setIsUpdated}/>
                )
            })}
            {/* <CommentRow>
                <UserName image={dummy.profile} name={dummy.username} className="bold"></UserName>
                <Input className="small" type='text' maxLength='28' />
                <GeneralButton className={`xsmall gray`}><StyledFontAwesomeIcon icon={faXmark} /></GeneralButton>
                <GeneralButton className={`xsmall`} margin={"0 0 0 6px"} onClick={() => handleEdit(13)}>
                    <StyledFontAwesomeIcon icon={faCheck} />
                </GeneralButton>
            </CommentRow> */}
            {total > 10 &&
                <Pagination
                    // total={total}
                    // limit={limit}
                    page={page}
                    setPage={setPage}
                    totalPages={totalPages}
                />
            }
        </Container>
    )
};

export default Comments;