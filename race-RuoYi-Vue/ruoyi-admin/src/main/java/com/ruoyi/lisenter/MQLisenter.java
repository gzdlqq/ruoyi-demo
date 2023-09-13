package com.ruoyi.lisenter;

import cn.hutool.core.date.DateUtil;
import com.ruoyi.busi.domain.TSign;
import com.ruoyi.busi.mapper.TSignMapper;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * 消费者
 *      监听指定的队列 消费消息
 */
@Slf4j
@Component
public class MQLisenter {


    @Autowired
    TSignMapper signMapper;

    public static final String QUEUE_NAME = "my_queue";

    @RabbitListener(queues = QUEUE_NAME)
    public void receiveMYQueue(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到普通的消息：{}", DateUtil.now(), msg);
    }

    public static final String DELAYED_QUEUE_NAME = "delayed.queue";

    @RabbitListener(queues = DELAYED_QUEUE_NAME)
    public void receiveDelayedQueue(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到延时队列的消息：{}", DateUtil.now(), msg);
    }

    public static final String DELAYED_CLOSE_ORDER_QUEUE_NAME = "delayed.close.order.queue";
    /**
     * 定义订单 到期关闭的消费者
     */
    @RabbitListener(queues = DELAYED_CLOSE_ORDER_QUEUE_NAME)
//    @Transactional
    @Transactional
    public void receiveDelayedCloseOrderQueue(Message message) {
        String tradeNo = new String(message.getBody());
        log.info("当前时间：{},收到 关闭订单的 延时队列的消息：{}", DateUtil.now(), tradeNo);

        //完整的mq  消息应该要做到 确保被消费成功了之后，才ACK
        //如果不确认（也许你的业务逻辑 出现异常），mq不会认为你消费了，会反复发送消息
        //1.一定要注意接口幂等性  2.注意性能问题（应该要做到 如果连续 5次 没有消费成功,没有手动ack，那就让mq不要发了 注意CPU爆炸）

        //1.修改业务订单
        TSign tSign = signMapper.selectTSignByTradeNo(tradeNo);
        //未付款的才关闭
        if(tSign.getStatus().equals("0")){
            tSign.setStatus("3");
            signMapper.updateTSign(tSign);
        }

    }
}
