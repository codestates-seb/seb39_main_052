import styled from 'styled-components'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

export const TextArea = styled.textarea`
    flex-grow: 1;
    padding: 2px 8px 0 8px;
    height: 36px;
    border-radius: 6px;
    margin: 0 8px 0 6px;
    border: 1px solid var(--primary-500);
    display: flex;
    resize: none;
    @media ${({ theme }) => theme.device.mobile} {
        height: 68px;
    }
`

export const CommentWrapper = styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    margin: 0 0 0 4px;
    padding: 6px 0 2px 0;
    font-size: 13px;
    :not(:last-of-type) {
        border-bottom: 1px solid var(--gray-200);
    }
    &.editMode {
        border-bottom: none;
    }
`

export const Comment = styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    margin: 0 0 0 8px;
    width: 58%;
    height: 36px;
    @media ${({ theme }) => theme.device.mobile} {
        font-size: 12px;
        padding: 4px 2px 4px 0;
        height: 80px;
    }
`

export const Time = styled.div`
    font-size: 12px;
    width: 56px;
    display: flex;
    justify-content: center;
    color: var(--fridge-800);
    @media ${({ theme }) => theme.device.mobile} {
        font-size: 10px;
    }
`

export const ButtonLikeWrapper = styled.div`
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    /* justify-content: flex-end; */
    width: 28px;
    > div:first-of-type {
        margin-bottom: 4px;
    }
`

export const ButtonLike = styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    color: var(--gray-600);
    cursor: pointer;
    
    &:hover {
        color: var(--black);
        text-decoration: underline;
    }

    &.invisible {
        display: none;
    }

    @media ${({ theme }) => theme.device.mobile} {
        font-size: 10px;
        margin-left: 2px;
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