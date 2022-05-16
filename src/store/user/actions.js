import { login } from "../../api/common/graph";
import { setToken, removeToken } from "../../utils/auth";

export function userLogin({ commit }, userInfo) {
  const { username, password } = userInfo;
  return new Promise((resolve, reject) => {
    login({ username, password })
      .then(response => {
        const { data } = response;
        console.log(data);
        commit("SET_NICKNAME", data.nickname);
        commit("SET_TOKEN", data.token);
        setToken(data.token);
        resolve();
      })
      .catch(error => {
        reject(error);
      });
  });
}


export function userLogout({ commit }, state) {
  return new Promise((resolve, reject) => {
    removeToken();
    resolve();
  });
}
