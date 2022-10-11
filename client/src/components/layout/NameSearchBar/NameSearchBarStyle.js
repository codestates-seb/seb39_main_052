import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import styled from 'styled-components';

export const Container = styled.div`
    display: flex;
    flex-direction: column;
`

export const SearchBar = styled.div`
    display: flex;
    align-items: center;
    width: 320px;
    border: 2px solid var(--fridge-300);
    background-color: var(--gray-050);
    padding: 4px 4px 4px 8px;
    border-radius: 8px;
    z-index: 99;
    @media ${({ theme }) => theme.device.mobile} {
        height: 32px;
        padding-left: 2px;
    }  
`
export const SearchInput = styled.input`
    flex-grow: 1;
    border: none;
    font-size: 14px;
`
export const StyledFontAwesomeIcon = styled(FontAwesomeIcon)`
    color: var(--fridge-800);
    font-size: 16px;
    cursor: pointer;
    :active {
        font-size: 15px;
    }
`
export const DropDown = styled.ul`
    width: 320px;
    max-height: 100px;
    overflow-y: scroll;
    /* border: 1px solid red; */
    margin: 0 0 8px 0;
    padding: 8px 0px 0 0px;
    border: 2px solid var(--fridge-300);
    border-top: none;
    border-bottom-left-radius: 8px;
    border-bottom-right-radius: 8px;
    font-size: 14px;
    background-color: var(--gray-050);
    position: absolute;
    top: 165px;
    z-index: 98;
    box-shadow: var(--shadow-medium);
    ::-webkit-scrollbar {
        width: 8px;
        background-color: var(--gray-100);
    }
    ::-webkit-scrollbar-thumb {
        border-radius: 10px;
        background-color: var(--fridge-600);
    }
    @media ${({ theme }) => theme.device.mobile} {
        top: 170px;
    }
`
export const Suggestion = styled.li`
    cursor: pointer;
    padding: 2px 4px 2px 8px;
    list-style: none;
    color: var(--fridge-800);
    :not(:last-of-type) {
        border-bottom: 1px solid var(--fridge-200);
    }
    :hover {
        background-color: var(--gray-200);
    }
    :active {
        background-color: var(--fridge-200);
    }
    background-color: ${(props) => props.selected && 'var(--fridge-200)'}
`

