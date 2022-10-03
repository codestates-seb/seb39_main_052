import styled from 'styled-components';

export const Container = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
`

export const Header = styled.h1 `
    margin: 32px 0 16px 0;
    color: var(--fridge-900);
    // 두번째 carousel 헤딩
    :last-of-type {
        @media ${({ theme }) => theme.device.mobile} {
        margin-top: 60px;
        }
    }
`