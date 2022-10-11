import styled from "styled-components";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

export const LikeWrapper = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    font-size: 0.625rem;
    @media ${({ theme }) => theme.device.mobile} {
        font-size: 11px;
        flex-direction: row-reverse;
        height: 30px;
        align-items: flex-end;
        color: var(--fridge-800);
    }
`

export const StyledFontAwesomeIcon = styled(FontAwesomeIcon)`
    color: var(--red-500);
    font-size: 16px;
    margin: 0 4px 0 0;
    cursor: pointer;
    &:active {
        font-size: 14px;
    }
    &:hover {
        color: var(--red-400);
    }
    @media ${({ theme }) => theme.device.mobile} {
        font-size: 28px;
        margin: 0 0 0 4px;
        &:active {
            font-size: 27px;
        }
    }
`;