import React from 'react';
import GlobalStyle from './GlobalStyle';
import styled from 'styled-components';
import "./style.css";

function App() {

  return (
    <>
      {/* Global Styles는 Router 안, 컴포넌트 상단에 위치  */}
      <GlobalStyle />
      <Test> 나만의 냉장고 </Test>
      <div> My Fridge </div>
    </>
  );
}

const Test = styled.div`
  color: var(--primary-900);
  background-color: var(--blue-500);
`

export default App;