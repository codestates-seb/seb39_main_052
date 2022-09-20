import RecipeEditor from "../../components/layout/RecipeEditor/RecipeEditor";
import { PageName } from "./EditRecipeStyle";

const EditRecipe = () => {

    const editMode = 'patch';

    return (
        <>  
            <PageName>레시피 수정하기</PageName>
            <RecipeEditor editMode={editMode} />
        </>
    )
};

export default EditRecipe;