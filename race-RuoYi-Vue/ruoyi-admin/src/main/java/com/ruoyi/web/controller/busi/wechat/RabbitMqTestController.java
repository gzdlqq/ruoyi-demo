package com.ruoyi.web.controller.busi.wechat;

import cn.hutool.core.date.DateUtil;
import com.ruoyi.busi.domain.TCompetition;
import com.ruoyi.common.config.RabbitMQConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.PageResp;
import com.ruoyi.common.core.domain.PageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(tags = "延迟队列插件")
@RestController
@RequestMapping("/wechat/mq")
@Slf4j
public class RabbitMqTestController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @PostMapping("/sendMsgTest")
    @ApiOperation("测试发送到一个队列中")
    public AjaxResult sendMsgTest() {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, "Hello, World!");


        return AjaxResult.success("成功");
    }


    //发送延迟消息测试


    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";


    @GetMapping("sendDelayMsg/{message}/{delayTime}")
    @ApiOperation("测试延迟队列")
    public void sendMsg(@PathVariable String message, @PathVariable Integer delayTime) {
        rabbitTemplate.convertAndSend(DELAYED_EXCHANGE_NAME, DELAYED_ROUTING_KEY, message,
                correlationData -> {
            //在队列中多久之后 才允许被消费者消费
                    correlationData.getMessageProperties().setDelay(delayTime);
                    return correlationData;
                });
        log.info(" 当 前 时 间 ： {}, 发送一条延迟 {} 毫秒的信息给队列 delayed.queue:{}", DateUtil.now(), delayTime, message);
    }

}
