import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import styled from "styled-components";

export const SearchBar = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  /* width: 68%; //기존 */
  width: 95%;
  height: 48px;
  border: 2px solid var(--fridge-300);
  background-color: var(--gray-050);
  padding: 4px 4px 4px 16px;
  border-radius: 8px;
`;
export const SearchInput = styled.input`
  flex-grow: 1;
  border: none;
  font-size: 16px;
  margin: 2px 8px 0 16px;
`;
export const StyledFontAwesomeIcon = styled(FontAwesomeIcon)`
  color: var(--fridge-800);
  font-size: 18px;
`;
