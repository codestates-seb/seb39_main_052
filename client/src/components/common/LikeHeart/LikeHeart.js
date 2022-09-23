import { useState, useEffect } from "react";
import { faHeart } from "@fortawesome/free-solid-svg-icons";
import { faHeart as faHeartLine} from "@fortawesome/free-regular-svg-icons";
import { LikeWrapper, StyledFontAwesomeIcon } from "./LikeHeartStyle";

const LikeHeart = ({ likes }) => {

    const [isClicked, setIsClicked] = useState(false);

    return (
        <LikeWrapper>
            {isClicked
                ? <StyledFontAwesomeIcon icon={faHeart} onClick={() => setIsClicked(false)} />
                : <StyledFontAwesomeIcon icon={faHeartLine} onClick={() => setIsClicked(true)} />
            }
            {/* ()괄호 */}

            &#40;{likes}&#41;
        </LikeWrapper>
    )
};

export default LikeHeart;