// 나의 냉장고 재료 상태를 저장하는 slice
import { createSlice } from "@reduxjs/toolkit";

// D-Day 계산 함수
const dDayCalculator = (expiration) => {
    const setDate = new Date(expiration); // 기준 날짜
    const now = new Date(); // 현재 날짜
    const gap = setDate - now; // 차이 계산
    const gapDay = Math.ceil(gap / (1000*60*60*24)); // 차이를 밀리세컨즈에서 데이로 변환

    return (gapDay);
} 

// 오늘 날짜 포맷 함수
const defaultDateFinder = () => {
    let today = new Date()
    return today.toISOString().split('T')[0]
}

const initialState = {
    ingredients: [{
        name: "",
        quantity: "",
        expiration: defaultDateFinder(),
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
                expiration: initialState.ingredients[0].expiration,
                note: initialState.ingredients[0].note,
            }];
        },
        deleteFrigIngrInput: (state, action) => {
            state.ingredients.splice(action.payload.index, 1);
        },
        editFrigIngredients: (state, action) => {
            if (action.payload.key === 'expiration') {
                state.ingredients[action.payload.index].dDay = dDayCalculator(action.payload.value);
            }
            state.ingredients[action.payload.index][action.payload.key] = action.payload.value;
            
        },
        clearFridge: () => {
            return initialState;
        },
        loadFridge: (state, action) => {
            state.ingredients = [...action.payload.ingredients];
            // dDay 계산 후 추가
            for (let i = 0; i < action.payload.ingredients.length; i++) {
                state.ingredients[i].dDay = dDayCalculator(action.payload.ingredients[i].expiration);
            }
        },
        sortByAlphabet: (state) => {
            let tmp = [...state.ingredients];
            tmp.sort((a, b) => {
                if (a.name < b.name) return -1;
                else if (a.name === b.name) return 0;
                else return 1;
            });
            state.ingredients = [...tmp];
        },
        sortByDate: (state) => {
            let tmp = [...state.ingredients];
            tmp.sort((a, b) => a.dDay - b.dDay);
            state.ingredients = [...tmp];
        }
    }
});

export default fridgeSlice;
export const {
    addFrigIngrInput,
    deleteFrigIngrInput,
    editFrigIngredients,
    clearFridge,
    loadFridge,
    sortByAlphabet,
    sortByDate,
} = fridgeSlice.actions;