import { useNavigate } from "react-router-dom";
import GeneralButton from "../../common/Button/GeneralButton";
import GeneralModal from "./GeneralModal";
import {
  ProfileWrapper,
  ProfilePhoto,
  ProfileName,
} from "./ProfileChangeModalStyle";
import small_logoface from "../../../assets/small_logoface.png";

const ProfileChangeModal = ({ handleClose, profileData }) => {
  const navigate = useNavigate();
  // const dummyData = [
  //   {
  //     id: 1,
  //     memberName: "들깨러버들깨러버",
  //     memberImage:
  //       "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
  //   },
  // ];
  //props로 MyProfile 부모 컴포넌트에서 받아온 프로필 상태
  // const profileData = useSelector((state) => {
  //   return {
  //     userId: state.user.userId,
  //     userName: state.user.userName,
  //     userProfileImgPath: state.user.userProfileImgPath,
  //   };

  return (
    <GeneralModal handleClose={handleClose} width="300px" height="340px">
      <ProfileWrapper className="ProfileWrapper">
        <h2>내 프로필 변경하기</h2>
        <ProfilePhoto
          src={
            profileData.userProfileImgPath === null
              ? small_logoface
              : profileData.userProfileImgPath
          }
        ></ProfilePhoto>
        <ProfileName>{profileData.userName}</ProfileName>

        <div className="cancel_done_button_wrapper">
          <GeneralButton onClick={handleClose} className={"small"}>
            취소
          </GeneralButton>
          <GeneralButton className={"small"}>완료</GeneralButton>
        </div>
      </ProfileWrapper>
    </GeneralModal>
  );
};

export default ProfileChangeModal;
