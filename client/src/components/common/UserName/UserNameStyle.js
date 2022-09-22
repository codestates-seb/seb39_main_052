import styled from "styled-components";

export const Wrapper = styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    margin: 0 0 0 0;
    color: var(--fridge-800);
    font-size: 12px;
    
    &.large {
        font-size: 14px;
    }
`

export const Image = styled.img`
    width: 24px;
    height: 24px;
    border-radius: 50%;
    margin-bottom: 0px;
`

export const Name = styled.div`
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 0 0 8px;
`