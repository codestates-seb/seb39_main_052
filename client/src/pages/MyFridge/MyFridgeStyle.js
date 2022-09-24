import styled from 'styled-components';

export const Container = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
`

export const Fridge = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 880px;
    padding: 32px 0 28px 24px;
    border-radius: 16px;
    background-color: var(--fridge-200);
    box-shadow: var(--shadow-medium);
`

export const Title = styled.h1`
    margin: 24px 0 16px 0;
`

export const ColumnHeads = styled.div`
    display: flex;
    width: 100%;
    justify-content: space-around;
    padding: 0 56px 0 3px;
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
    padding: 0 12px 0 0;
    margin: 8px 0 0 0;
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
