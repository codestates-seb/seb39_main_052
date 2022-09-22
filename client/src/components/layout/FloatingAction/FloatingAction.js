import GeneralButton from "../../common/Button/GeneralButton";
import { Link } from 'react-router-dom'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPencil } from "@fortawesome/free-solid-svg-icons";
import { ButtonWrapper } from "./FloatingActionStyle";

const FloatingAction = () => {
    return (
        <ButtonWrapper>
            <Link to="/recipes/new">
                <GeneralButton className={"round large shadow"}>
                    <FontAwesomeIcon icon={faPencil}/>
                </GeneralButton>
            </Link>
        </ButtonWrapper>
    )
};

export default FloatingAction;