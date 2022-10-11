import styled, { keyframes } from 'styled-components'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'


const slideIn = keyframes`
    from {
        transform: translateY(-300%);
    }
    to {
        transform: translateY(0%);
    }
`
  
const slideOut = keyframes`
    from {
        transform: translateY(0%);
    }
    to {
        transform: translateY(-300%);
    }
`

export const Container = styled.div`
    width: 100vw;
    position: fixed;  
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    top: 20%;
    z-index: 999;

    &.openAnimation {
        animation: ${slideIn} 0.5s ease-in-out 0s 1 normal forwards;
    }
  
    &.closeAnimation {
        animation: ${slideOut} 0.5s ease-in-out 0s 1 normal forwards;
    }
`

export const Box = styled.div`
    display: flex;
    justify-content: flex-start;
    align-items: center;
    padding: 8px 30px 8px 12px;
    font-size: 14px;
    background-color: ${(props) => props.bgColor || "var(--gray-100"};;
    border-radius: 10px;
    border: 1px solid var(--gray-100);
    box-shadow: var(--shadow-medium);
    opacity: 0.8;
    @media ${({ theme }) => theme.device.mobile} {
        padding: 4px 20px 4px 8px;
        font-size: 12px;
    }
`

export const Text = styled.p`
    margin: 0;
`

export const StyledFontAwesomeIcon = styled(FontAwesomeIcon)`
    margin-right: 16px;
    font-size: 24px;
    &.red {
        color: var(--red-600);
    }
    &.green {
        color: var(--green-800);
    }
    @media ${({ theme }) => theme.device.mobile} {
        font-size: 12px;
    }
`