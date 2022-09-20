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
  width: ${(props) => props.width || " 300px"};
  height: ${(props) => props.height || "35px"};
  background-color: ${(props) => props.backgroundColor || "var(--fridge-500)"};
  color: ${(props) => props.color || "var(--white)"};
  margin-top: 20px;
  font-size: 16px;

  &.large {
    height: 48px;
    width: 200px;
    font-size: 20px;
  }
  &.gray {
    background: var(--gray-400);
  }
  &:hover {
    /* background-color: var(--fridge-700); */
    background-color: ${(props) =>
      props.hoverBackgroundColor || "var(--fridge-700)"};
    color: ${(props) => props.hoverColor};
    &.gray {
      background: var(--gray-500);
    }
  }

  &.disabled-btn {
    background-color: var(--gray-400);
  }
`;

export default GeneralButton;
