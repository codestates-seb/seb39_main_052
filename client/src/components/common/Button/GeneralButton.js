import styled from "styled-components";

const GeneralButton = ({
  disabled,
  children,
  className,
  width,
  height,
  backgroundColor,
  color,
  hoverBackgroundColor,
  hoverColor,
}) => {
  return (
    <Button
      disabled={disabled}
      className={className}
      width={width}
      height={height}
      backgroundColor={backgroundColor}
      color={color}
      hoverBackgroundColor={hoverBackgroundColor}
      hoverColor={hoverColor}
    >
      {" "}
      {children}{" "}
    </Button>
  );
};

const Button = styled.button`
  border-radius: 10px;
  border: none;
  /* width: 300px; */
  /* height: 35px; */
  /* background-color: var(--fridge-500); */
  /* color: var(--white); */
  width: ${(props) => props.width};
  height: ${(props) => props.height};
  background-color: ${(props) => props.backgroundColor};
  color: ${(props) => props.color};
  margin-top: 20px;
  font-size: 16px;

  &:hover {
    /* background-color: var(--fridge-700); */
    background-color: ${(props) => props.hoverBackgroundColor};
    color: ${(props) => props.hoverColor};
  }

  &.disabled-btn {
    background-color: var(--gray-400);
  }
`;

export default GeneralButton;
