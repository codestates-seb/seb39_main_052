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
  onClick,
  margin,
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
      onClick={onClick}
      margin={margin}
    >
      {" "}
      {children}{" "}
    </Button>
  );
};

const Button = styled.button`
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 10px;
  border: none;
  /* width: 300px; */
  /* height: 35px; */
  /* background-color: var(--fridge-500); */
  /* color: var(--white); */
  width: ${(props) => props.width || " 300px"};
  /* width: ${({ width }) => width || "300px"}; //props 이렇게 내려도 똑같음 */
  height: ${(props) => props.height || "35px"};
  background-color: ${(props) => props.backgroundColor || "var(--fridge-500)"};
  color: ${(props) => props.color || "var(--white)"};
  font-size: 16px;
  margin: ${(props) => props.margin || "20px 0 0 0"};
  &:disabled {
    background-color: var(--gray-400);
    cursor: none;
    &:hover {
      background-color: var(--gray-400);
    }
  }

  &.large {
    margin-top: 0;
    height: 68px;
    width: 68px;
    font-size: 20px;
  }
  &.medium {
    height: 48px;
    width: 120px;
  }
  &.small {
    margin-top: 0;
    height: 35px;
    width: 56px;
    font-size: 14px;
  }
  &.xsmall {
    margin-top: 0;
    height: 20px;
    width: 28px;
    font-size: 12px;
    border-radius: 5px;
  }
  &.round {
    border-radius: 50%;
  }
  &.shadow {
    box-shadow: var(--shadow-low);
  }
  &.gray {
    background: var(--gray-400);
  }
  &.invisible {
    display: none;
  }
  &.disabled {
    cursor: default;
    &:hover {
      background-color: var(--fridge-500);
    }
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
    cursor: none;
  }
`;

export default GeneralButton;
