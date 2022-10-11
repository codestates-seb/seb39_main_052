import styled from 'styled-components'

export const SortWrapper = styled.div`
    display: flex;
    justify-content: center;
    align-items: flex-end;
    color: var(--fridge-700);
    font-size: 13px;
`

export const Option = styled.div`
    padding: 0 8px;
    cursor: pointer;
    // 옵션 사이 줄
    :not(:last-of-type) {
        border-right: 1px solid var(--fridge-800);
    }
    // 선택된 옵션 볼드 효과 
    &.selected {
        font-weight: 700;
        color: var(--fridge-800);
    }
`
