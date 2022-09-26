import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import styled from 'styled-components';

export const Container = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
`
export const SearchBar = styled.div`
    display: flex;
    align-items: center;
    width: 320px;
    border: 2px solid var(--fridge-300);
    background-color: var(--gray-050);
    padding: 4px 4px 4px 8px;
    border-radius: 8px;
    z-index: 997;
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
    position: absolute;
    top: 34%;
    z-index: 996;
    box-shadow: var(--shadow-medium);
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
    background-color: ${(props) => props.selected && 'var(--fridge-200)'};
`
export const TagWrapper = styled.div`
    display: flex;
    justify-content: center;
    width: 400px;
    flex-wrap: wrap;
    margin: 8px 0 16px 0; 
`
export const Tag = styled.div`
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-radius: 8px;
    background-color: var(--fridge-700);
    color: var(--white);
    font-size: 13px;
    margin: 4px 4px 4px 4px;
    padding: 4px 8px;
    // x 아이콘 호버시 색상 변경
    :hover {
        > :nth-child(1) {
            color: var(--white);
        }
    }
`
export const StyledFaXmark = styled(FontAwesomeIcon)`
    margin-left: 8px;
    font-size: 12px;
    color: var(--fridge-300);
    :active {
        font-size: 11px;
    }
`