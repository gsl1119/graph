import { Cookies } from 'quasar'

const TokenKey = 'jxpt_token'

export function getToken () {
  return Cookies.get(TokenKey) || ''
}

export function setToken (token) {
  Cookies.set(TokenKey, token, {path: "/"})
}

export function removeToken () {
  Cookies.remove(TokenKey, {path: "/"})
}
