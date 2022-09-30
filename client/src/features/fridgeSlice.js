// 나의 냉장고 재료 상태를 저장하는 slice
import { createSlice } from "@reduxjs/toolkit";


const dDayCalculator = (date) => {
    const setDate = new Date(date); // 기준 날짜
    const now = new Date(); // 현재 날짜
    const gap = setDate - now; // 차이 계산
    const gapDay = Math.ceil(gap / (1000*60*60*24)); // 차이를 밀리세컨즈에서 데이로 변환

    return (gapDay);
} 

const initialState = {
    ingredients: [{
        name: "",
        quantity: "",
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
                quantity: initialState.ingredients[0].quantity,
                dDay: initialState.ingredients[0].dDay,
                date: initialState.ingredients[0].date,
                note: initialState.ingredients[0].note,
            }];
        },
        deleteFrigIngrInput: (state, action) => {
            state.ingredients.splice(action.payload.index, 1);
        },
        editFrigIngredients: (state, action) => {
            if (action.payload.key === 'date') {
                state.ingredients[action.payload.index].dDay = dDayCalculator(action.payload.value);
            }
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