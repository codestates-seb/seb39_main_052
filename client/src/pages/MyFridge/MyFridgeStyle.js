import styled from 'styled-components';

export const Container = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    min-height: 70vh;
`
export const SortWrapper = styled.div`
    display: flex;
    justify-content: flex-end;
    align-items: center;
    color: var(--fridge-700);
    font-size: 13px;
    margin-bottom: 8px;
    width: 800px;
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

export const Fridge = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 880px;
    padding: 14px 14px 14px 14px;
    border-radius: 16px;
    background-color: var(--fridge-200);
    box-shadow: var(--shadow-medium);
`

export const InnerContainer = styled.div`
    padding: 16px 0 14px 12px;
    border-radius: 16px;
    display: flex;
    flex-direction: column;
    align-items: center;
    background-color: var(--fridge-100);
`

export const Title = styled.h1`
    margin: 24px 0 16px 0;
`

export const ColumnHeads = styled.div`
    display: flex;
    width: 100%;
    justify-content: space-around;
    padding: 0 40px 0 0px;
    margin: 0;
`

export const Head = styled.div`
    width: 150px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    color: var(--white);
    /* font-weight: 900; */
    /* border: 1px solid red; */
    background-color: var(--fridge-500);
    border-radius: 4px;
    box-shadow: var(--shadow-low) inset;
    /* box-shadow: var(--fridge-600) 3px 3px 6px 0px inset, var(--fridge-300) -1px -1px 6px 1px inset; */
`

export const InputWrapper = styled.div`
    height: 360px;
    padding: 0 2px 0 0;
    margin: 8px 0 0 4px;
    overflow-y: scroll;
    ::-webkit-scrollbar {
        width: 8px;
        background-color: var(--fridge-100);
    }
    ::-webkit-scrollbar-thumb {
        border-radius: 10px;
        background-color: var(--fridge-500);
    }
`

export const ButtonWrap = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    width: 480px;
    margin: 0px 0 0 0;
`