import RecipeEditor from "../../components/layout/RecipeEditor/RecipeEditor";
import { PageName } from "./NewRecipeStyle";

const NewRecipe = () => {


    return (
        <>
            <PageName>새 레시피 작성하기</PageName>
            <RecipeEditor />
        </>
    )
};

export default NewRecipe;