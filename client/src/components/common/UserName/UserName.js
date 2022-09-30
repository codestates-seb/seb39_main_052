import { Image, Name, Wrapper } from "./UserNameStyle";
import logoface from "../../../assets/small_logoface.png";

const UserName = ({ image, name, className }) => {

    return (
        <Wrapper className={className}>
            <Image src={image ? image : logoface} alt={`profile`} />
            <Name>{name}</Name>
        </Wrapper>
    )
};

export default UserName;