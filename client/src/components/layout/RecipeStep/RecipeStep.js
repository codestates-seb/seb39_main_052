import { Container, Content, Image } from "./RecipeStepStyle";

const RecipeStep = ({index, image, content}) => {
    return (
        <Container>
            <Image src={image} alt={"recipe step image"} />
            <Content>{index}. {content}</Content>
        </Container>
    )
}

export default RecipeStep;