import styled from "styled-components";

export const ProfileWrapper = styled.div`
  /* padding: 16px; */
  display: flex;
  flex-direction: column;
  align-items: center;

  > h2 {
    padding-bottom: 30px;
  }
  > div.cancel_done_button_wrapper {
    display: flex;
    width: 100%;
    justify-content: space-around;
    padding-top: 16px;
  }
`;
export const ProfilePhoto = styled.img`
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
`;
export const ProfileName = styled.input`
  margin: 16px 0;
  /* border-radius: 10px; */

  border: none;
  border-bottom: 2px solid var(--fridge-500);
  width: 160px;
`;

//프로필 이미지 변경 사진 업로더
export const ImgContainer = styled.div`
  background-color: var(--white);
  position: relative;

  > *.upload {
    left: 78%;
    top: 78%;
    font-size: 36px;
    :active {
      color: var(--mint-600);
    }
  }
  > *.cancel {
    left: 78%;
    top: 78%;
    font-size: 40px;
    color: var(--white);
  }

  // 사진 추가 아이콘
  > *:nth-child(3) {
    left: 40%;
    top: 34%;
    font-size: 30px;
  }
  // 사진 추가 삭제 아이콘은 호버시에만 보이기
  :not(:hover) {
    > *.upload {
      display: none;
    }
    > *.cancel {
      display: none;
    }
  }
`;

export const Img = styled.img`
  object-fit: cover;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  box-shadow: var(--shadow-medium);
  object-fit: cover;
  &.dragover {
    border: 2px solid var(--fridge-300);
  }
`;

export const Input = styled.input`
  display: none;
`;
