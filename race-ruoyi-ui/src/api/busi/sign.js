import request from '@/utils/request'

// 查询sign列表
export function listSign(query) {
  return request({
    url: '/busi/sign/list',
    method: 'get',
    params: query
  })
}

// 查询sign详细
export function getSign(id) {
  return request({
    url: '/busi/sign/' + id,
    method: 'get'
  })
}

// 新增sign
export function addSign(data) {
  return request({
    url: '/busi/sign',
    method: 'post',
    data: data
  })
}

// 修改sign
export function updateSign(data) {
  return request({
    url: '/busi/sign',
    method: 'put',
    data: data
  })
}

// 删除sign
export function delSign(id) {
  return request({
    url: '/busi/sign/' + id,
    method: 'delete'
  })
}

//审核拒绝
export function handleReject(id) {
  return request({
    url: '/busi/sign/reject?signId='+id,
    method: 'put'
  })
}
//审核通过
export function handleAccess(id) {
  return request({
    url: '/busi/sign/access?signId='+id,
    method: 'put'
  })
}
