import styled from 'styled-components';
import Slider from "react-slick";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

// 커스텀 화살표
export const StyledSlider = styled(Slider)`
    .slick-arrow {
        display: flex;
        z-index: 10;
        width: 1vw;
        height: 1vw;
    }
    .slick-prev {
        left: -30px;
        top: 100px;
        cursor: pointer;
        // 기본 화살표 모양 없애기
        &::before {
            opacity: 0;
            display: none;
        }
    }

    .slick-next {
        right: -8px;
        top: 100px;
        cursor: pointer;
        // 기본 화살표 모양 없애기
        &::before {
            opacity: 0;
            display: none;
        }
    }
`

// 화살표 아이콘
export const StyledFontAwesomeIcon = styled(FontAwesomeIcon)`
    color: var(--fridge-700);
    font-size: 24px;
    :hover {
        color: var(--fridge-800);
    }
`

export const Container = styled.div`
    width: 800px;
    margin: 0 0 0px 24px;
    /* overflow: hidden; */
    @media ${({ theme }) => theme.device.mobile} {
        width: 320px;
        margin: 0 0 0px 8px;
    }
`

export const RecipeWrapper = styled.div`
    display: flex;
    flex-direction: column;
    width: 180px;
    justify-content: center;
    align-items: center;
    margin: 0 24px 0 0;
    @media ${({ theme }) => theme.device.mobile} {
        margin: 0 8px 0 0;
        width: 150px;
    }
`

export const Image = styled.img`
    width: 180px;
    height: 180px;
    object-fit: cover;
    border-radius: 10px;
    @media ${({ theme }) => theme.device.mobile} {
        width: 150px;
        height: 150px;
    }
`

export const Name = styled.div`
    width: 180px;
    height: 30px;
    font-size: 14px;
    color: var(--gray-900);
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    display: block;
    text-align: center;
    margin: 16px 0 0 0 ;
    @media ${({ theme }) => theme.device.mobile} {
        width: 150px;
        margin: 8px 0 0 0 ;
        font-size: 12px;
    }
`