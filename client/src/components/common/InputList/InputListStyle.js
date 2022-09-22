import styled from "styled-components";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

export const Block = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 5px 0 5px 0;
`;

export const Order = styled.label`
    display: flex;
    justify-content: center;
    width: 30px;
`

export const Input = styled.input`
    margin: 0 10px 0 0;
    border-radius: 5px;
    // className에 사이즈 설정하면 input 크기 지정 가능
     &.small {
        height: 40px;
        width: 150px;
        padding: 1px 10px 0 10px;
        font-size: 12px;
     }
     &.medium {
        height: 80px;
        width: 300px;
        padding: 4px 10px 0 10px;
     }
     &.large {
        height: 120px;
        width: 300px;
        padding:4px 10px 0 10px;
     }
`;

export const ButtonWrapper = styled.div`
    display: flex;
    flex-direction: column;
`;

export const Button = styled.button`
    background-color: var(--white);
    
`

export const StyledFontAwesomeIcon = styled(FontAwesomeIcon)`
    &.delete {
        color: var(--red-600);
    }
    &.add {
        color: var(--green-900);
    }
`;