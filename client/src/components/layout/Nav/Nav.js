import { Link } from "react-router-dom";
import LogOut from "../DropDown/LogOut";
import { NavWrapper } from "./NavStyles";

const Nav = () => {
    return (
        <NavWrapper>
            <Link to="/">Home</Link>
            <LogOut></LogOut>
            <Link to="/login">로그인</Link>
            <Link to="/mypage">마이페이지</Link>
            <Link to="/signup">회원가입</Link>
            <Link to="/search">냉장고 파먹기</Link>
            <Link to="/recipes/new">새 레시피 작성하기</Link>
            <Link to="/recipes/edit">레시피 수정하기</Link>
            <Link to="/recipes/1">레시피 상세보기</Link>
            <Link to="/myfridge">나의 냉장고</Link>
        </NavWrapper>
    )
};

export default Nav;
