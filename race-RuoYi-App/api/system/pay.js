import request from '@/utils/request'

// 生成业务订单 并生成支付订单
export function createPayOrder(competitionId) {
  return request({
    'url': `/wechat/sign`,
    'method': 'post',
    'params': {competitionId:competitionId}
  })
}

//继续支付
export function continueToPay(tradeNo) {
  return request({
    'url': `/wechat/continueToPay`,
    'method': 'get',
    'params': {tradeNo:tradeNo}
  })
}


