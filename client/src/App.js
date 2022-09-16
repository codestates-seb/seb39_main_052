import './App.css';
import React, { useState } from 'react';


function App() {
  // const [message, setMessage] = useState("message");
  
  const handleSubmit = (event) => {
    event.preventDefault();
    httpGet();
  };
  
  const httpGet = () => {
    fetch("/json")
    .then((res) => {
        return res.json(); //Promise 반환
    })
    .then((json) => {
        console.log(json); // 서버에서 주는 json데이터가 출력 됨
    });
  }
  return (
    <div className="App">
      <header className="App-header">
        <button onClick={handleSubmit}>FETCH</button>
      </header>
    </div>
  );
}

export default App;