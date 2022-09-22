import LogOut from "../../layout/DropDown/LogOut";
import largelogoblack from "../../../assets/largelogoblack.png";
import { Link } from "react-router-dom";
import { Header, Nav, LeftBox, LogoBox, Img, RightBox } from "./GnbStyle";

const Gnb = () => {
  return (
    <Header>
      <Nav>
        <LeftBox className="leftbox">
          <ul>
            <li>냉장고 파먹기</li>
            <li>나의 냉장고</li>
          </ul>
        </LeftBox>
        <LogoBox className="logo">
          <Link to="/">
            <Img src={largelogoblack} />
          </Link>
        </LogoBox>
        <RightBox className="rightbox">
          <ul>
            <li>
              <Link to="/login">로그인</Link>
            </li>
            <li>
              <LogOut></LogOut>
            </li>
            <li>search</li>
          </ul>
        </RightBox>
      </Nav>
    </Header>
  );
};

export default Gnb;
