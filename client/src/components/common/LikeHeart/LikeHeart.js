import { useState, useEffect } from "react";
import { faHeart } from "@fortawesome/free-solid-svg-icons";
import { faHeart as faHeartLine} from "@fortawesome/free-regular-svg-icons";
import { LikeWrapper, StyledFontAwesomeIcon } from "./LikeHeartStyle";
import axios from "axios";

const LikeHeart = ({ heartCounts, idx, heartExists }) => {

    const [isClicked, setIsClicked] = useState(heartExists);
    const [newHeartCounts, setNewHeartCounts] = useState(heartCounts);

    const handleClick = async() => {
        try {
            const response = await axios.post(`/api/recipes/${idx}/heart`);
            console.log(response);
            setIsClicked(true)
        }
        catch (error) {
            console.log(error);
        }
    }

    return (
        <LikeWrapper>
            {isClicked
                ? <StyledFontAwesomeIcon icon={faHeart} onClick={() => setIsClicked(false)} />
                : <StyledFontAwesomeIcon icon={faHeartLine} onClick={handleClick} />
            }
            {/* ()괄호 */}
            &#40;{newHeartCounts}&#41;
        </LikeWrapper>
    )
};

export default LikeHeart;