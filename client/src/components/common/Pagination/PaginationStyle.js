import styled from "styled-components";

export const Wrapper = styled.nav`
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 5px;
    margin: 16px;
`
export const Pages = styled.nav`
`

export const Button = styled.button`
    padding: 8px;
    background: none;
    font-size: 12px;
    color: var(--fridge-800);
    border: none;
    border-radius: 3px;

    &:hover {
        background: #c9c9c9;
        cursor: pointer;
    }

    &:disabled {
        cursor: revert;
    }

    &.none {
        display: none;
    }
    
    &[aria-current] {
        background: var(--fridge-500);
        color: var(--white);
        font-weight: bold;
    }
`