import React from 'react';
import GlobalStyle from './GlobalStyle';
import styled from 'styled-components';

import RecipeEditor from './components/layout/RecipeEditor/RecipeEditor';

function App() {

  return (
    <>
      {/* Global Styles는 Router 안, 컴포넌트 상단에 위치  */}
      <GlobalStyle />
      <RecipeEditor />
    </>
  );
}

// const Test = styled.div`
//   color: var(--red-500);
//   box-shadow: var(--shadow-high);
// `

export default App;