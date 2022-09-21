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
      state.userEmail = action.payload.userEmail; //나중에 서버에서 받아오는 id정보가 있다면 이메일 정보는 필요없음
      //로그인 성공하면 이미지링크도 슬라이스에 저장하기 추가
    },
  },
});

export default userSlice;
export const { setLoginSuccess } = userSlice.actions;
