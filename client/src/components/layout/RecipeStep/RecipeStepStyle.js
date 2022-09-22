import styled from "styled-components";

export const Container = styled.div`
    display: flex;
    width: 548px;
    margin: 10px 0;
    color: var(--gray-900);
`

export const Image = styled.img`
    width: 160px;
    border-radius: 10px;
    box-shadow: var(--shadow-low);
`

export const Content = styled.div`
    flex-grow: 1;
    padding: 4px 0 0 32px;
    font-size: 12px;
`