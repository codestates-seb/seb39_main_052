import RecipeEditor from "../../components/layout/RecipeEditor/RecipeEditor";
import { PageName } from "./NewRecipeStyle";

const NewRecipe = () => {

    const editMode = 'post';

    return (
        <>
            <PageName>새 레시피 작성하기</PageName>
            <RecipeEditor editMode={editMode} />
        </>
    )
};

export default NewRecipe;