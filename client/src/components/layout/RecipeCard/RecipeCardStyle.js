import styled from "styled-components";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

export const Container = styled.div`
    width: 172px;
    height: 250px;
    padding: 16px 0px 8px 0;
    margin: 0px 8px 24px 8px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    border-radius: 10px;
    background-color: var(--fridge-200);
    box-shadow: var(--shadow-low);
    border: 1px solid var(--fridge-400);
`

export const Image = styled.img`
    width: 140px;
    height: 140px;
    object-fit: cover;
    border-radius: 10px;
    box-shadow: var(--shadow-low);
`

export const ContentWrapper = styled.div`
    width: 140px;
    /* background-color: var(--white); */
    padding: 5px 0 0 0;
    margin: 8px 0 0 0;
    border-top: 1px solid var(--fridge-500);
`

export const Title = styled.div`
    width: 150px;
    font-size: 13px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    margin: 0 0 10px 0;
`

export const Doornob = styled.div`
    display: flex;
    justify-content: space-between;
    align-items: flex-end;
`

export const StyledFontAwesomeIcon = styled(FontAwesomeIcon)`
    color: var(--fridge-600);
    cursor: pointer;
    :active {
        transform: rotate(135deg);
    }
`

export const LikesAndViews = styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    margin-top: 10px;
    padding: 0 0 0 1px;
    // 조회수
    > div:nth-child(2) {
        font-size: 11px;
        color: var(--fridge-800);
    }
`