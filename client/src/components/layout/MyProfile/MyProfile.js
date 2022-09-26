import { useState } from "react";
import GeneralButton from "../../common/Button/GeneralButton";
import ProfileChangeModal from "../Modal/ProfileChangeModal";
import {
  MyProfileContainer,
  ProfileWrapper,
  ProfilePhoto,
  ProfileName,
} from "./MyProfileStyle";

const MyProfile = () => {
  const dummyData = [
    {
      id: 1,
      memberName: "들깨러버들깨러버",
      memberImage:
        "https://i.pinimg.com/736x/81/03/37/810337c76e5b1d32c0a3ef2d376735eb.jpg",
    },
  ];

  const [showProfileChangeModal, setShowProfileChangeModal] = useState(false);
  const clickProfileChangeModal = () => {
    setShowProfileChangeModal(!showProfileChangeModal);
  };

  return (
    <MyProfileContainer className="MyProfileContainer">
      <ProfileWrapper>
        <ProfilePhoto src={dummyData[0].memberImage}></ProfilePhoto>
        <ProfileName>{dummyData[0].memberName}</ProfileName>
      </ProfileWrapper>
      <GeneralButton width="250px" onClick={clickProfileChangeModal}>
        프로필 변경하기
      </GeneralButton>
      {showProfileChangeModal && (
        <ProfileChangeModal
          handleClose={clickProfileChangeModal}
        ></ProfileChangeModal>
      )}
    </MyProfileContainer>
  );
};

export default MyProfile;
