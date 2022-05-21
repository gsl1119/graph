import axios from "axios";

export const request = axios.create({
  baseURL: "https://knowledge.darryllin.cn/", // 线上服务地址
  // baseURL: "http://anrunlu-test.com/",   // 后端测试地址
  // baseURL: "http://182.92.231.190:9000/",
  timeout: 150000
});
export const requestGraph = axios.create({
  // baseURL: "http://47.105.219.223:8090",
  //  baseURL: "http://localhost:8888",
   baseURL: "http://101.43.7.172:8080",
   timeout: 50000
});

// export default ({
//   app,
//   router,
//   store
// }) => {
//   // Response interceptor
//   request.interceptors.response.use(
//     res => {
//       return res;kill
//     },
//     err => {
//       if (err.response.status === 401) {
//         Notify.create({
//           message: err.response.data.error,
//           type: "negative",
//           position: "bottom"
//         });
//         removeToken();
//         router.push("/login");
//       } else if (err.response.status === 500) {
//         Notify.create({
//           message: "服务器异常，请稍后再试",
//           color: "red",
//           icon: "error",
//           position: "bottom"
//         });
//       } else if (err.response.status === 404) {
//         Notify.create({
//           message: err.response.data.error,
//           type: "negative",
//           icon: "error",
//           position: "bottom"
//         });
//       } else {
//         Notify.create({
//           message: err.response.data.error,
//           type: "negative",
//           icon: "error",
//           position: "bottom",
//           timeout: 300
//         });
//       }

//       return Promise.reject(err);
//     }
//   );

  // Request interceptor
//   request.interceptors.request.use(
//     config => {
//       // do something before request is sent
//       if (store.getters["user/token"]) {
//         // let each request carry token
//         config.headers.Authorization = "Bearer " + getToken() || "";
//       }
//       return config;
//     },
//     error => {
//       // do something with request error
//       console.log(error); // for debug
//       return Promise.reject(error);
//     }
//   );
// };
