import styled from "styled-components";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

export const Container = styled.div`
    background-color: var(--white);
    position: relative;
    box-shadow: var(--shadow-low);
    &.big {
        height: 12.5rem;
        width: 12.5rem;
        border-radius: 10px;
        > *.loading {
            left: 36%;
            top: 36%;
            font-size: 56px;
        }
        > *.upload {
            left: 78%;
            top: 78%;
            font-size: 36px;
            :active {
                color: var(--mint-600); 
            }
        }
        > *.cancel {
            left: 82%;
            top: 0%;
            font-size: 40px;
            color: var(--white);
        }
    }
    /* &.mobile {
        height: 360px;
        width: 360px;
        border-radius: 10px;
    } */
    &.small {
        height: 140px;
        width: 140px;
        border-radius: 10px;
        > *.loading {
            left: 34%;
            top: 34%;
            font-size: 44px;
        }
        > *.upload {
            left: 78%;
            top: 76%;
            font-size: 24px;
            :active {
                color: var(--mint-600); 
            }
        }
        > *.cancel {
            left: 84%;
            top: 2%;
            font-size: 24px;
            color: var(--white);
        }
    }
    &.round {
        border-radius: 50%;
    }
    // 사진 추가 아이콘
    > *:nth-child(3) {
        left: 34%;
        top: 34%;
        font-size: 10px;
    }
    // 사진 추가 삭제 아이콘은 호버시에만 보이기
    :not(:hover) {
        > *.upload {
            display: none;
        }
        > *.cancel {
            display: none;
        }
    }
`

export const Img = styled.img`
    object-fit: cover;
    height: 100%;
    width: 100%;
    border-radius: inherit;
`

export const Input = styled.input`
    display: none;
`

export const Button = styled.button`
    width: 100px;
    position: absolute;
    right: 0%;
    bottom: 0%;
    display: none;
`

export const StyledFontAwesomeIcon = styled(FontAwesomeIcon)`
    position: absolute;
    color: var(--gray-600);
    cursor: pointer;
    filter: drop-shadow(0 0 1px #777);
`;