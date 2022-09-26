import GeneralButton from "../../common/Button/GeneralButton";
import GeneralModal from "./GeneralModal";
import {
  ProfileWrapper,
  ProfilePhoto,
  ProfileName,
} from "./ProfileChangeModalStyle";

const ProfileChangeModal = ({ handleClose }) => {
  const dummyData = [
    {
      id: 1,
      memberName: "들깨러버들깨러버",
      memberImage:
        "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
    },
  ];
  return (
    <GeneralModal handleClose={handleClose} width="300px" height="340px">
      <ProfileWrapper className="ProfileWrapper">
        <h2>내 프로필 변경하기</h2>
        <ProfilePhoto src={dummyData[0].memberImage}></ProfilePhoto>
        <ProfileName>{dummyData[0].memberName}</ProfileName>

        <div className="cancel_done_button_wrapper">
          <GeneralButton className={"small"}>취소</GeneralButton>
          <GeneralButton className={"small"}>완료</GeneralButton>
        </div>
      </ProfileWrapper>
    </GeneralModal>
  );
};

export default ProfileChangeModal;
