import { createSlice } from "@reduxjs/toolkit";
import { PURGE } from "redux-persist"; //for store purge

const initialState = {
  isLoggedIn: false,
  userToken: null, //액세스 토큰
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
      //새로고침시에도 안날라가게 액세스 토큰 저장
      state.userToken = action.payload.userToken;
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
      state.userToken = null; //액세스 토큰 저장된 정보도 초기화
    },
    //로그아웃시 초기상태로 store purge 작업
    extraReducers: (builder) => {
      builder.addCase(PURGE, () => initialState);
    },
  },
});

export default userSlice;
export const { setLoggedIn, setUserInfo, setLoggedOut } = userSlice.actions;
