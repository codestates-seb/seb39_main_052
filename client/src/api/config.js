// import axios from "axios";
// import { useSelector, useDispatch } from "react-redux";
// import userSlice, { setLoggedIn } from "../features/userSlice";

// //axios 인스턴스 만드기
// export const instance = axios.create();

// // const dispatch = useDispatch();
// // const userToken = useSelector((state) => {
// //   return state.user.userToken;
// // });

// let store;
// export const injectStore = (_store) => {
//   store = _store;
// };

// console.log("store", store);
// const userToken = store.getState().user.userToken;

// //요청 인터셉터 - 요청 헤더에 토큰 정보 넣기
// instance.interceptors.request.use(
//   (config) => {
//     //리덕스 슬라이스에 액세스 토큰 있으면
//     if (userToken) {
//       config.headers["authorization"] = `Bearer ${userToken}`;
//       return config;
//     }
//     return config;
//   },
//   (error) => {
//     console.log(error, "error");
//     return Promise.reject(error);
//   }
// );

// //응답 인터셉터 - 가지고있는 액세스토큰으로 refresh요청 401 실패시
// instance.interceptors.response.use(
//   (response) => {
//     return response;
//   },
//   (error) => {
//     const { response: errorResponse } = error;
//     const originalRequest = error.config;

//     //인증 에러 발생시 토큰 재발급하고 원래 보내려고하던 요청 다시 보내기
//     if (errorResponse.status === 401) {
//       return reissueTokenReattemptRequest(error);
//     }

//     return Promise.reject(error);
//   }
// );

// let isTokenRefreshing = false;
// let refreshSubscribers = [];

// const addRefreshSubscriber = (callback) => {
//   refreshSubscribers.push(callback);
// };

// const reissueTokenReattemptRequest = (error) => {
//   const { response: errorResponse } = error;

//   const retryOriginalRequest = new Promise((resolve) => {
//     addRefreshSubscriber((userToken) => {
//       errorResponse.config.headers["Authorization"] = "Bearer " + userToken;
//       resolve(instance(errorResponse.config));
//     });
//   });

//   if (!isTokenRefreshing) {
//     isTokenRefreshing = true;
//     axios.post("/api/auth/refresh").then((response) => {
//       const ACCESS_TOKEN = response.headers["access-token"];
//       instance.defaults.headers["authorization"] = ACCESS_TOKEN;
//       //   dispatch(setLoggedIn({ userToken: ACCESS_TOKEN })); //refesh로 새로받아온 액세스 토큰 리덕스에도 저장하기
//       store.dispatch(
//         userSlice.actions.setLoggedIn({ userToken: ACCESS_TOKEN })
//       );
//       refreshSubscribers.map((callback) => callback(ACCESS_TOKEN));
//       refreshSubscribers = [];
//       isTokenRefreshing = false;
//     });
//   }
//   return retryOriginalRequest;
// };
