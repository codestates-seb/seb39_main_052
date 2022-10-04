import styled from "styled-components";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

export const Container = styled.div`
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
    /* color: var(--white); */
`

export const Alert = styled.div`
    width: 736px;
    height: 54vh;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 16px 0 0 0;
    color: var(--fridge-900);
    &.invisible {
        display: none;
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