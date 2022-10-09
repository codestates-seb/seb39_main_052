import styled from "styled-components";

export const RecipeWrapper = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    margin: 40px 0 0 0;
    min-height: 100%;
    @media ${({ theme }) => theme.device.mobile} {
        
    }
`
export const Extra = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    font-size: 11px;
    width: 548px;
    @media ${({ theme }) => theme.device.mobile} {
        width: 360px;
    }
`

export const RecipeId = styled.div`
    color: var(--gray-800);
`

export const Head = styled.div`
    display: flex;
    flex-direction: row;
    width: 548px;
    justify-content: space-between;
    @media ${({ theme }) => theme.device.mobile} {
        width: 100vw;
        align-items: center;
        flex-direction: column-reverse;
    }
`

export const HeadLeft = styled.div`
    display: flex;
    flex-direction: column;
    flex-grow: 1;
    padding: 0 24px 0 0;
    @media ${({ theme }) => theme.device.mobile} {
        align-items: center;
        justify-content: center;
        padding-right: 0;
        margin-top: 16px;
    }
`

export const HeadLeftTop = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin: 0 0 8px 0;
    height: 40px;
    @media ${({ theme }) => theme.device.mobile} {
        flex-direction: column;
        align-items: center;
    }
`

export const Heading = styled.h1`
    margin: 0 0 0 0;
    height: 40px;
    width: 270px;
    overflow: wrap;
    font-size: 18px;
    /* white-space: nowrap;
    text-overflow: ellipsis; */
    @media ${({ theme }) => theme.device.mobile} {
        text-align: center;
        width: 360px;
        font-size: 16px;
    }
`

export const LikeViewWrapper = styled.div`
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    justify-content: flex-start;
    @media ${({ theme }) => theme.device.mobile} {
        flex-direction: row-reverse;
        justify-content: space-between;
        align-items: flex-end;
        width: 300px;
        margin-top: 8px;
    }   
`

export const View = styled.div`
    font-size: 10px;
    margin-top: 4px;
`

export const HeadLeftBottom = styled.div`
    display: flex;
    flex-direction: row;
    font-size: 12px;
    justify-content: space-between;
    flex-grow: 1;
    @media ${({ theme }) => theme.device.mobile} {
        flex-direction: column;
    }
`

export const Info = styled.div`
    display: flex;
    flex-direction: column;
    // 유저 프로필
    > *:nth-child(1) {
        margin: 0 0 24px 0;
        @media ${({ theme }) => theme.device.mobile} {
            margin-bottom: 0;
            flex-grow: 1;
        }
    }
    > *:not(:first-child) {
        @media ${({ theme }) => theme.device.mobile} {
            flex-grow: 0;
        }
    } 
    @media ${({ theme }) => theme.device.mobile} {
        width: 300px;
        margin-top: 18px;
        flex-direction: row;
        justify-content: space-around;
        align-items: center;
    }
`

export const PortionAndTime = styled.div`
    min-width: 60px;
    max-width: 110px;
    height: 24px;
    border-radius: 10px;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: var(--fridge-200);
    color: var(--fridge-800);
    box-shadow: var(--shadow-low);
    margin-top: 8px;
    @media ${({ theme }) => theme.device.mobile} {
        margin-top: 0;
        font-size: 10px;
        width: 10px;
        margin-left: 4px;
    }
`

export const ButtonLikeWrapper = styled.div`
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    justify-content: flex-start;
    padding: 16px 0 0px 0;
    @media ${({ theme }) => theme.device.mobile} {
        flex-direction: row;
        align-items: center;
        justify-content: flex-start;
    }
`

export const ButtonLike = styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    margin: 0 0px 8px 0px;
    color: var(--gray-600);
    cursor: pointer;
    &.dark {
        color: var(--gray-800);
        border-bottom: 1px solid var(--gray-800);
    }
    &:hover {
        color: var(--black);
        /* text-decoration: underline; */
        border-bottom: 1px solid var(--black);
        background-color: var(--gray-100);
    }
    &.invisible {
        display: none;
    }
    @media ${({ theme }) => theme.device.mobile} {
        margin-right: 8px;
    }
`

export const Image = styled.img`
    height: 12.5rem;
    width: 12.5rem;
    object-fit: cover;
    border-radius: 10px;
    box-shadow: var(--shadow-low);
`

export const SubHeading = styled.h2`
    width: 548px;
    color: var(--gray-900);
    // 재료 계량
    :first-of-type {
        @media ${({ theme }) => theme.device.mobile} {
            margin-top: 24px;
        }
    }
    @media ${({ theme }) => theme.device.mobile} {
        width: 100%;
        text-align: center;
        font-size: 14px;
    }
`
export const Ingredients = styled.div`
    display: flex;    
    width: 548px;
    font-size: 12px;
    color: var(--gray-900);
    margin: 0 0 32px 0;
    flex-direction: column;
    > div {
        display: flex;
        margin: 4px 0;
    }
    @media ${({ theme }) => theme.device.mobile} {
        width: 100%;
        align-items: center;
    }
`

export const Ingredient = styled.div`
    width: 100px;
    margin: 0 8px 0 0;
    @media ${({ theme }) => theme.device.mobile} {
        background-color: var(--gray-100);
        padding: 2px 4px;
        border-radius: 4px;
        text-align: center;
    }
    &.darker {
        @media ${({ theme }) => theme.device.mobile} {
            background-color: var(--fridge-100);
        }
    }
`