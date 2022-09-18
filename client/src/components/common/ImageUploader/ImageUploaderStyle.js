import styled from "styled-components";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

export const Container = styled.div`
    height: 360px;
    width: 360px;
    position: relative;
`

export const Img = styled.img`
    border-radius: 10px;
    object-fit: cover;
    height: 100%;
    width: 100%;
`

export const Input = styled.input`
    display: none;
`

export const Button = styled.button`
    width: 100px;
    position: absolute;
    left: 72%;
    bottom: 0%;
`

export const StyledFontAwesomeIcon = styled(FontAwesomeIcon)`
    position: absolute;
    left: 36%;
    top: 36%;
    font-size: 100px;
    color: var(--mint-600);
`;