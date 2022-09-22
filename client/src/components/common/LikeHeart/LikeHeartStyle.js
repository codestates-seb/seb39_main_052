import styled from "styled-components";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

export const LikeWrapper = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    font-size: 0.625rem;
`

export const StyledFontAwesomeIcon = styled(FontAwesomeIcon)`
    color: var(--red-500);
    font-size: 16px;
    margin: 0 4px 0 0;
    cursor: pointer;
    &:active {
        font-size: 18px;
    }
`;