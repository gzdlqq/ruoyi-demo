import request from '@/utils/request'

// 查询场次列表
export function listSession(query) {
  return request({
    url: '/busi/session/list',
    method: 'get',
    params: query
  })
}

// 查询场次详细
export function getSession(id) {
  return request({
    url: '/busi/session/' + id,
    method: 'get'
  })
}

// 新增场次
export function addSession(data) {
  return request({
    url: '/busi/session',
    method: 'post',
    data: data
  })
}

// 修改场次
export function updateSession(data) {
  return request({
    url: '/busi/session',
    method: 'put',
    data: data
  })
}

// 删除场次
export function delSession(id) {
  return request({
    url: '/busi/session/' + id,
    method: 'delete'
  })
}

// 编排生成场次
export function arrange(id) {
  return request({
    url: `/busi/session/arrange?competitionId=${id}`,
    method: 'get'
  })
}

// 编排生成场次
export function xunhuanSessions(id) {
  return request({
    url: `/busi/session/xunhuanSessions?competitionId=${id}`,
    method: 'get'
  })
}
