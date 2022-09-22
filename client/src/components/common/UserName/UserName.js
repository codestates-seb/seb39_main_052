import { Image, Name, Wrapper } from "./UserNameStyle";

const UserName = ({ image, name, className }) => {
    return (
        <Wrapper className={className}>
            <Image src={image} alt={`profile`} />
            <Name>{name}</Name>
        </Wrapper>
    )
};

export default UserName;