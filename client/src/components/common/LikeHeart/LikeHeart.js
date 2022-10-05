import { useState, useEffect } from "react";
import { faHeart } from "@fortawesome/free-solid-svg-icons";
import { faHeart as faHeartLine} from "@fortawesome/free-regular-svg-icons";
import { LikeWrapper, StyledFontAwesomeIcon } from "./LikeHeartStyle";
import { useSelector, useDispatch } from "react-redux";
import axios from "axios";

const LikeHeart = ({ heartCounts, idx, heartExist }) => {
      //로그인 상태 가져와서 변수에 저장
    const isLoggedIn = useSelector((state) => {
        return state.user.isLoggedIn;
    });
    
    // redux에서 받아오는 heartExist로 하트 색 변화 반응 속도가 느려 useState 사용
    const [isClicked, setIsClicked] = useState(heartExist);
    const [newHeartCounts, setNewHeartCounts] = useState(heartCounts);

    // 서버에서 받아오는 값으로 로컬 상태 변경
    useEffect(() => {
        setIsClicked(heartExist);
        setNewHeartCounts(heartCounts);
    }, [heartExist, heartCounts])

    const handleClick = async() => {
        if (isLoggedIn) {
            try {
                const response = await axios.post(`/api/recipes/${idx}/heart`);
                console.log(response);
                setIsClicked(true); // 하트 색 변경
                setNewHeartCounts(newHeartCounts+1); // 하트 숫자 변경
            }
            catch (error) {
                console.log(error);
            }
        }
        else {
            alert(`로그인이 필요한 서비스입니다ㅠㅠ`)
        }
    }

    const handleDelete = async() => {
        try {
            const response = await axios.delete(`/api/recipes/${idx}/heart`);
            console.log(response);
            setIsClicked(false); // 하트 색 변경
            setNewHeartCounts(newHeartCounts-1); // 하트 숫자 변경
        }
        catch (error) {
            console.log(error);
        }
    }

    return (
        <LikeWrapper>
            {/* 로그인 한 경우 */}
            {isLoggedIn 
            ? (isClicked
                ? <StyledFontAwesomeIcon icon={faHeart} onClick={handleDelete} />
                : <StyledFontAwesomeIcon icon={faHeartLine} onClick={handleClick} />
            ) 
            : (
                // 로그인 안 한 경우
                <StyledFontAwesomeIcon icon={faHeartLine} onClick={handleClick} />
            )}
            {/* ()괄호 */}
            &#40;{newHeartCounts}&#41;
        </LikeWrapper>
    )
};

export default LikeHeart;