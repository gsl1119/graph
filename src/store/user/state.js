import { getToken } from "../../utils/auth";

export default function() {
  return {
    token: getToken(),
    nickname: "",
  };
}
