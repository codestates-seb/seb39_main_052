import styled from "styled-components";

export const Container = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 28px 0 0 0;
    width: 100%;
`

export const Header = styled.div`
    display: flex;
    margin-bottom: 28px;
`

export const Warning = styled.div`
    color: red;
    padding: 4px 0 0 4px;
    &.invisible {
        display: none;
    }
`

export const Main = styled.div`
    display: flex;
    flex-direction: column;
    margin-right: 60px;
`
export const ImageWrap = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin-top: auto;
    > *:nth-child(2){
        margin: 10px 0 0 0;
    }
`

export const Input = styled.input`
    &.large {
        height: 60px;
        width: 500px;
        font-size: 28px;
        padding: 0 12px;
        border-radius: 10px;
    }
    &.small {
        height: 28px;
        width: 56px;
        margin-right: 4px;
        padding: 2px 0 0 8px;
        border-radius: 5px;
    }
`

export const Portion = styled.div`
    display: flex;
    flex-direction: column; 
    margin-top: 28px;
`

export const Time = styled.div`
    display: flex;
    flex-direction: column; 
    margin-top: 40px;
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
    margin: 12px 0 48px 0;
`

export const Steps = styled.div`
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    width: 842px;
`
export const ButtonWrap = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    width: 842px;
    margin: 20px 0 0 0;
`

export const Button = styled.button`

`