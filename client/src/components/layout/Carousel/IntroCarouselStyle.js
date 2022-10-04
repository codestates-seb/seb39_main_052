import styled from 'styled-components';
import Slider from "react-slick";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

export const Banner = styled.div`
    height: 260px;
    width: 100vw;
`

// carousel (react-slick) 관련
export const StyledSlider = styled(Slider)`
    /* // 마지막 페이지 > 첫페이지 전환시 깜박임 문제 해결
    .slick-slide,
    .slick-slider .slick-track,
    .slick-slider .slick-list
    {
    -webkit-transform: translate3d(0, 0, 0);
    -moz-transform: translate3d(0, 0, 0);
    -ms-transform: translate3d(0, 0, 0);
    -o-transform: translate3d(0, 0, 0);
    transform: translate3d(0, 0, 0);

    -webkit-transform: translateZ(0);
    -moz-transform: translateZ(0);
    -ms-transform: translateZ(0);
    -o-transform: translateZ(0);
    transform: translateZ(0);

    -webkit-perspective: 1000;
    -moz-perspective: 1000;
    -ms-perspective: 1000;
    perspective: 1000;
    -webkit-backface-visibility: hidden;
    -moz-backface-visibility: hidden;
    -ms-backface-visibility: hidden;
    backface-visibility: hidden;
    } */
    // 하단 dots
    .slick-dots {
        top: 88%;
    }
    // 커스텀 화살표
    .slick-arrow {
        display: flex;
        z-index: 10;
        width: 1vw;
        height: 1vw;
    }
    .slick-prev {
        /* left: 30px;
        top: 30px; */
        left: 40px;
        cursor: pointer;
        // 기본 화살표 모양 없애기
        &::before {
            opacity: 0;
            display: none;
        }
    }
    .slick-next {
        /* left: 30px;
        top: 30px; */
        right: 40px;
        cursor: pointer;
        // 기본 화살표 모양 없애기
        &::before {
            opacity: 1;
            display: none;
        }
    }
    // 슬라이드 내 css
    .slick-slide > div > div {
        width: 100px;
        display: flex;
        justify-content: center;
        align-items: center;
        padding-left: 24vw;
    }
    .slick-slide > div > div > div > img {
        position: relative;
        top: -8px;
        right: -200px;
        // 두번째 사진 (나의 냉장고 관련 배너)
        &.small {
            right: -274px;
        }
    }
`

export const Container = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100vw;
    height: 240px;
    overflow: hidden;
    box-shadow: var(--shadow-low);

    &.mint {
        background-color: var(--mintgreen-400);
    }
    &.pink {
        background-color: var(--red-050);
        > div > div {
            color: var(--red-800);
        }
    }
`

export const Left = styled.div`
    padding: 0 0 0 16px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    position: absolute;
    z-index: 2;
    margin: 48px 290px 0 0 ;
`

export const Text = styled.div`
    font-size: 24px;
    height: 92px;
    width: 340px;
    color: var(--mint-900);
    font-weight: bold;
    > p {
        margin: 8px 0;
        background-color: var(--mintgreen-400);
        border-radius: 10px;
        width: fit-content;
        > span {
            /* color: var(--mint-900); */
            font-size: 26px;
        }
    }
    &.pink {
        > p {
            background-color: var(--red-050);
        }
    }
`

export const Button = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    background-color: var(--mint-900);
    color: var(--white);
    width: 180px;
    height: 48px;
    border-radius: 12px;
    /* border: 1px solid var(--mint-900);
    color: var(--mint-900); */
    font-size: 14px;
    &:hover {
        background-color: var(--mint-950);
    }
    &.pink {
        background-color: var(--red-700);
        &:hover {
            background-color: var(--red-800);
        }
    }
`

export const Right = styled.div`
    /* border: 1px solid red; */
`

export const Image = styled.img`
    width: 500px;
    /* height: 260px; */
    position: relative;
    top: -8px;
    right: -120px;
    z-index: 1;
    opacity: 0.9;
    /* transform: rotate(45deg);
    top: -45px;
    position: relative; */
    /* border: 1px solid red; */
    &.small {
        width: 440px;
    }
`