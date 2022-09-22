import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import reportWebVitals from "./reportWebVitals";
import axios from "axios";

// redux toolkit 관련
import { Provider } from "react-redux";
import store from "./store";

const root = ReactDOM.createRoot(document.getElementById("root"));

root.render(
  <React.StrictMode>
    <Provider store={store}>
      <App />
    </Provider>
  </React.StrictMode>
);

axios.defaults.withCredentials = true; //refreshToken cookie를 주고받으려고 리액트 최상단에서 설정
//axios, fetch같은 브라우저의 비동기 통신 라이브러리는 별도로 설정해주지 않으면 브라우저의 쿠키 정보나 인증과 관련된 헤더를 요청에 함부로 담지 않는다.
//요청시 인증과 관련된 정보를 담을수있게 해주는 옵션이 credentials 옵션

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
