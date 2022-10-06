import MenuTab from "../../components/layout/MenuTab/MenuTab";
import { MyPageContainer } from "./MyPageStyle";
import Footer from "../../components/layout/Footer/Footer";

const MyPage = () => {
  return (
    <MyPageContainer>
      <MenuTab></MenuTab>
      <Footer />
    </MyPageContainer>
  );
};
export default MyPage;
