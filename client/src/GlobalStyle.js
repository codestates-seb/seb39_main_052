import { createGlobalStyle } from "styled-components";
import "./style.css";

const GlobalStyle = createGlobalStyle`
    * {
        box-sizing: border-box;
        font-family: 'GmarketSansMedium';
    }
    a {
        text-decoration: none;
        color: inherit;
    }
    ul {
      list-style: none;
      text-decoration: none;
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
      border: none;
      border-radius: 10%;
      background-color: var(--white);
      :hover {
        /* background-color: var(--mintgreen-300); */
        text-shadow: var(--text-shadow);
      }
    }
    h1 {
      margin-top: 0;
      font-size: 40px;
    }
    h2 {
      font-size: 24px;
      margin: 0 0 12px 0;
    }

    @font-face {
    font-family: 'GmarketSansMedium';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff') format('woff');
    font-weight: normal;
    font-style: normal;
    }
`;

export default GlobalStyle;
