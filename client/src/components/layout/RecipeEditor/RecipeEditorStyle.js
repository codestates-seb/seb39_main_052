import styled from "styled-components";

export const Container = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 28px 0 0 0;
    width: 100%;
    font-size: 12px;
`

export const Header = styled.div`
    display: flex;
    margin-bottom: 28px;
`

export const Warning = styled.div`
    color: red;
    padding: 4px 0 0 4px;
    font-size: 12px;
    &.invisible {
        display: none;
    }
`

export const Main = styled.div`
    display: flex;
    flex-direction: column;
    margin-right: 28px;
`
export const ImageWrap = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin-top: auto;
    // 이미지 전용 유효성 경고창
    > *:nth-child(2) {
        margin: 10px 0 0 0;
    }
`

export const Input = styled.input`
    &.large {
        height: 2.5rem;
        width: 20rem;
        font-size: 16px;
        padding: 3px 12px 0 12px;
        border-radius: 10px;
    }
    &.small {
        height: 24px;
        width: 44px;
        margin-right: 4px;
        padding: 1px 0 0 8px;
        border-radius: 5px;
        font-size: 11px;
    }
`

export const Portion = styled.div`
    display: flex;
    flex-direction: column; 
    justify-content: center;
    margin-top: 24px;
    > * {
        display: flex;
        align-items: center;
    }
`

export const Select = styled.select`
    height: 20px;
    width: 36px;
    margin-right: 6px;
    padding-left: 5px;
    border-radius: 4px;
    font-size: 10px;
`

export const Time = styled.div`
    display: flex;
    flex-direction: column; 
    margin-top: 32px;
    > * > div {
        display: flex;
        align-items: center;
    }
`

export const Ingredients = styled.div`
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    width: 548px;
    margin: 0px 0 30px 0;
`

export const Steps = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    width: 548px;
`
export const ButtonWrap = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    width: 548px;
    margin: 0px 0 0 0;
`

export const Button = styled.button`

`