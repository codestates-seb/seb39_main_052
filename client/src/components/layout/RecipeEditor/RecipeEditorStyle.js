import styled from "styled-components";

export const Container = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 48px;
    width: 100%;
`

export const Header = styled.div`
    display: flex;
    margin-bottom: 28px;
`

export const Main = styled.div`
    display: flex;
    flex-direction: column;
    margin-right: 60px;
`

export const Input = styled.input`
    &.large {
        height: 60px;
        width: 500px;
        font-size: 28px;
        padding-left: 12px;
        border-radius: 10px;
    }
    &.small {

    }
`

export const Portion = styled.div`
    display: flex;
    flex-direction: column; 
    margin-top: 36px;
`

export const Select = styled.select`
    height: 28px;
    margin-right: 4px;
    padding-left: 6px;
    border-radius: 5px;
`

export const Ingredients = styled.div`
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    width: 842px;
    margin-bottom: 48px;
`

export const Steps = styled.div`
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    width: 842px;
`