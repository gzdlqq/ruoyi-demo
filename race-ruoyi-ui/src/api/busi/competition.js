import request from '@/utils/request'

// 查询赛事列表
export function listCompetition(query) {
  return request({
    url: '/busi/competition/list',
    method: 'get',
    params: query
  })
}

// 查询赛事详细
export function getCompetition(id) {
  return request({
    url: '/busi/competition/' + id,
    method: 'get'
  })
}

// 新增赛事
export function addCompetition(data) {
  return request({
    url: '/busi/competition',
    method: 'post',
    data: data
  })
}

// 修改赛事
export function updateCompetition(data) {
  return request({
    url: '/busi/competition',
    method: 'put',
    data: data
  })
}

// 删除赛事
export function delCompetition(id) {
  return request({
    url: '/busi/competition/' + id,
    method: 'delete'
  })
}


// 发布赛事
export function publish(id) {
  return request({
      url: '/busi/competition/publish/' + id,
    method: 'get'
  })
}
