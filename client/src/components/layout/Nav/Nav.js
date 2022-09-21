import { Link } from 'react-router-dom'
import { NavWrapper } from './NavStyles';

const Nav = () => {
    return (
        <NavWrapper>
            <Link to="/">Home</Link>
            <Link to="/login">로그인</Link>
            <Link to="/signup">회원가입</Link>
            <Link to="/recipes/new">새 레시피 작성하기</Link>
            <Link to="/recipes/edit">레시피 수정하기</Link>
            <Link to="/recipes">레시피 상세보기</Link>
        </NavWrapper>
    )
};

export default Nav;