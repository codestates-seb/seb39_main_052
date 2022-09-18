import ImageUploader from "../../common/ImageUploader/ImageUploader";
import InputList from "../../common/InputList/InputList";
import ImageInputList from "../ImageInputList/ImageInputList";
import { Container, Header, Main, Input, Select, Ingredients, Steps, Portion } from "./RecipeEditorStyle";

const RecipeEditor = () => {
    
    const titlesArr = ["food", "amount"];
    const placeholders = ["예) 감자", "예) 100g"];
    const portionOptions = Array.from({length: 10}, (_, i) => i + 1);

    let imgPostApi = ``;

    // 인분 수 선택하는 드랍다운
    const SelectBox = ({ defaultValue }) => {

        const handleChange = (e) => {
            console.log(e.target.value);
        }

        return (
            <Select onChange={handleChange}>
                {portionOptions.map((option) => {
                    return (
                        <option
                            key={option}
                            value={option}
                            defaultValue={defaultValue === option}
                        >
                            {option}
                        </option>
                    )
                })}
            </Select>
        )
    }

    return (
        <Container>
            <Header>
                <Main>
                    <h1>새 레시피 작성하기</h1>
                    <Input placeholder="제목을 입력해주세요" className="large" />
                    <Portion>
                        <h2>양</h2>
                        <div>
                            <SelectBox defaultValue={1} />
                            인분
                        </div>
                    </Portion>
                </Main>
                <ImageUploader imgPostApi={imgPostApi} size={`big`}/>
            </Header>
            <Ingredients>
                <h2>재료 (계량)</h2>
                <InputList titlesArr={titlesArr} placeholders={placeholders} />
            </Ingredients>
            <Steps>
                <h2>요리 순서</h2>
                <ImageInputList />
            </Steps>
        </ Container>
    )
};

export default RecipeEditor;
