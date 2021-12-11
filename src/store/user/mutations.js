import { getToken } from '../../utils/auth'

function getDefaultState () {
  return {
    token: getToken(),
    nickname: "",
  }
}

export function RESET_STATE (state) {
  Object.assign(state, getDefaultState())
}

export function SET_TOKEN (state, token) {
  state.token = token
}

export function SET_NICKNAME (state, nickname) {
  state.nickname = nickname
}