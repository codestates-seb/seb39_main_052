import { useState } from "react";
import { useSelector } from "react-redux"; //for bringing user data from redux
import GeneralButton from "../../common/Button/GeneralButton";
import ProfileChangeModal from "../Modal/ProfileChangeModal";
import {
  MyProfileContainer,
  ProfileWrapper,
  ProfilePhoto,
  ProfileName,
} from "./MyProfileStyle";

import small_logoface from "../../../assets/small_logoface.png";

const MyProfile = () => {
  const profileData = useSelector((state) => {
    return {
      userId: state.user.userId,
      userName: state.user.userName,
      userProfileImgPath: state.user.userProfileImgPath,
    };
  });
  //   console.log(profileData); //{id: 4, name: 'test1', profileImg: null}

  const [showProfileChangeModal, setShowProfileChangeModal] = useState(false);
  const clickProfileChangeModal = () => {
    setShowProfileChangeModal(!showProfileChangeModal);
  };

  return (
    <MyProfileContainer className="MyProfileContainer">
      <ProfileWrapper>
        {profileData.userProfileImgPath === null ? (
          <ProfilePhoto
            src={small_logoface}
            className="small_logoface"
          ></ProfilePhoto>
        ) : (
          <ProfilePhoto src={profileData.userProfileImgPath}></ProfilePhoto>
        )}
        <ProfileName>{profileData.userName}</ProfileName>
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
