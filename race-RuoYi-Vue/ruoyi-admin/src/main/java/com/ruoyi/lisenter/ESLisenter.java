package com.ruoyi.lisenter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.ruoyi.busi.domain.TSession;
import com.ruoyi.busi.domain.TSessionDetail;
import com.ruoyi.busi.domain.TSign;
import com.ruoyi.busi.easyes.mapper.SessionDetailMapper;
import com.ruoyi.busi.easyes.mapper.SessionMapper;
import com.ruoyi.busi.mapper.TSignMapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryWrapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 消费者
 * 监听指定的队列 消费消息
 */
@Slf4j
@Component
public class ESLisenter {

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private SessionDetailMapper sessionDetailMapper;

    /**
     * 定义订单 到期关闭的消费者
     */
    @RabbitListener(queues = "bianpaiQueue")
    public void receiveDelayedCloseOrderQueue(Message message) {
        //        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, map);
        //转换成map
        byte[] body = message.getBody();
//        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
//        stringObjectHashMap.put("tSessionList",tSessionList);
//        stringObjectHashMap.put("tSessionDetailList",tSessionDetailList);


        Map map = JSONUtil.toBean(new String(body), Map.class);
        log.info("当前时间：{},收到 ES 延时队列的消息：{}", DateUtil.now(), map);

        JSONArray tSessionList = JSONUtil.parseArray(map.get("tSessionList"));
        List<TSession> tSessions = tSessionList.toList(TSession.class);

        JSONArray tSessionDetailList = JSONUtil.parseArray(map.get("tSessionDetailList"));
        List<TSessionDetail> tSessionDetails = tSessionDetailList.toList(TSessionDetail.class);

        //绑定关系
        {
            tSessions.stream().forEach(new Consumer<TSession>() {
                @Override
                public void accept(TSession tSession) {
                    //过滤出当前session的两个detail
                    List<TSessionDetail> collect = tSessionDetails.stream().filter(tSessionDetail -> tSessionDetail.getSessionId().longValue() == tSession.getId().longValue()).collect(Collectors.toList());
                    tSession.setSessionsDetailList(collect);
                }
            });
        }
        //先删除所有这个赛事id的所有数据再插入，因为重新编排的话，id变了
        Integer delete = sessionMapper.delete(new LambdaEsQueryWrapper<TSession>()
                .eq(TSession::getCompetitionId, tSessions.get(0).getCompetitionId()));
        System.out.println(delete);

        //插入ES
        Integer integer = sessionMapper.insertBatch(tSessions);

        System.out.println("ES插入数量:"+integer);


    }




}
