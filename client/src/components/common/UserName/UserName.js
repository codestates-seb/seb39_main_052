import { Image, Name, Wrapper } from "./UserNameStyle";

const UserName = ({ image, name }) => {
    return (
        <Wrapper>
            <Image src={image} alt={`profile`} />
            <Name>{name}</Name>
        </Wrapper>
    )
};

export default UserName;