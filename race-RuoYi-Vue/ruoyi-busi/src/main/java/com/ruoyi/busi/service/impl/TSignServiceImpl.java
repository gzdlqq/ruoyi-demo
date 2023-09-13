package com.ruoyi.busi.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import com.baomidou.lock.executor.RedissonLockExecutor;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.ruoyi.busi.aspectj.NeedWechatLoginAspect;
import com.ruoyi.busi.domain.TSign;
import com.ruoyi.busi.domain.resp.MySign;
import com.ruoyi.busi.mapper.TSignMapper;
import com.ruoyi.busi.service.ITMemberUserService;
import com.ruoyi.busi.service.ITSignService;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.busi.domain.TCompetition;
import com.ruoyi.busi.domain.TMemberUser;
import com.ruoyi.busi.domain.TRaceSchema;
import com.ruoyi.busi.service.ITCompetitionService;
import com.ruoyi.busi.service.ITRaceSchemaService;
import com.ruoyi.common.config.RabbitMQConfig;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import me.chanjar.weixin.common.error.WxErrorException;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * signService业务层处理
 *
 * @author shenzhao
 * @date 2023-04-16
 */
@Service
public class TSignServiceImpl implements ITSignService {
    @Autowired
    private TSignMapper tSignMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private ITMemberUserService memberUserService;

    /**
     * 查询sign
     *
     * @param id sign主键
     * @return sign
     */
    @Override
    public TSign selectTSignById(Long id) {
        return tSignMapper.selectTSignById(id);
    }

    /**
     * 查询sign列表
     *
     * @param tSign sign
     * @return sign
     */
    @Override
    public List<TSign> selectTSignList(TSign tSign) {
        return tSignMapper.selectTSignList(tSign);
    }

    /**
     * 新增sign
     *
     * @param tSign sign
     * @return 结果
     */
    @Override
    public int insertTSign(TSign tSign) {
        return tSignMapper.insertTSign(tSign);
    }

    /**
     * 修改sign
     *
     * @param tSign sign
     * @return 结果
     */
    @Override
    public int updateTSign(TSign tSign) {
        return tSignMapper.updateTSign(tSign);
    }

    /**
     * 批量删除sign
     *
     * @param ids 需要删除的sign主键
     * @return 结果
     */
    @Override
    public int deleteTSignByIds(Long[] ids) {
        return tSignMapper.deleteTSignByIds(ids);
    }

    /**
     * 删除sign信息
     *
     * @param id sign主键
     * @return 结果
     */
    @Override
    public int deleteTSignById(Long id) {
        return tSignMapper.deleteTSignById(id);
    }


    @Autowired
    SnowflakeGenerator snowflakeGenerator;

    @Autowired
    ITCompetitionService competitionService;
    @Autowired
    ITRaceSchemaService schemaService;

    @Autowired
    private WxPayService wxService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private LockTemplate lockTemplate;

    public static ThreadLocal<Long> threadeNoLocal = new ThreadLocal();

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Transactional  //必须有！！！！！！！！！！！！
    @Override
    public Object sign(Long competitionId) {
        System.out.println(this);
        Object res = null;

        TCompetition tCompetition = competitionService.selectTCompetitionById(competitionId);
        TRaceSchema tRaceSchema = schemaService.selectTRaceSchemaById(tCompetition.getRaceSchemaId());
        TMemberUser tMemberUser = NeedWechatLoginAspect.wechatLoginThreadLocal.get(); //从 threadLocal中获取用户


        TSign tSign = new TSign();
        tSign.setSignTime(new Date());//报名时间
        tSign.setCompetitionId(competitionId);//赛事id
        tSign.setCompetitionName(tCompetition.getName()); //注意冗余的艺术 ，后期查询会大大提升效率，牺牲改 新增，提升查询
        tSign.setUserId(tMemberUser.getId());//报名用户id
        tSign.setPlayerName(tMemberUser.getNickname());
        tSign.setStatus("0"); //待支付， 如果有免费的 还要考虑下免费怎么做~
        tSign.setTotalFee(tRaceSchema.getPrice().multiply(BigDecimal.valueOf(100)));//总金额  单位是分

        Long tradeNo = snowflakeGenerator.next();//业务订单号
        System.out.println("订单号" + tradeNo);
        tSign.setTradeNo(tradeNo);

        tSignMapper.insertTSign(tSign);

        //调用微信支付接口
        //    request.setBody("商品描述");
        //    request.setOutTradeNo("商户订单号");
        //    request.setTotalFee(100); // 订单总金额，单位为分
        //    request.setSpbillCreateIp("123.12.12.123");
        //    request.setNotifyUrl("接收微信支付异步通知回调地址");
        //    request.setTradeType("JSAPI");
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setBody(tSign.getCompetitionName());//描述
        request.setOutTradeNo(String.valueOf(tradeNo));
        request.setTotalFee(tSign.getTotalFee().intValue()); // 订单总金额，单位为分
        request.setSpbillCreateIp("123.12.12.123");
        request.setNotifyUrl( "http://yvi2jp.natappfree.cc/wechat/notify/order");
        request.setTradeType("JSAPI");
        request.setOpenid(tMemberUser.getOpenId()); //openid
        Date date = new Date();
        DateTime offset = DateUtil.offset(date, DateField.MINUTE, 5);
        System.out.println("生成支付订单的时间：" + DateUtil.format(offset, "yyyy-MM-dd HH:mm:ss"));
        request.setTimeExpire(DateUtil.format(offset, "yyyyMMddHHmmss"));
        try {
            res = wxService.createOrder(request);
        } catch (WxPayException e) {
            throw new ServiceException(e.getErrCodeDes());
        }

        threadeNoLocal.set(tradeNo);

        //存储到redis中
        redisCache.setCacheObject("pay:" + tradeNo, res, 15, TimeUnit.MINUTES);

//        rabbitTemplate.convertAndSend(RabbitMQConfig.DELAYED_CLOSE_ORDER_EXCHANGE_NAME, RabbitMQConfig.DELAYED_CLOSE_ORDER_ROUTING_KEY,tradeNo );
        rabbitTemplate.convertAndSend(RabbitMQConfig.DELAYED_CLOSE_ORDER_EXCHANGE_NAME, RabbitMQConfig.DELAYED_CLOSE_ORDER_ROUTING_KEY, tradeNo,
                correlationData -> {
                    //在队列中多久之后 才允许被消费者消费
                    correlationData.getMessageProperties().setDelay(5*60*1000);
                    return correlationData;
                });
        return res;
    }

//    @Transactional(isolation = Isolation.READ_COMMITTED) //验证数据库的隔离性  不要在这加
    @Override
    public void paySuccess(Long tradeNo) {
        //双重检查 第一次检查
        //查询出 之前生成的订单
        TSign tSign1 = tSignMapper.selectTSignByTradeNo(tradeNo + "");
        if (tSign1 == null) {
            throw new ServiceException("订单为空！！！");
        }
        //等于 已经支付，就不用重复执行了
        if ("1".equals(tSign1.getStatus())) {
            return;
        }

        {
//            System.out.println("准备睡觉");
//            ThreadUtil.sleep(5000);


            final LockInfo lockInfo = lockTemplate.lock(tradeNo + "", 60000L, 2000L, RedissonLockExecutor.class);
            if (null == lockInfo) {
                throw new RuntimeException("业务处理中,请稍后再试");
            }
            // 获取锁成功，处理业务
            try {
                //双重检查锁 二次检查
                //查询出 之前生成的订单
                TSign tSign2 = tSignMapper.selectTSignByTradeNo(tradeNo + "");

                //等于 已经支付，就不用重复执行了
                if ("1".equals(tSign2.getStatus())) {
                    return;
                }

//                this.xuyaoshiwu(tSign2);
                // 想办法 获取 this的 动态代理对象
                TSignServiceImpl proxyBean = SpringUtil.getBean(TSignServiceImpl.class);
                System.out.println(proxyBean.getClass());
                proxyBean.xuyaoshiwu(tSign2);

            } finally {
                //释放锁
                lockTemplate.releaseLock(lockInfo);
            }
        }
    }
//    @Transactional //此处加的事务注解 由于是方法内部 直接this自调用 是直接失效的
   @Transactional  //如果想 方法内部自调用，必须是 this的代理对象 来调用他才生效
    public void xuyaoshiwu(TSign tSign2) {
        System.out.println("看看谁进来执行 修改订单状态的业务了~~~~~~");

        if (tSign2 == null) {
            throw new ServiceException("订单为空！！！");
        }


        //修改订单状态 为支付成功 待审核 / 后续可能会有更多的复杂逻辑
        tSign2.setStatus("1");
        this.tSignMapper.updateTSign(tSign2);
    }

    @Override
    public List<MySign> queryMySign() {
        TMemberUser tMemberUser = NeedWechatLoginAspect.wechatLoginThreadLocal.get();
        Long id = tMemberUser.getId();//当前登录用户

        //查询当前登录用户 的 报名订单 （tip:  其实这个就是最简单的数据权限）
        List<MySign> list = tSignMapper.queryMySign(id);


        return list;
    }

    @Override
    public Object continueToPay(String tradeNo) {
        Object res = null;
        //查询出来 之前的订单
        TSign tSign = tSignMapper.selectTSignByTradeNo(tradeNo);

        TMemberUser tMemberUser = NeedWechatLoginAspect.wechatLoginThreadLocal.get(); //从 threadLocal中获取用户


        /**
         * 错误示例
         *      拿着之前的业务订单号 重新 生成支付订单 ， 会失败的！
         */
//        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
//        request.setBody(tSign.getCompetitionName());//描述
//        request.setOutTradeNo(String.valueOf(tradeNo));
//        request.setTotalFee(tSign.getTotalFee().intValue()); // 订单总金额，单位为分
//        request.setSpbillCreateIp("123.12.12.123");
//        request.setNotifyUrl("接收微信支付异步通知回调地址");
//        request.setTradeType("JSAPI");
//        request.setOpenid(tMemberUser.getOpenId()); //openid
//        try {
//            res = wxService.createOrder(request);
//        } catch (WxPayException e) {
//            throw new ServiceException(e.getErrCodeDes());
//        }

        //存储到redis中
        res = redisCache.getCacheObject("pay:" + tradeNo);

        if (res == null) {
            throw new ServiceException("订单已经过期了！");
        }

        return res;
    }

    @Transactional
    @Override
    public void reject(Long signId) throws WxPayException, WxErrorException {

        //查询订单
        TSign tSign = this.tSignMapper.selectTSignById(signId);

        //修改订单状态
        tSign.setStatus("4");//拒绝
        this.tSignMapper.updateTSign(tSign);

        //进行微信退款，如果失败了，需要抛出异常，要保证订单状态还是 待审核
        WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
        //组装退款参数
        /**
         * {
         *   "outTradeNo": "1651243542758031360",
         *   "outRefundNo":"1651243542758031360",
         *   "refundAccount": "REFUND_SOURCE_RECHARGE_FUNDS",
         *   "refundDesc": "描述信息",
         *   "totalFee": 1,
         *   "refundFee": 1
         * }
         */
        wxPayRefundRequest.setOutTradeNo(tSign.getTradeNo()+"");
        wxPayRefundRequest.setOutRefundNo(tSign.getTradeNo()+"");
        wxPayRefundRequest.setRefundAccount("REFUND_SOURCE_RECHARGE_FUNDS");
        wxPayRefundRequest.setRefundDesc("拒绝的理由");
        wxPayRefundRequest.setTotalFee(tSign.getTotalFee().intValue());
        wxPayRefundRequest.setRefundFee(tSign.getTotalFee().intValue());

        WxPayRefundResult refund = this.wxService.refund(wxPayRefundRequest);
//        System.out.println(refund);
        if(!"SUCCESS".equals(refund.getResultCode())){
            throw new ServiceException("审批拒绝失败");
        }


        // =========== 发送小程序消息 =================
        Long userId = tSign.getUserId();
        TMemberUser tMemberUser = memberUserService.selectTMemberUserById(userId);


        WxMaSubscribeMessage wxMaSubscribeMessage = new WxMaSubscribeMessage();
//        wxMaSubscribeMessage.setMiniprogramState("developer");
        ArrayList<WxMaSubscribeMessage.MsgData> list = new ArrayList<>();

        WxMaSubscribeMessage.MsgData msgData1 = new WxMaSubscribeMessage.MsgData();
        msgData1.setName("thing1");
        msgData1.setValue(tSign.getCompetitionName());

        WxMaSubscribeMessage.MsgData msgData2 = new WxMaSubscribeMessage.MsgData();
        msgData2.setName("phrase7");
        msgData2.setValue("审核拒绝");
        list.add(msgData1);
        list.add(msgData2);

        wxMaSubscribeMessage.setData(list);//绑定的数据
        wxMaSubscribeMessage.setLang(WxMaConstants.MiniProgramLang.ZH_CN);
//        wxMaSubscribeMessage.setPage();
        wxMaSubscribeMessage.setTemplateId("5U67v-dtQCNUzWoJhHL-vbNyzXDwwKEoo7ZKEVE7JRI");
        wxMaSubscribeMessage.setToUser(tMemberUser.getOpenId());//openid
        //发送订阅消息
        wxMaService.getMsgService().sendSubscribeMsg(wxMaSubscribeMessage);
        // =========== 发送小程序消息 =================
    }

    @Override
    public void access(Long signId) throws WxErrorException {

        //查询订单
        TSign tSign = this.tSignMapper.selectTSignById(signId);
        //修改订单状态
        tSign.setStatus("2");//通过
        this.tSignMapper.updateTSign(tSign);

        Long userId = tSign.getUserId();
        TMemberUser tMemberUser = memberUserService.selectTMemberUserById(userId);

        // =========== 发送小程序消息 =================
        WxMaSubscribeMessage wxMaSubscribeMessage = new WxMaSubscribeMessage();
//        wxMaSubscribeMessage.setMiniprogramState("developer");
        ArrayList<WxMaSubscribeMessage.MsgData> list = new ArrayList<>();

        WxMaSubscribeMessage.MsgData msgData1 = new WxMaSubscribeMessage.MsgData();
        msgData1.setName("thing1");
        msgData1.setValue(tSign.getCompetitionName());

        WxMaSubscribeMessage.MsgData msgData2 = new WxMaSubscribeMessage.MsgData();
        msgData2.setName("phrase7");
        msgData2.setValue("审核通过");
        list.add(msgData1);
        list.add(msgData2);

        wxMaSubscribeMessage.setData(list);//绑定的数据
        wxMaSubscribeMessage.setLang(WxMaConstants.MiniProgramLang.ZH_CN);
//        wxMaSubscribeMessage.setPage();
        wxMaSubscribeMessage.setTemplateId("5U67v-dtQCNUzWoJhHL-vbNyzXDwwKEoo7ZKEVE7JRI");
        wxMaSubscribeMessage.setToUser(tMemberUser.getOpenId());//openid
        //发送订阅消息
        wxMaService.getMsgService().sendSubscribeMsg(wxMaSubscribeMessage);
        // =========== 发送小程序消息 =================

    }
}
