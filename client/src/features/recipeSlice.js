// 작성한/수정할 레시피 상태를 저장하는 slice
import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    id: null,
    memberName: null,
    memberId: null,
    createdAt: null,
    title: "",
    // imagePath: "",
    portion: "1",
    time: "",
    ingredients: [{
      sequence: 1,
      name: "",
      amount: ""
    }],
    steps: [{
      sequence: 1,
      content: "",
      imagePath: "",
    }],
}

const recipeSlice = createSlice({
    name: 'recipe',
    initialState,
    reducers: {
      setId: (state, action) => {
        state.id = action.payload.id;
      },
      setTitle: (state, action) => {
        state.title = action.payload.title;
      },
      setPortion: (state, action) => {
        state.portion = action.payload.portion;
      },
      setTime: (state, action) => {
        state.time = action.payload.time;
      },
      addIngrInput: (state, action) => {
        state.ingredients = [...state.ingredients, {
          sequence: action.payload.index+2,
          name: initialState.ingredients[0].name,
          amount: initialState.ingredients[0].amount
        }];
      },
      deleteIngrInput: (state, action) => {
        state.ingredients.splice(action.payload.index, 1);
      },
      editIngredients: (state, action) => {
        state.ingredients[action.payload.index][action.payload.key] = action.payload.value;
      },
      addStepsInput: (state, action) => {
        state.steps = [...state.steps, {
          sequence: action.payload.index+2,
          content: initialState.steps[0].content
        }];
      },
      deleteStepsInput: (state, action) => {
        state.steps.splice(action.payload.index, 1);
      },
      editSteps: (state, action) => {
        state.steps[action.payload.index][action.payload.key] = action.payload.value;
      },
      loadRecipe: (state, action) => {
        state.id = action.payload.recipeId;
        state.memberName = action.payload.memberName;
        state.memberId = action.payload.memberId;
        state.createdAt = action.payload.createdAt;
        state.title = action.payload.title;
        state.portion = action.payload.portion;
        state.time = action.payload.time;
      },
      clearRecipe: () => {
        return initialState;
      },
    }
});

export default recipeSlice;
export const { 
  setId,
  setTitle,
  setImagePath,
  setPortion,
  setTime,
  addIngrInput, 
  deleteIngrInput, 
  editIngredients, 
  addStepsInput,
  deleteStepsInput,
  editSteps, 
  clearRecipe 
} = recipeSlice.actions;