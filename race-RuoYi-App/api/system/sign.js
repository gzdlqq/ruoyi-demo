import request from '@/utils/request'

// 分页查询赛事列表
export function paySuccess(tradeNo) {

  return request({
    'url': `/wechat/paySuccess`,
    'method': 'get',
    'params': {tradeNo}
  })
}

// 查询我的报名
export function queryMySign() {
  return request({
    'url': `/wechat/queryMySign`,
    'method': 'get'
  })
}


//查询报名是否支付
export function querySignBasePay(tradeNo) {
  return request({
    'url': `/wechat/querySignBasePay`,
    'method': 'get',
    'params': {outTradeNo:tradeNo}
  })
}
