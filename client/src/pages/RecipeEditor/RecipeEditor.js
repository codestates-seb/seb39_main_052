import ImageUploader from "../../components/common/ImageUploader/ImageUploader";
import InputList from "../../components/common/InputList/InputList";

const RecipeEditor = () => {
    
    const titlesArr = ["food", "amount"];

    let imgPostApi = ``;

    return (
        <>  
            <ImageUploader imgPostApi={imgPostApi} />
            <InputList titlesArr={titlesArr} />
        </>
    )
};

export default RecipeEditor;
