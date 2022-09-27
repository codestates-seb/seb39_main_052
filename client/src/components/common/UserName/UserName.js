import { Image, Name, Wrapper } from "./UserNameStyle";
import blankImage from "../../../assets/blankImage.webp";

const UserName = ({ image, name, className }) => {

    return (
        <Wrapper className={className}>
            <Image src={image ? image : blankImage} alt={`profile`} />
            <Name>{name}</Name>
        </Wrapper>
    )
};

export default UserName;