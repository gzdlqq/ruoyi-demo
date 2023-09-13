package com.ruoyi.web.controller.busi.wechat;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import com.baomidou.lock.annotation.Lock4j;
import com.baomidou.lock.executor.RedissonLockExecutor;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.busi.domain.TSign;
import com.ruoyi.busi.domain.resp.MySign;
import com.ruoyi.busi.service.ITSignService;
import com.ruoyi.busi.service.impl.TSignServiceImpl;
import com.ruoyi.common.annotation.NeedWechatLogin;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "报名")
@RestController
@RequestMapping("/wechat")
public class WechatSignController {

    @Autowired
    ITSignService signService;
    @Autowired
    private WxPayService wxService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private LockTemplate lockTemplate;

    @ApiOperation(value = "报名生成订单 且去调用微信生成支付口，返回支付需要的5个参数")
    @PostMapping("/sign")
    @NeedWechatLogin
    public AjaxResult sign(Long competitionId) {

        try {
            Object res = signService.sign(competitionId);
            AjaxResult success = AjaxResult.success(res);

            success.put("tradeNo", TSignServiceImpl.threadeNoLocal.get());

            return success;
        } finally {
            TSignServiceImpl.threadeNoLocal.remove();//防止内存泄露
        }
    }


    @ApiOperation(value = "支付成功,修改订单状态")
    @GetMapping("/paySuccess")
    @NeedWechatLogin
    public AjaxResult paySuccess(Long tradeNo) {
        System.out.println("支付成功,修改订单状态");

        signService.paySuccess(tradeNo);

        return AjaxResult.success("支付成功");
    }


    /**
     * queryMySign
     */
    @GetMapping("/queryMySign")
    @ApiOperation(value = "查询我的报名订单")
    @NeedWechatLogin
    public AjaxResult queryMySign() {
        List<MySign> list = signService.queryMySign();
        return AjaxResult.success(list);
    }


    @GetMapping("/continueToPay")
    @ApiOperation(value = "继续支付")
    @NeedWechatLogin
    public AjaxResult continueToPay(String tradeNo) {
        Object res = signService.continueToPay(tradeNo);
        return AjaxResult.success(res);
    }


    @ApiOperation(value = "支付回调通知处理")
    @PostMapping("/notify/order")
    public String parseOrderNotifyResult(@RequestBody String xmlData) throws WxPayException {
        System.out.println("支付成功微信回调");
        final WxPayOrderNotifyResult notifyResult = this.wxService.parseOrderNotifyResult(xmlData);
        // TODO 根据自己业务场景需要构造返回对象
        String outTradeNo = notifyResult.getOutTradeNo();
        signService.paySuccess(new Long(outTradeNo));

        return WxPayNotifyResponse.success("成功");
//       return WxPayNotifyResponse.fail("失败，请继续通知我");
    }


    //    @Lock4j(keys = {"#outTradeNo"}, expire = 60000, acquireTimeout = 1000)
    @ApiOperation(value = "查询订单")
    @GetMapping("/querySignBasePay")
    public WxPayOrderQueryResult queryOrder(@RequestParam(required = false) String transactionId,
                                            @RequestParam(required = false) String outTradeNo)
            throws WxPayException {
        {
//        System.out.println("进入时间:"+ DateUtil.now());

//        ThreadUtil.sleep(10000);


//        redissonClient 测试
//        RMap<String, String> map = redissonClient.getMap("testMap");
//        map.put("key", "value");
//        map.put("name", "张三");
//        map.remove("key");


            //lock4j编程式
            // 各种查询操作 不上锁
            // ...
            // 获取锁
//        final LockInfo lockInfo = lockTemplate.lock(outTradeNo, 60000L, 1000L, RedissonLockExecutor.class);
//        if (null == lockInfo) {
//            throw new RuntimeException("业务处理中,请稍后再试");
//        }
//        // 获取锁成功，处理业务
//        try {
//            System.out.println("进入时间:"+ DateUtil.now());
//            System.out.println("执行简单方法1 , 当前线程:" + Thread.currentThread().getName() + " , counter：" );
//
//            ThreadUtil.sleep(10000);
//        } finally {
//            //释放锁
//            lockTemplate.releaseLock(lockInfo);
//        }
            //结束
        }

        //查询已经支付的话，可以去尝试修改我们的订单状态
        signService.paySuccess(new Long(outTradeNo));

        return this.wxService.queryOrder(transactionId, outTradeNo);
    }


    /**
     * <pre>
     * 微信支付-申请退款
     * 详见 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_4
     * 接口链接：https://api.mch.weixin.qq.com/secapi/pay/refund
     * </pre>
     *
     * @param request 请求对象
     * @return 退款操作结果
     */
    @ApiOperation(value = "订单退款,并修改订单状态为 已拒绝")
    @PostMapping("/refund")
    public WxPayRefundResult refund(@RequestBody WxPayRefundRequest request) throws WxPayException {


        //微信退款
        return this.wxService.refund(request);
    }
}
