import styled from "styled-components";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

export const Block = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
`;

export const Input = styled.input`
    // className에 사이즈 설정하면 input 크기 지정 가능
     &.small {
        height: 40px;
        color: var(--red-600);
     }
     &.medium {
        height: 80px;
     }
     &.large {
        height: 120px;
     }
`;

export const ButtonWrapper = styled.div`
    display: flex;
    flex-direction: column;
`;

export const Button = styled.button`
    
`

export const StyledFontAwesomeIcon = styled(FontAwesomeIcon)`
    color: red;
    &.delete {
        color: var(--red-600);
    }
    &.add {
        color: var(--green-900);
    }
`;