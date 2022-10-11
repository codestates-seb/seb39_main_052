import styled from "styled-components";

export const Container = styled.div`
    display: flex;
    width: 548px;
    margin: 10px 0;
    color: var(--gray-900);
    @media ${({ theme }) => theme.device.mobile} {
        width: 100vw;
        padding: 0 24px;
    }
`

export const Image = styled.img`
    width: 160px;
    height: 160px;
    object-fit: cover;
    border-radius: 10px;
    box-shadow: var(--shadow-low);
    @media ${({ theme }) => theme.device.mobile} {
        width: 100px;
        height: 100px;
    }
`

export const Content = styled.div`
    flex-grow: 1;
    padding: 4px 0 0 32px;
    font-size: 12px;
    @media ${({ theme }) => theme.device.mobile} {
        padding-left: 16px;
    }
`