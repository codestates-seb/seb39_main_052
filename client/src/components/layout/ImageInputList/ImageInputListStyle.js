import styled from "styled-components";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

export const Block = styled.div`
    background-color: var(--primary-300);
    display: flex;
    justify-content: flex-start;
    align-items: center;
    padding: 4px;
    border-radius: 10px;
    width: 548px;
    margin-bottom: 12px;
    @media ${({ theme }) => theme.device.mobile} {
        width: 360px;
        height: 120px;
    }
`;

export const Order = styled.label`
    display: flex;
    justify-content: center;
    width: 30px;
    margin-right: 4px;
    @media ${({ theme }) => theme.device.mobile} {
        font-size: 10px;
        width: 20px;
    }
`

export const Input = styled.textarea`
    // className에 사이즈 설정하면 input 크기 지정 가능
    height: 140px;
    border-radius: 10px;
    margin: 0 4px 0 12px;
    padding: 12px;
    flex-grow: 1;
    @media ${({ theme }) => theme.device.mobile} {
        flex-grow: 0;
        width: 200px;
        height: 101px;
    }  
`;

export const ButtonWrapper = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    height: 160px;
    @media ${({ theme }) => theme.device.mobile} {
        height: 90px;
        width: 16px;
        align-items: center;
    }
`;

export const Button = styled.button`
    background-color: var(--primary-300);
    @media ${({ theme }) => theme.device.mobile} {
        width: 14px;
        display: flex;
        align-items: center;
        justify-content: center;
    }
`

export const StyledFontAwesomeIcon = styled(FontAwesomeIcon)`
    &.delete {
        color: var(--red-600);
    }
    &.add {
        color: var(--green-900);
    }
`;