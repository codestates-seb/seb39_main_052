import styled, {keyframes} from 'styled-components';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const Container = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    min-height: 80vh;
    @media ${({ theme }) => theme.device.mobile} {
        width: 100vw;
    }
`

export const SubHead = styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    @media ${({ theme }) => theme.device.mobile} {
        width: 100vw;
        height: 60px;
        flex-direction: column;
        justify-content: space-around;
        flex-wrap: wrap;
    }
`

export const Help = styled.div`
    cursor: pointer;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
    margin-bottom: 8px;
    position: absolute;
    > span {
        font-size: 14px;
        color: var(--gray-800);
    }
    > .modal {
        z-index: 99;
    }
    &:hover {
        > *:not(.modal) {
            color: var(--fridge-900);
        }
    }
    :not(:hover) {
        > .modal {
            display: none;
        }
    }
    @media ${({ theme }) => theme.device.mobile} {
        width: 100vw;
        position: static;
        // 모바일에선 도움말 창 떴을 땐 도움말 글씨 사라지기
        :hover {
            > :not(.modal) {
                display: none;
            }
        }
    }
`

export const StyledQuestionMark = styled(FontAwesomeIcon)`
    font-size: 16px;
    color: var(--gray-600);
    margin-right: 4px;
`

const fadeIn = keyframes`
    from {
        opacity: 0;
    }
    to {
        opacity: 0.8;
    }
`

export const HelpMessage = styled.div`
    animation: ${fadeIn} 0.5s ease-in-out;
    background-color: var(--white);
    opacity: 0.8;
    border: 1px solid var(--gray-300);
    font-size: 14px;
    position: relative;
    top: 115px;
    left: -64px;
    padding: 16px 24px;
    border-radius: 16px;
    color: var(--gray-900);
    > ul {
        margin: 0;
    }
    > ul > li {
        list-style-position: inside;
        list-style-type: circle;
        :not(:last-of-type) {
            margin-bottom: 8px;
        }
    }
    > ul > li > ul > li {
        margin-left: 16px;
        font-size: 12px;
        :first-of-type {
            margin-top: 4px;
        }
        @media ${({ theme }) => theme.device.mobile} {
            font-size: 10px;
        }
    }
    @media ${({ theme }) => theme.device.mobile} {
        top: 108px;
        left: 0px;
        font-size: 12px;
        width: 360px;
    }
`

const BlinkAnimation = keyframes`
  0% { color: var(--primary-600) }
  50% { color: var(--red-300) }
  100% { color: var(--primary-600) }
`;

export const StyledBulletPoint = styled(FontAwesomeIcon)`
    &.green {
        color: var(--green-600);
        box-shadow: var(--shadow-low);
    }
    &.yellow {
        color: var(--primary-600);
    }
    &.red {
        color: var(--red-300);
    }
    &.blink {
        animation: ${BlinkAnimation} 2s linear infinite;
    }
`

export const SortWrapper = styled.div`
    display: flex;
    justify-content: flex-end;
    align-items: center;
    color: var(--fridge-700);
    font-size: 13px;
    margin-bottom: 8px;
    width: 800px;
    @media ${({ theme }) => theme.device.mobile} {
        width: 100vw;
        justify-content: center;
    }
`

export const Option = styled.div`
    padding: 0 8px;
    cursor: pointer;
    // 옵션 사이 줄
    :not(:last-of-type) {
        border-right: 1px solid var(--fridge-800);
    }
    // 선택된 옵션 볼드 효과 
    &.selected {
        font-weight: 700;
        color: var(--fridge-800);
    }
`

export const Fridge = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 890px;
    padding: 14px 14px 14px 14px;
    border-radius: 16px;
    background-color: var(--fridge-200);
    box-shadow: var(--shadow-medium);
    @media ${({ theme }) => theme.device.mobile} {
        width: 95vw;
        overflow: scroll;
        align-items: flex-start;
    }  
`

export const InnerContainer = styled.div`
    padding: 16px 0 14px 12px;
    border-radius: 16px;
    display: flex;
    flex-direction: column;
    align-items: center;
    background-color: var(--fridge-100);
`

export const Title = styled.h1`
    margin: 24px 0 16px 0;
    @media ${({ theme }) => theme.device.mobile} {
        margin-bottom: 8px;
    }
`

export const ColumnHeads = styled.div`
    display: flex;
    width: 100%;
    justify-content: space-around;
    padding: 0 40px 0 14px;
    margin: 0;
`

export const Head = styled.div`
    width: 150px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    color: var(--white);
    /* font-weight: 900; */
    /* border: 1px solid red; */
    background-color: var(--fridge-500);
    border-radius: 4px;
    box-shadow: var(--shadow-low) inset;
    /* box-shadow: var(--fridge-600) 3px 3px 6px 0px inset, var(--fridge-300) -1px -1px 6px 1px inset; */
`

export const InputWrapper = styled.div`
    height: 360px;
    padding: 0 2px 0 0;
    margin: 8px 0 0 4px;
    overflow-y: scroll;
    ::-webkit-scrollbar {
        width: 8px;
        background-color: var(--fridge-100);
    }
    ::-webkit-scrollbar-thumb {
        border-radius: 10px;
        background-color: var(--fridge-500);
    }
`

export const ButtonWrap = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    width: 480px;
    margin: 0px 0 15vh 0;
    @media ${({ theme }) => theme.device.mobile} {
        width: 96vw;
        > * {
            font-size: 14px;
        }
    }
`