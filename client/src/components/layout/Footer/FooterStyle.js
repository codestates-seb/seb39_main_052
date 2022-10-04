import styled from 'styled-components'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

export const Container = styled.div`
    position: relative;
    z-index: 999;
    margin-top: 48px;   
    width: 100vw;
    height: 120px;
    background-color: var(--fridge-900);
    /* border-top: 5px dotted var(--gray-200); */
    /* border-top: 8px dotted var(--white); */
    border-top: 5px solid var(--fridge-500);
    display: flex;
    justify-content: space-between;
    padding: 28px 48px 28px  48px;
`

export const Left = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
`

export const LeftTop = styled.div`
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    height: 28px;
    > div {
        /* font-weight: bold; */
        font-size: 14px;
        display: flex;
        width: 126px;
        padding-bottom: 4px;
        color: var(--fridge-400);
        border-bottom: 1px solid var(--fridge-400);
        &.BE {
            /* color: var(--green-950);
            border-bottom: 1px solid var(--green-950); */
            margin-right: 20px;
        }
        &.FE {
            /* color: var(--blue-900);
            border-bottom: 1px solid var(--blue-900); */
        }
    }
`

export const LeftBottom = styled.div`
    display: flex;
    font-size: 14px;
    color: var(--fridge-400);
    > :not(div:last-of-type) {
        margin: 0 8px 0 0;
    }
    > div:hover {
        color: var(--fridge-200);
    }
    /* > div.BE {
        color: var(--green-900);
        &:hover {
            color: var(--green-950);
            font-weight: bold;
        }
    }
    > div.FE {
        color: var(--blue-800);
        &:hover {
            color: var(--blue-900);
            font-weight: bold;
        }
    } */
    // 김용주
    > div:nth-of-type(2) {
        margin-right: 20px;
    }
`

export const StyledFontAwesomeIcon = styled(FontAwesomeIcon)`
    margin: 0 0 0 4px;
`

export const Right = styled.div`
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    > :not(div:last-of-type) {
        font-size: 14px;
        color: var(--fridge-400);
        margin: 0 0 4px 0;
    }
    > div:last-of-type {
        color: var(--fridge-400);
        margin-top: 4px;
        font-weight: bold;
    }
`