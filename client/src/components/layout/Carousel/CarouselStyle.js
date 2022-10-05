import styled from 'styled-components';
import Slider from "react-slick";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

export const StyledSlider = styled(Slider)`
    // 커스텀 화살표
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
        right: -20px;
        top: 100px;
        cursor: pointer;
        // 기본 화살표 모양 없애기
        &::before {
            opacity: 0;
            display: none;
        }
    }
    // 슬라이드
    .slick-slide > * > * > * {
        margin: 0 auto;
        /* width: 180px; */
    }
    // 레시피 카드 하나
    .slick-slide > * > * {
        margin: auto;
        /* width: 180px; */
    }
    // 레시피 제목
    .slick-slide > * > * > div {
        display:flex;
        align-items:center;
        justify-content: center;
        /* width: 180px; */
    }
    // 슬라이드 전체
    .slick-list {
        width: ${(props) => props.width || "800px"};
        @media ${({ theme }) => theme.device.mobile} {
            width: 320px;
            margin: 0 0 0px 0px;
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
    max-width: 800px;
    margin: 0 0 0px 0px;
    /* overflow: hidden; */
    @media ${({ theme }) => theme.device.mobile} {
        width: 320px;
        margin: 0 0 0px 0px;
    }
`

export const RecipeWrapper = styled.div`
    display: flex;
    flex-direction: column;
    width: 180px;
    justify-content: center;
    align-items: center;
    @media ${({ theme }) => theme.device.mobile} {
        margin: 0 8px 0 0;
        width: 150px;
    }
`

export const Image = styled.img`
    width: 184px;
    height: 184px;
    object-fit: cover;
    border-radius: 10px;
    padding: 2px 2px;
    :hover {
        padding: 0 0;
    }
    @media ${({ theme }) => theme.device.mobile} {
        width: 150px;
        height: 150px;
        :hover {
            width: 150px;
            height: 150px;
        }
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