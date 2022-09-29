import styled from 'styled-components'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'


export const Input = styled.input`
    flex-grow: 1;
    padding: 0px 8px 0 8px;
    margin: 0 8px 0 0;
    &.large {
        height: 36px;
        border-radius: 8px;
    }
    &.small {
        height: 24px;
        border-radius: 6px;
        margin: 0 8px 0 6px;
        border: 1px solid var(--primary-500);
        padding-top: 1px;
    }
`

export const CommentWrapper = styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    margin: 0 0 8px 4px;
    font-size: 13px;
`

export const Comment = styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    margin: 0 0 0 16px;
    width: 65%;
    height: 24px;
`

export const ButtonLikeWrapper = styled.div`
    display: flex;
    align-items: center;
    justify-content: flex-end;
    width: 68px;
`

export const ButtonLike = styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    margin: 0 4px 0 4px;
    color: var(--gray-800);
    cursor: pointer;
    
    &:hover {
        color: var(--black)
    }

    &.invisible {
        display: none;
    }
`

export const Button = styled.button`
    background-color: var(--gray-200);
    margin: 0 0 0 10px;
    :hover {
        color: var(--primary-800);
    }
`

export const StyledFontAwesomeIcon = styled(FontAwesomeIcon)`


`