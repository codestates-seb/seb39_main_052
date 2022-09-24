import InputList from "../../components/common/InputList/InputList";
import { Head, ColumnHeads, Container, Title, Fridge, InputWrapper } from "./MyFridgeStyle";

const MyFridge = () => {
    const titlesArr = ["name", "amount", "date", "dDay", "note"]; //재료 입력에서 각 column의 키값 배열
    const placeholders = ["예) 계란", "예) 30알", "예) 2100/01/01", "", "기타 정보를 작성하세요"];

    return (
        <Container>
            <Title>나의 냉장고</Title>
            <Fridge>
                <ColumnHeads>
                    <Head>재료</Head>
                    <Head>양</Head>
                    <Head>유통기한</Head>
                    <Head>D-Day</Head>
                    <Head>비고</Head>
                </ColumnHeads>
                <InputWrapper>
                    <InputList titlesArr={titlesArr} placeholders={placeholders} />
                </InputWrapper>
            </Fridge>
        </Container>
    )
}

export default MyFridge