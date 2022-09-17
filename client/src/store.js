// REDUX TOOLKIT
// store: 전체 저장소
// slice: store을 이루는 작은 저장소
// reducer: reducer 함수는 dispatch에게서 받은 값에 따라 상태를 변경시키는 함수
import { configureStore } from '@reduxjs/toolkit';
import counterSlice from './features/counterSlice'; // store에 slice 연결 방법 예시

const store = configureStore({
    reducer: {
        counter: counterSlice.reducer, // store에 slice 연결 방법 예시
    }
  });
  
  export default store;