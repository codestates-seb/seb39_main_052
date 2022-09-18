import styled from "styled-components";

const GeneralButton = ({ disabled, width, height, children, className }) => {
  return (
    <Button
      disabled={disabled}
      width={width}
      height={height}
      className={className}
    >
      {" "}
      {children}{" "}
    </Button>
  );
};

const Button = styled.button`
  border-radius: 10px;
  border: none;
  background-color: var(--fridge-500);
  color: var(--white);
  width: 300px;
  height: 35px;
  margin-top: 20px;

  &:hover {
    background-color: var(--fridge-700);
  }

  &.disabled-btn {
    background-color: var(--gray-400);
  }
`;

export default GeneralButton;
