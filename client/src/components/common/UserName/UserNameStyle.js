import styled from "styled-components";

export const Wrapper = styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    margin: 0 0 0 0;
    color: var(--fridge-800);
    font-size: 12px;
    
    &.large {
        > img {
            width: 24px;
            height: 24px;
        }
        font-size: 14px;
    }
    // 댓글 요소에서는 길이를 넘어가면 ...이 보이게 하기
    &.short {
        > div {
            width: 80px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
        }
    }
    // 레시피 카드 요소에서는 길이를 넘어가면 ...이 보이게 하기
    &.long {
        > div {
            width: 100px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
        }
    }
`

export const Image = styled.img`
    width: 16px;
    height: 16px;
    border-radius: 50%;
    object-fit: cover;
`

export const Name = styled.div`
    margin: 0 0 0 8px;
`