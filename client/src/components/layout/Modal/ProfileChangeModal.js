import { useNavigate } from "react-router-dom";
import GeneralButton from "../../common/Button/GeneralButton";
import GeneralModal from "./GeneralModal";
import {
  ProfileWrapper,
  ProfilePhoto,
  ProfileName,
  ImgContainer,
  Img,
  Input,
} from "./ProfileChangeModalStyle";
import small_logoface from "../../../assets/small_logoface.png";
import { useRef, useState } from "react";
import {
  editUserPhoto,
  deleteUserPhoto,
  editUserName,
} from "../../../features/userSlice";
import { useDispatch, useSelector } from "react-redux";
import { StyledFontAwesomeIcon } from "../../common/ImageUploader/ImageUploaderStyle";
import { faPlus, faXmark } from "@fortawesome/free-solid-svg-icons";
import axios from "axios";

const ProfileChangeModal = ({ handleClose, profileData }) => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  //redux에서 가져오는 유저 데이터 MyProfile 컴포넌트에서 props로 가져옴
  // const profileData = useSelector((state) => {
  //   return {
  //     userId: state.user.userId,
  //     userName: state.user.userName,
  //     userProfileImgPath: state.user.userProfileImgPath,
  //   };

  const profilePhoto = profileData.userProfileImgPath; //redux에서 가져오는 프로필 이미지
  const [previewImg, setPreviewImg] = useState(profilePhoto); //미리보기 이미지를 리덕스에서 가져온 프로필 이미지 정보로 셋팅

  const profileName = profileData.userName; //redux에서 가져오는 프로필 닉네임
  const [previewUserName, setPreviewUserName] = useState(profileName); //미리보기 닉네임

  const [uploadFile, setUploadFile] = useState({}); //자식 PorfileImgUploader에서 업로드파일 셋팅하고 부모 컴포넌트 ProfileChangeModal에서 파일 데이터 서버에 전송해야되니까 상태로 관리

  // const [previewNameLenMsg, setPreviewNameLenMsg] = useState("");

  //previewImg 있으면 수정한 사진 보내기, null이면 다시 대표이미지로 되돌리는거니 리덕스에도 이미지 삭제하기
  const changeProfile = () => {
    console.log("체인지프로필부모에서 uploadFile객체?", uploadFile);
    //FileList {0: File, length: 1}
    //0: File {name: '소면.jpeg', lastModified: 1664802180985, lastModifiedDate:...}

    const formdata = new FormData();
    formdata.append("profileImage", uploadFile);
    formdata.append(
      "requestBody",
      new Blob([JSON.stringify({ name: previewUserName })], {
        type: "application/json",
      })
    );

    for (let key of formdata.keys()) {
      console.log(`폼데이터키 ${key}: ${formdata.get(key)}`);
    }

    //서버에 프로필 수정 요청
    axios({
      method: "patch",
      url: `/api/members`,
      headers: {
        "Content-Type": "multipart/form-data",
      },
      data: formdata,
    })
      .then((res) => {
        console.log(res);
        //서버 통신 성공해야지 리덕스 상태 변경하기
        if (previewImg === null) {
          dispatch(deleteUserPhoto());
        } else {
          dispatch(editUserPhoto({ userProfileImgPath: previewImg }));
        }
        if (previewUserName) {
          dispatch(editUserName({ userName: previewUserName }));
        }

        formdata.delete("profileImage");
        formdata.delete("requestBody");
        alert("프로필이 잘 변경되었어요");
        handleClose(); //모달창 닫기
      })
      .catch((err) => {
        console.log(err.response);
        alert("프로필을 변경할 수 없어요ㅠㅠ");
        formdata.delete("profileImage");
        formdata.delete("requestBody");
      });
  };

  //input으로 들어오는 미리보기 화면상 닉네임
  const changeUserName = (e) => {
    setPreviewUserName(e.target.value);
  };

  // if (previewUserName.length > 10) {
  //   setPreviewNameLenMsg("닉네임은 2자 이상 10자 이하로 입력해주세요");
  // } //유효성 검사하려는데 화면이 나감 Uncaught Error: Too many re-renders

  return (
    <GeneralModal handleClose={handleClose} width="300px" height="340px">
      <ProfileWrapper className="ProfileWrapper">
        <h2>내 프로필 변경하기</h2>
        {/* <ProfilePhoto
          src={
            profileData.userProfileImgPath === null
              ? small_logoface
              : profileData.userProfileImgPath
          }
        ></ProfilePhoto> */}
        <ProfileImgUploader
          previewImg={previewImg}
          setPreviewImg={setPreviewImg}
          setUploadFile={setUploadFile}
        ></ProfileImgUploader>
        {/* <ProfileName>{profileData.userName}</ProfileName> //input으로 변경전 */}
        <ProfileName
          type="text"
          value={previewUserName}
          onChange={changeUserName}
        ></ProfileName>
        {/* {previewUserName.length > 10 && <p>{previewNameLenMsg}</p>} */}
        <div className="cancel_done_button_wrapper">
          <GeneralButton onClick={handleClose} className={"small"}>
            취소
          </GeneralButton>
          <GeneralButton onClick={changeProfile} className={"small"}>
            완료
          </GeneralButton>
        </div>
      </ProfileWrapper>
    </GeneralModal>
  );
};

const ProfileImgUploader = ({ previewImg, setPreviewImg, setUploadFile }) => {
  const [isDragOver, setIsDragOver] = useState(false);

  const imgRef = useRef(); // input 내장 버튼을 직접 만든 버튼과 연결하기 위한 ref

  const imageHandler = (fileList) => {
    // e.preventDefault();
    setIsDragOver(false);

    if (fileList[0]) {
      // const uploadFile = fileList[0];
      // 미리보기 이미지는 따로 상태로 관리해야 함. 여기서 직접 리덕스에 저장하면 미리보기하는 이미지로 리덕스에 변경됨
      // 모달에서 완료버튼을 눌렀을때 서버랑통신하고, redux 상태도바꾸기
      // dispatch(editUserPhoto({ userProfileImgPath: URL.createObjectURL(uploadFile) }));
      setUploadFile(fileList[0]); //부모 컴포넌트 ProfileChangeModal에서 파일 데이터 서버에 전송해야되니까 상태로 관리
      setPreviewImg(URL.createObjectURL(fileList[0])); //미리보기 사진만 변경하기
      console.log("이미지핸들러에서 fileList[0] 객체?", fileList[0]);
      //FileList {0: File, length: 1}
      //0: File {name: '소면.jpeg', lastModified: 1664802180985, lastModifiedDate:...}
    }
  };

  const handleClick = async () => {
    // 버튼 클릭으로 input 클릭과 동일한 기능을 한다.
    await imgRef.current.click();
  };

  const handleDelete = () => {
    imgRef.current.value = "";
    // setPreviewImg(profilePhoto); //초기에 redux에서 가져온 이미지로 다시 되돌림- 로직 안맞음
    setPreviewImg(null);
  };

  // 클릭으로 이미지 파일 추가
  const onClickFiles = (e) => {
    console.log(e.target.files); //FileList {0: File, length: 1}
    //0: File {name: '소면.jpeg', lastModified: 1664802180985, lastModifiedDate:...}
    e.preventDefault();
    imageHandler(e.target.files);
  };

  // drop으로 이미지 파일 추가
  const onDropFiles = (e) => {
    console.log(e.dataTransfer.files);
    e.preventDefault();
    imageHandler(e.dataTransfer.files);
  };
  // 없으면 drop 작동안함
  const dragOver = (e) => {
    e.preventDefault();
    setIsDragOver(true);
  };

  return (
    <ImgContainer>
      <Img
        src={previewImg === null ? small_logoface : previewImg} //미리보기(리덕스에서 가져온 프로필이미지) 이미지 없으면 대표이미지, 있으면 원래갖고있는 이미지 띄우기
        onDrop={onDropFiles}
        onDragOver={dragOver}
        className={isDragOver && "dragover"}
      ></Img>
      <Input
        type="file"
        accept="image/*"
        name="file"
        ref={imgRef}
        onChange={onClickFiles}
      ></Input>
      {previewImg ? ( //미리보기 이미지 있으면 x 버튼눌러서 삭제, 없으면(대표이미지) +눌러서 미리보기 사진업로드
        <StyledFontAwesomeIcon
          icon={faXmark}
          className="cancel"
          onClick={handleDelete}
        />
      ) : (
        <StyledFontAwesomeIcon
          icon={faPlus}
          className="upload"
          onClick={handleClick}
        ></StyledFontAwesomeIcon>
      )}
    </ImgContainer>
  );
};

export default ProfileChangeModal;
