import styled from "styled-components";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

export const Container = styled.div`
    display: flex;
    flex-direction: column;
    /* background-color: var(--primary-200); */
    background-color: var(--gray-200);
    width: 640px;
    height: 400px;
    margin: 60px 0 0 0;
    padding: 16px 20px;
    border-radius: 10px;
`

export const InputWrapper = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin: 0 0 20px 0;
`

export const Input = styled.input`
    flex-grow: 1;
    padding: 1px 8px 0 8px;
    margin: 0 8px 0 0;
    &.large {
        height: 42px;
        font-size: 16px;
        border-radius: 8px;
    }
    &.small {
        height: 24px;
        border-radius: 6px;
        margin: 0 8px 0 26px;
        font-size: 12px;
    }
`

export const CommentRow = styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    margin: 0 0 6px 4px;
    font-size: 13px;
`

export const Comment = styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    margin: 0 0 0 16px;
    width: 65%;
`

export const ButtonLikeWrapper = styled.div`
    display: flex;
    align-items: center;
    justify-content: center;
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