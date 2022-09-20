import styled from "styled-components";

export const Container = styled.div`
    display: flex;
    flex-direction: column;
    background-color: var(--gray-300);
    width: 40vw;
    height: 30vh;
    margin: auto;
    padding: 16px 20px;
`

export const InputWrapper = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin: 0 0 12px 0;
`

export const Input = styled.input`
    height: 40px;
    flex-grow: 1;
    border-radius: 8px;
    padding: 0 8px;
    font-size: 16px;
    margin: 0 8px 0 0;
`

export const CommentRow = styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
`

export const Comment = styled.div`
    margin: 0 0 0 20px;
`