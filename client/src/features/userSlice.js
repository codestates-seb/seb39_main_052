import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  isLoggedIn: false,
  userId: null,
  userEmail: null,
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    //로그인 성공시
    setLoginSuccess(state, action) {
      state.isLoggedIn = true;
      state.userId = action.payload.userId;
      //로그인 성공하면 이미지링크도 슬라이스에 저장하기 추가
    },

    //로그아웃시 상태 초기화
    setLoggedOut(state) {
      state.isLoggedIn = false;
      state.userId = null;
    },
  },
});

export default userSlice;
export const { setLoginSuccess, setLoggedOut } = userSlice.actions;
