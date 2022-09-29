// 작성한/수정할 레시피 상태를 저장하는 slice
import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    id: "",
    member: {
      id: "",
      name: "",
      profileImagePath: "",
    },
    createdAt: null,
    heartCounts: "",
    view: "",
    title: "",
    portion: "1",
    time: "",
    imageInfo: {
      idx: 0,
      imagePath: "",
      isUpdated: "N",
    },
    ingredients: [{
      name: "",
      quantity: ""
    }],
    steps: [{
      sequence: 1,
      content: "",
      imageInfo: {
        idx: 1,
        imagePath: "",
        isUpdated: "N",
      },
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
      setMainImage: (state, action) => {
        state.imageInfo.imagePath = action.payload.mainImage;
      },
      editMainImage: (state, action) => {
        state.imageInfo.imagePath = action.payload.mainImage;
        state.imageInfo.isUpdated = "Y";
      },
      deleteMainImg: (state, aciton) => {
        state.imageInfo.imagePath = "";
        state.imageInfo.isUpdated = "N";
      },
      setPortion: (state, action) => {
        state.portion = action.payload.portion;
      },
      setTime: (state, action) => {
        state.time = action.payload.time;
      },
      addIngrInput: (state, action) => {
        state.ingredients = [...state.ingredients, {
          name: initialState.ingredients[0].name,
          quantity: initialState.ingredients[0].quantity
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
          sequence: action.payload.index+2, //이전 input index (0) 기준 + 1, sequence는 1부터 시작하니 총 + 2
          content: initialState.steps[0].content,
          imageInfo: initialState.steps[0].imageInfo,
        }];
      },
      deleteStepsInput: (state, action) => {
        state.steps.splice(action.payload.index, 1);
      },
      editSteps: (state, action) => {
        state.steps[action.payload.index][action.payload.key] = action.payload.value;
      },
      setStepImage: (state, action) => {
        state.steps[action.payload.index].imageInfo.imagePath = action.payload.imagePath;
      },
      editStepImage: (state, action) => {
        state.steps[action.payload.index].imageInfo.imagePath = action.payload.imagePath;
        state.steps[action.payload.index].imageInfo.isUpdated = "Y";
      },
      deleteStepImage: (state, action) => {
        state.steps[action.payload.index].imageInfo.imagePath = "";
        state.steps[action.payload.index].imageInfo.isUpdated = "N";
      },
      loadRecipe: (state, action) => {
        state.id = action.payload.recipeId;
        state.member.name = action.payload.memberName;
        state.member.id = action.payload.memberId;
        state.member.profileImagePath = action.payload.profileImagePath;
        state.createdAt = action.payload.createdAt;
        state.heartCounts = action.payload.heartCounts;
        state.view = action.payload.view;
        state.title = action.payload.title;
        state.portion = action.payload.portion;
        state.time = action.payload.time;
        state.imageInfo.imagePath = action.payload.mainImage;
        state.ingredients = [...action.payload.ingredients];
        state.steps = [...action.payload.steps];
      },
      clearRecipe: () => {
        return initialState;
      },
    }
});

export default recipeSlice;
export const { 
  setId,
  // POST, PATCH
  setTitle,
  setMainImage,
  editMainImage,
  deleteMainImg,
  setPortion,
  setTime,
  addIngrInput,
  deleteIngrInput,
  editIngredients,
  addStepsInput,
  deleteStepsInput,
  editSteps,
  setStepImage,
  editStepImage,
  deleteStepImage,
  // GET
  loadRecipe, 
  clearRecipe 
} = recipeSlice.actions;