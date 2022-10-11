// REDUX TOOLKIT
// store: 전체 저장소
// slice: store을 이루는 작은 저장소
// reducer: reducer 함수는 dispatch에게서 받은 값에 따라 상태를 변경시키는 함수
import { combineReducers, configureStore } from "@reduxjs/toolkit";
import counterSlice from "./features/counterSlice"; // store에 slice 연결 방법 예시
import recipeSlice from "./features/recipeSlice";
import imageSlice from "./features/imageSlice";
import userSlice from "./features/userSlice";
import fridgeSlice from "./features/fridgeSlice";

//for redux persist
import { persistReducer } from "redux-persist";
import thunk from "redux-thunk";
import storageSession from "redux-persist/lib/storage/session";
import toastSlice from "./features/toastSlice";

// // 기존코드
// const store = configureStore({
//   reducer: {
//     counter: counterSlice.reducer, // store에 slice 연결 방법 예시
//     recipe: recipeSlice.reducer,
//     images: imageSlice.reducer,
//     user: userSlice.reducer,
//     fridge: fridgeSlice.reducer,
//   },
//   middleware: (getDefaultMiddleware) =>
//     getDefaultMiddleware({
//       serializableCheck: false,
//     }), //기본적인 미들웨어 thunk, immutableStateInvariant, serializableStateInvariant가 포함 가져오겠다
// });

// export default store;

// //redux persist 이후 코드
const rootReducer = combineReducers({
  counter: counterSlice.reducer,
  recipe: recipeSlice.reducer,
  images: imageSlice.reducer,
  user: userSlice.reducer,
  fridge: fridgeSlice.reducer,
  toast: toastSlice.reducer,
});

const persistConfig = {
  key: "root",
  storage: storageSession,
  whitelist: ["user"],
};

const persistedReducer = persistReducer(persistConfig, rootReducer);

const store = configureStore({
  reducer: persistedReducer,
  devTools: process.env.NODE_ENV !== "production",
  middleware: [thunk],
});

export default store;
