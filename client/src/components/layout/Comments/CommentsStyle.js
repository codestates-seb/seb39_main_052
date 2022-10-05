import styled from "styled-components";

export const Container = styled.div`
    display: flex;
    flex-direction: column;
    /* background-color: var(--mintgreen-400); */
    background-color: var(--gray-100);
    width: 548px;
    max-height: 610px;
    margin: 54px 0 32px 0;
    padding: 20px 20px;
    border-radius: 10px;
    font-size: 12px;
    > h2 {
        margin-left: 6px;
        margin-bottom: 12px;
        @media ${({ theme }) => theme.device.mobile} {
            font-size: 14px;
        }
    }
    @media ${({ theme }) => theme.device.mobile} {
        width: 360px;
        max-height: fit-content;
    }
`

export const InputWrapper = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin: 0 0 20px 0;
    @media ${({ theme }) => theme.device.mobile} {
        margin-bottom: 4px;
    }
`

export const Input = styled.input`
    flex-grow: 1;
    padding: 1px 8px 0 8px;
    margin: 0 8px 0 0;
    &.large {
        height: 36px;
        border-radius: 8px;
    }
    &.small {
        height: 24px;
        border-radius: 6px;
        margin: 0 8px 0 12px;
    }
`