import { createSlice } from "@reduxjs/toolkit";
import userSlice from "./userSlice";

const initialState = {
    showToast: false,
    message: "",
    mode: "notice",
};

const toastSlice = createSlice({
    name: "toast",
    initialState,
    reducers: {
        setWarningToast(state, action) {
            state.showToast = true;
            state.mode = "warning";
            state.message = action.payload.message;
        },
        setNoticeToast(state, action) {
            state.showToast = true;
            state.mode = "notice";
            state.message = action.payload.message;
        },
        closeToast(state) {
            state.showToast = false;
        },
    },
});

export default toastSlice;
export const {
    setWarningToast,
    setNoticeToast,
    closeToast,
} = toastSlice.actions;