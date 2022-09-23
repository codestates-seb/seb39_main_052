import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  isLoggedIn: false,
  userId: null,
  userName: null,
  userProfileImgPath: null,
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    //로그인 성공시
    setLoggedIn(state, action) {
      state.isLoggedIn = true;
    },

    //유저정보 저장
    setUserInfo(state, action) {
      state.userId = action.payload.userId;
      state.userName = action.payload.userName;
      state.userProfileImgPath = action.payload.userProfileImgPath;
      //로그인 성공하면 이미지링크도 슬라이스에 저장하기 추가
    },

    //로그아웃시 유저 로그인 상태 false, 저장된 정보 초기화
    setLoggedOut(state) {
      state.isLoggedIn = false;
      state.userId = null;
      state.userName = null;
      state.userProfileImgPath = null;
    },
  },
});

export default userSlice;
export const { setLoggedIn, setUserInfo, setLoggedOut } = userSlice.actions;
