import styled from "styled-components";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

export const Container = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
`

export const Heading = styled.h1`
    margin-top: 16px;
`

export const Option = styled.div`
    width: 736px;
    display: flex;
    justify-content: flex-end;
    margin: 0 0 16px 0;
`

export const RecipeWrapper = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: center;
    width: 760px;
    flex-wrap: wrap;
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