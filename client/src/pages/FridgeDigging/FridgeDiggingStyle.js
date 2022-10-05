import styled from "styled-components";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

export const Container = styled.div`
    width: 100vw;
    display: flex;
    flex-direction: column;
    align-items: center;
`
export const SearchWrapper = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100vw;
    background-color: var(--primary-050);
    /* background-color: var(--white); */
    margin-bottom: 16px;
    box-shadow: var(--shadow-low);
`

export const Heading = styled.h1`
    margin-top: 16px;
    color: var(--fridge-900);
    > span {
        font-size: 22px;
    }
    /* color: var(--white); */
    @media ${({ theme }) => theme.device.mobile} {
        font-size: 16px;
        > span {
        font-size: 18px;
        }
    }
`

export const Alert = styled.div`
    width: 736px;
    height: 56vh;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 16px 0 0 0;
    color: var(--fridge-900);
    flex-wrap: wrap;
    > p {
        margin: 4px 0;
    }
    &.invisible {
        display: none;
    }
    @media ${({ theme }) => theme.device.mobile} {
        height: 50vh;
        font-size: 14px;
    }
`

export const Option = styled.div`
    width: 736px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 0 0 16px 0;
    &.invisible {
        display: none;
    }
    @media ${({ theme }) => theme.device.mobile} {
        width: 100vw;
        padding: 0 16px;
    }
`
export const ResultNum = styled.div`
    padding: 0 0 0 8px;
    font-size: 14px;
    color: var(--primary-900);
`

export const RecipeWrapper = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    width: 754px;
    min-height: 50vh;
    flex-wrap: wrap;
    &.invisible {
        display: none;
    }
    @media ${({ theme }) => theme.device.mobile} {
        width: 100vw;
        justify-content: center;
    }
`

export const Loader = styled.div`
    width: 760px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 0 16px 0;
`

export const StyledFontAwesomeIcon = styled(FontAwesomeIcon)`
    font-size: 24px;
    color: var(--primary-600);
    margin: 0 8px;
`