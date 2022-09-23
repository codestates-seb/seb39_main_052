import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import styled from 'styled-components';

export const SearchBar = styled.div`
    display: flex;
    align-items: center;
    width: 320px;
    border: 2px solid var(--fridge-300);
    background-color: var(--gray-050);
    padding: 4px 4px 4px 8px;
    border-radius: 8px;
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
    max-height: 96px;
    overflow-y: scroll;
    /* border: 1px solid red; */
    margin: 0 0 8px 0;
    padding: 4px 0px 0 0px;
    border: 2px solid var(--fridge-300);
    border-top: none;
    border-bottom-left-radius: 8px;
    border-bottom-right-radius: 8px;
    font-size: 14px;
    background-color: var(--gray-050);
    ::-webkit-scrollbar {
        width: 8px;
        background-color: var(--gray-100);
    }
    ::-webkit-scrollbar-thumb {
        border-radius: 10px;
        background-color: var(--fridge-600);
    }
`
export const Suggestion = styled.li`
    cursor: pointer;
    padding: 0 4px 0 8px;
    list-style: none;
    color: var(--fridge-800);
    margin: 0 0 4px 0;
    :hover {
        background-color: var(--gray-200)
    }
    :active {
        background-color: var(--fridge-300)
    }
`

