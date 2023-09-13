import request from '@/utils/request'

// 查询赛事方案列表
export function listSchema(query) {
  return request({
    url: '/busi/schema/list',
    method: 'get',
    params: query
  })
}


// 查询赛事方案列表
export function querySchemaByCompetitionId(competitionId) {
  return request({
    url: `/busi/schema/querySchemaByCompetitionId/${competitionId}`,
    method: 'get'
  })
}


// 查询赛事方案详细
export function getSchema(id) {
  return request({
    url: '/busi/schema/' + id,
    method: 'get'
  })
}

// 新增赛事方案
export function addSchema(data) {
  return request({
    url: '/busi/schema',
    method: 'post',
    data: data
  })
}

// 修改赛事方案
export function updateSchema(data) {
  return request({
    url: '/busi/schema',
    method: 'put',
    data: data
  })
}

// 删除赛事方案
export function delSchema(id) {
  return request({
    url: '/busi/schema/' + id,
    method: 'delete'
  })
}


