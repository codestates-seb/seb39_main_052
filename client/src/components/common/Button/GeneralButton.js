import styled from "styled-components"

const GeneralButton = ({width, height, children}) => {
    return (
        <Button width={width} height={height}> {children} </Button>
    )

}

const Button = styled.button`
    border-radius: 10px;
    border: none;
    background-color: var(--fridge-500);
    color: var(--white);
    width: 250px;
    height: 35px;

    &:hover {
        background-color: var(--mint-500);
}
`

export default GeneralButton