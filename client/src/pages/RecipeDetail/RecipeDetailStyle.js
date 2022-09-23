import styled from "styled-components";

export const RecipeWrapper = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    margin: 40px 0 0 0;
`
export const Extra = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    font-size: 11px;
    width: 548px;
`

export const RecipeId = styled.div`
    color: var(--gray-800);
`

export const Head = styled.div`
    display: flex;
    flex-direction: row;
    width: 548px;
    justify-content: space-between;
`

export const HeadLeft = styled.div`
    display: flex;
    flex-direction: column;
    flex-grow: 1;
    padding: 0 24px 0 0;
`

export const HeadLeftTop = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin: 0 0 8px 0;
`

export const Heading = styled.h1`
    margin: 0 0 0 0
`

export const HeadLeftBottom = styled.div`
    display: flex;
    flex-direction: row;
    font-size: 12px;
    justify-content: space-between;
    flex-grow: 1;
`

export const Info = styled.div`
    display: flex;
    flex-direction: column;
    // 유저 프로필
    > *:nth-child(1) {
        margin: 0 0 24px 0;
    } 
`

export const PortionAndTime = styled.div`
    min-width: 60px;
    height: 24px;
    border-radius: 10px;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: var(--fridge-200);
    color: var(--fridge-800);
    box-shadow: var(--shadow-low);
    margin-top: 8px;
`

export const ButtonLikeWrapper = styled.div`
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    justify-content: flex-start;
    padding: 16px 0 0px 0;
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
    }
    &:hover {
        color: var(--black);
        /* text-decoration: underline; */
        border-bottom: 1px solid var(--black);
    }
`

export const Image = styled.img`
    height: 12.5rem;
    width: 12.5rem;
    border-radius: 10px;
    box-shadow: var(--shadow-low);
`

export const SubHeading = styled.h2`
    width: 548px;
`
export const Ingredients = styled.div`
    display: flex;    
    width: 548px;
    font-size: 12px;
    color: var(--gray-900);
    margin: 0 0 32px 0;
 > div {
    width: 100px;
    margin: 0 8px 0 0;
 }
`