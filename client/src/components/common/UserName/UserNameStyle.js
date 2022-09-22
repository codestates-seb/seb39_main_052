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
        > img {
            width: 24px;
            height: 24px;
        }
        font-size: 14px;
    }
`

export const Image = styled.img`
    width: 16px;
    height: 16px;
    border-radius: 50%;
`

export const Name = styled.div`
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 0 0 8px;
`