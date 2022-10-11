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
    @media ${({ theme }) => theme.device.mobile} {
        flex-direction: row;
        width: 360px;
        height: 150px;
        justify-content: space-between;
        margin-bottom: 16px;
    }
    @media ${({ theme }) => theme.device.laptop} {
        :hover {
            width: 176px;
            height: 254px;
            padding: 8px 0px 0px 0;
            margin: 0px 6px 20px 6px;
            > * > img {
                width: 144px;
                height: 144px;
            }
        } 
    }
`

export const Image = styled.img`
    width: 140px;
    height: 140px;
    object-fit: cover;
    border-radius: 10px;
    box-shadow: var(--shadow-low);
    @media ${({ theme }) => theme.device.mobile} {
        margin: 16px;
        width: 120px;
        height: 120px;
    }
`

export const ContentWrapper = styled.div`
    width: 140px;
    /* background-color: var(--white); */
    padding: 5px 0 0 0;
    margin: 8px 0 0 0;
    border-top: 1px solid var(--fridge-500);
    @media ${({ theme }) => theme.device.mobile} {
        padding-top: 0;
        width: 190px;
        margin-right: 16px;
        margin-top: 0;
        border: none;
        display: flex;
        flex-direction: column;
        justify-content: center;
    }
`

export const Title = styled.div`
    width: 150px;
    font-size: 13px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    margin: 0 0 10px 0;
    @media ${({ theme }) => theme.device.mobile} {
        width: 199px;
    }
`

export const Doornob = styled.div`
    display: flex;
    justify-content: space-between;
    align-items: flex-end;
    @media ${({ theme }) => theme.device.mobile} {
        border-top: 1px solid var(--fridge-500);
        padding-top: 8px;
    }
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
        @media ${({ theme }) => theme.device.mobile} {
        }
    }
    @media ${({ theme }) => theme.device.mobile} {
        flex-direction: row-reverse;
        align-items: flex-end;
        margin-top: 16px;
        /* align-items: flex-end; */
    }
`