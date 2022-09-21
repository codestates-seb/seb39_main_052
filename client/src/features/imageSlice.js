// 메인 이미지와 요리 순서 이미지를 저장하는 slice
import { createSlice } from "@reduxjs/toolkit";

const initialState = [null, null];

const imageSlice = createSlice({
    name: 'images',
    initialState,
    reducers: {
        addElement: (state, action) => {
            return state = [...state, null];
        },
        addImage: (state, action) => {
            state[action.payload.index+1] = action.payload.image;
        },
        addMainImage: (state, action) => {
            state[0] = action.payload.image;
        },
        deleteMainImage: (state, action) => {
            state[0] = null;
        },
        deleteImage: (state, action) => {
            state[action.payload.index+1] = null;
        },
        clearImages: () => {
            return initialState;
        },
    },
});

export default imageSlice;
export const { addElement, addImage, addMainImage, deleteImage, deleteMainImage, clearImages } = imageSlice.actions;