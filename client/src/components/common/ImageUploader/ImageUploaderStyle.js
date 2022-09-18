import styled from "styled-components";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

export const Container = styled.div`
    position: relative;
    &.big {
        height: 280px;
        width: 280px;
        border-radius: 10px;
    }
    /* &.mobile {
        height: 360px;
        width: 360px;
        border-radius: 10px;
    } */
    &.small {
        height: 160px;
        width: 160px;
        border-radius: 10px;
    }
    &.round {
        height: 280px;
        width: 280px;
        border-radius: 50%;
    }
`

export const Img = styled.img`
    object-fit: cover;
    height: 100%;
    width: 100%;
    border-radius: inherit;
`

export const Input = styled.input`
    display: none;
`

export const Button = styled.button`
    width: 100px;
    position: absolute;
    right: 0%;
    bottom: 0%;
`

export const StyledFontAwesomeIcon = styled(FontAwesomeIcon)`
    position: absolute;
    left: 36%;
    top: 36%;
    font-size: 100px;
    color: var(--mint-600);
`;