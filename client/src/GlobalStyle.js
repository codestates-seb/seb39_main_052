import { createGlobalStyle } from "styled-components";
import reset from 'styled-reset'

const GlobalStyle = createGlobalStyle`

    * {
        box-sizing: border-box;
        font-family: 'GmarketSansMedium';
    }
    a {
        text-decoration: none;
        color: inherit;
    }
    // 아이폰 safari 환경에서 input 요소에 입력이 안되는 경우가 발생할 수도 있다고 하여 일단 불러옴
    input, textarea { 
      -moz-user-select: auto;
      -webkit-user-select: auto;
      -ms-user-select: auto;
      user-select: auto;
    }
    input:focus {
      outline: none;
    }
    button {
      cursor: pointer;
    }

    @font-face {
    font-family: 'GmarketSansMedium';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff') format('woff');
    font-weight: normal;
    font-style: normal;
    }
`;

export default GlobalStyle;