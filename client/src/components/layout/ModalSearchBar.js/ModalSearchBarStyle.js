import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import styled from "styled-components";

export const SearchBar = styled.div`
  display: flex;
  align-items: center;
  /* justify-content: flex-start; //input이 삐져나가는 현상 없애기 */
  justify-content: space-evenly;
  /* width: 68%; //기존 */
  /* width: 95%; */
  height: 48px;
  border: 2px solid var(--fridge-300);
  background-color: var(--gray-050);
  padding: 4px 16px 4px 16px;
  border-radius: 8px;
`;
export const SearchInput = styled.input`
  flex-grow: 1;
  border: none;
  font-size: 16px;
  /* margin: 2px 0px 0 16px; */
  padding-left: 8px; //돋보기아이콘과 input 커서사이
`;
export const StyledFontAwesomeIcon = styled(FontAwesomeIcon)`
  color: var(--fridge-800);
  font-size: 18px;
`;
