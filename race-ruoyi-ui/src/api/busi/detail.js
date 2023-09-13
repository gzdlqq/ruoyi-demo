import request from '@/utils/request'

// 查询场次详情列表
export function listDetail(query) {
  return request({
    url: '/busi/detail/list',
    method: 'get',
    params: query
  })
}

// 查询场次详情详细
export function getDetail(id) {
  return request({
    url: '/busi/detail/' + id,
    method: 'get'
  })
}

// 新增场次详情
export function addDetail(data) {
  return request({
    url: '/busi/detail',
    method: 'post',
    data: data
  })
}

// 修改场次详情
export function updateDetail(data) {
  return request({
    url: '/busi/detail',
    method: 'put',
    data: data
  })
}

// 删除场次详情
export function delDetail(id) {
  return request({
    url: '/busi/detail/' + id,
    method: 'delete'
  })
}
