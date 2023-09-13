import request from '@/utils/request'

// 分页查询赛事列表
export function pageRace(page, raceQuery) {
  const data = raceQuery
  return request({
    'url': `/wechat/competition/pageRace?pageNum=${page.pageNum}&pageSize=${page.pageSize}`,
    'method': 'post',
    'data': data
  })
}

// 查询当前赛事列表详情
export function getRaceDetails(id) {
  return request({
    'url': `/wechat/competition/getRaceDetailsById?id=${id}`,
    'method': 'get'
  })
}


