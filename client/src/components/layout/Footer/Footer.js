import { Container, Right, Left, LeftBottom, LeftTop, StyledFontAwesomeIcon } from "./FooterStyle"
import { faGithub } from "@fortawesome/free-brands-svg-icons";
import { Link} from "react-router-dom";

const Footer = () => {

    return (
        <Container>
            <Left>
                <LeftTop>
                    <div className="BE">Backend</div>
                    <div className="FE">Frontend</div>
                </LeftTop>
                <LeftBottom>
                    <div className="BE">
                        <a href="https://github.com/SJ0000" target="_blank" rel="author noreferrer">
                            강성진
                            <StyledFontAwesomeIcon icon={faGithub} />
                        </a>
                    </div>
                    <div className="BE">
                        <a href="https://github.com/yongjuvv" target="_blank" rel="author noreferrer">
                            김용주
                            <StyledFontAwesomeIcon icon={faGithub} />
                        </a>
                    </div>
                    <div className="FE">
                        <a href="https://github.com/imsujinpark" target="_blank" rel="author noreferrer">
                            박수진
                            <StyledFontAwesomeIcon icon={faGithub} />
                        </a>
                    </div>
                    <div className="FE">
                        <a href="https://github.com/hana1203" target="_blank" rel="author noreferrer">
                            조하나
                            <StyledFontAwesomeIcon icon={faGithub} />
                        </a>
                    </div>
                </LeftBottom>
            </Left>
            <Right>
                <div>코드스테이츠 SEB 39기</div>
                <div>52조 메인 프로젝트</div>
                <div>자취생 냉장고</div>
            </Right>
        </Container>
    )
}

export default Footer