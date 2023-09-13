package com.ruoyi.common.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "my_exchange";
    public static final String QUEUE_NAME = "my_queue";
    public static final String ROUTING_KEY = "my_routing_key";

    //=====================普通队列定义 测试==============================

    //创建一个交换机
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    //创建一个队列
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME);
    }

    //绑定
    @Bean
    public Binding binding(DirectExchange exchange, Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    //=====================普通队列定义 测试==============================


    //=========================延迟插件队列 和 交换机定义========================
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    //自定义交换机 我们在这里定义的是一个延迟交换机
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        //自定义交换机的类型
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false,
                args);
    }

    //绑定队列
    @Bean
    public Binding bindingDelayedQueue(@Qualifier("delayedQueue") Queue queue,
                                       @Qualifier("delayedExchange") CustomExchange
                                               delayedExchange) {
        return
                BindingBuilder.bind(queue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }

    //=========================延迟插件队列 和 交换机定义========================



    //=========================2分钟关闭订单========================
    public static final String DELAYED_CLOSE_ORDER_QUEUE_NAME = "delayed.close.order.queue";
    public static final String DELAYED_CLOSE_ORDER_EXCHANGE_NAME = "delayed.close.order.exchange";
    public static final String DELAYED_CLOSE_ORDER_ROUTING_KEY = "delayed.close.order.routingkey";

    @Bean
    public Queue delayedCloseOrderQueue() {
        return new Queue(DELAYED_CLOSE_ORDER_QUEUE_NAME);
    }

    //自定义交换机 我们在这里定义的是一个延迟交换机
    @Bean
    public CustomExchange delayedCloseOrderExchange() {
        Map<String, Object> args = new HashMap<>();
        //自定义交换机的类型
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_CLOSE_ORDER_EXCHANGE_NAME, "x-delayed-message", true, false,
                args);
    }

    //绑定队列
    @Bean
    public Binding bindingDelayedCloseOrderQueue(@Qualifier("delayedCloseOrderQueue") Queue delayedCloseOrderQueue,
                                       @Qualifier("delayedCloseOrderExchange") CustomExchange
                                               delayedCloseOrderExchange) {
        return
                BindingBuilder.bind(delayedCloseOrderQueue).to(delayedCloseOrderExchange).with(DELAYED_CLOSE_ORDER_ROUTING_KEY).noargs();
    }

    //=========================2分钟关闭订单========================


    //=========================ES存储场次========================

    @Bean
    public Queue bianpaiQueue() {
        return new Queue("bianpaiQueue");
    }


    @Bean
    public DirectExchange esExchange() {
        return new DirectExchange("esExchange");
    }
    //绑定队列
    @Bean
    public Binding bindingbianpaiQueue(@Qualifier("bianpaiQueue") Queue bianpaiQueue,
                                                 @Qualifier("esExchange") DirectExchange
                                                         esExchange) {
        return
                BindingBuilder.bind(bianpaiQueue).to(esExchange).with("bianpaiRoutingKey");
    }

    //=========================ES存储场次========================



    //发送消息的工具类
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

}
