// slice (작은 단위의 저장소) 생성 예시
import { createSlice } from "@reduxjs/toolkit";

const counterSlice = createSlice({
    name: 'counter',
    initialState: {value: 0},
    reducers: {
      up: (state, action) => {
        state.value = state.value + action.payload;
      }
    }
});

export default counterSlice;
export const {up} = counterSlice.actions;