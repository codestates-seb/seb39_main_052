import styled from "styled-components";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

export const Block = styled.div`
    background-color: var(--mint-300);
    display: flex;
    justify-content: flex-start;
    align-items: center;
    padding: 12px;
    border-radius: 10px;
    width: 840px;
    margin-bottom: 12px;
`;

export const Order = styled.label`
    display: flex;
    justify-content: center;
    width: 30px;
    margin-right: 10px;
`

export const Input = styled.textarea`
    // className에 사이즈 설정하면 input 크기 지정 가능
    height: 160px;
    border-radius: 10px;
    margin: 0 10px 0 12px;
    padding: 12px;
    flex-grow: 1;
`;

export const ButtonWrapper = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    height: 160px;
`;

export const Button = styled.button`
    background-color: var(--mint-300);
`

export const StyledFontAwesomeIcon = styled(FontAwesomeIcon)`
    &.delete {
        color: var(--red-600);
    }
    &.add {
        color: var(--green-900);
    }
`;