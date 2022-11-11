import styled from "styled-components";
import {
  faEgg,
  faCarrot,
  faFish,
  faPizzaSlice,
  faBowlRice,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const Loading = () => {
  return (
    <LoadingContainer>
      <StyledFontAwesomeIcon icon={faEgg} spin />
      <StyledFontAwesomeIcon icon={faCarrot} spin />
      <StyledFontAwesomeIcon icon={faFish} spin />
      <StyledFontAwesomeIcon icon={faPizzaSlice} spin />
      <StyledFontAwesomeIcon icon={faBowlRice} spin />
    </LoadingContainer>
  );
};

export default Loading;

export const LoadingContainer = styled.div``;
export const StyledFontAwesomeIcon = styled(FontAwesomeIcon)`
  font-size: 24px;
  color: var(--primary-600);
  margin: 0 8px;
`;
