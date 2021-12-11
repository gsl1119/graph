import { request,requestGraph } from "../../boot/axios";

//获取图谱信息
export function editKG(data) {
    return request({
      url: "/editkg",
      method: "post",
      data
    });
}

  export function fuzzFindConterm(data) {
    return request({
      url: '/fuzzfindconterm',
      method: 'post',
      data
    })
  }
  export function fuzzFindRelterm(data) {
    return request({
      url: '/fuzzfindrelterm',
      method: 'post',
      data
    })
  }
  export function submitKgCode(data) {
    return request({
      url: '/submitkgcode',
      method: 'post',
      data
    })
  }
  export function showDB(){
    return request({
      url:'/showdb',
      method:"get"
    })
  }
  export function createKGDB(data){
      return request({
        url:'/creatdb',
        method:'post',
        data
      })
  }
  export function matchSearch(data){
    return request({
      url:'/head2term',
      method:"POST",
      data
    })
  }
export function findAllDBNameAndCName(){
  return request({
    url:'/findAllDBNameAndCName',
    method:"get"
  })
}
export function checkExistDBInter(data){
  return request({
    url:'/findIsACourseDB',
    method:'post',
    data
  })
}
export function addADBInfoInter(data){
  return request({
    url:'/addADBInfo',
    method:'post',
    data
  })
}
  export function login(data) {
    return requestGraph({
      url: '/login',
      method: 'POST',
      data
    })
  }
