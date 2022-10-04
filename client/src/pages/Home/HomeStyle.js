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

export const EmptyBox = styled.div `
    width: 800px;
    height: 210px;
    border-radius: 10px;
    background-color: var(--gray-100);
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    color: var(--gray-700);
    font-size: 16px;
    box-shadow: var(--shadow-low);
    > div > span {
        font-size: 16px;
        :hover {
            color: var(--fridge-900);
            text-decoration: underline;
        }
    }
    > p {
        cursor: pointer;
        font-size: 14px;
        margin: 16px 0 0 0 ;
        padding: 4px 8px;
        border-radius: 4px;
        background-color: var(--fridge-500);
        color: var(--fridge-100);
        :hover {
            background-color: var(--fridge-600);
        }
        :active {
            box-shadow: var(--shadow-low);
        }
    }
`