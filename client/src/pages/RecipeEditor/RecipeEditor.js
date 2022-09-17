import InputList from "../../components/common/InputList/InputList";

const RecipeEditor = () => {
    
    const titlesArr = ["food", "amount"];

    return (
        <>
            <InputList titlesArr={titlesArr} />
        </>
    )
};

export default RecipeEditor;
