// 나의 냉장고 재료 상태를 저장하는 slice
import { createSlice } from "@reduxjs/toolkit";

// const initialState = {
//     ingredients: [{
//         name: "자두",
//         amount: "1개",
//         date: "2022-10-10",
//         dDay: "14",
//         note: "",
//     },{
//         name: "계란",
//         amount: "30개",
//         date: "2022-10-02",
//         dDay: "6",
//         note: "",
//     },{
//         name: "떡",
//         amount: "1개",
//         date: "2022-09-24",
//         dDay: "-2",
//         note: "",
//     },{
//         name: "",
//         amount: "",
//         date: "",
//         dDay: "",
//         note: "",
//     },],
     
// }

const initialState = {
    ingredients: [{
        name: "",
        amount: "",
        date: "",
        dDay: "",
        note: "",
    },],
     
}

const fridgeSlice = createSlice({
    name: 'fridge',
    initialState,
    reducers: {
        addFrigIngrInput: (state, action) => {
            state.ingredients = [...state.ingredients, {
                name: initialState.ingredients[0].name,
                amount: initialState.ingredients[0].amount,
                dDay: initialState.ingredients[0].dDay,
                date: initialState.ingredients[0].date,
                note: initialState.ingredients[0].note,
            }];
        },
        deleteFrigIngrInput: (state, action) => {
            state.ingredients.splice(action.payload.index, 1);
        },
        editFrigIngredients: (state, action) => {
            state.ingredients[action.payload.index][action.payload.key] = action.payload.value;
        },
        clearFridge: () => {
            return initialState;
        },
        setDDay: (state, action) => {
            state.ingredients[action.payload.index][action.payload.key] = action.payload.value;
        },
    }
});

export default fridgeSlice;
export const {
    addFrigIngrInput,
    deleteFrigIngrInput,
    editFrigIngredients,
    clearFridge,
    setDDay
} = fridgeSlice.actions;