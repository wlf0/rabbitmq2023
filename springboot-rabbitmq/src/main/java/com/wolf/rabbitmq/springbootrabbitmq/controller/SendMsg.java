package com.wolf.rabbitmq.springbootrabbitmq.controller;

import com.wolf.rabbitmq.springbootrabbitmq.config.DelayedQueueConfig;
import com.wolf.rabbitmq.springbootrabbitmq.config.TtlQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 发送延迟消息
 */
@Slf4j
@RestController
@RequestMapping("ttl")
public class SendMsg {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message){
        log.info("当前时间:{},发送一条信息给两个TTL队列:{}",new Date().toString(),message);

        rabbitTemplate.convertAndSend(TtlQueueConfig.X_EXCHANGE,
                "XA",
                String.format("消息来自ttl为10s的队列:%s", message));

        rabbitTemplate.convertAndSend(TtlQueueConfig.X_EXCHANGE,
                "XB",
                String.format("消息来自ttl为40s的队列:%s", message));
    }

    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendExpirationMsg(@PathVariable String message,@PathVariable String ttlTime){
        log.info("当前时间:{},发送一条信息给一个TTL队列:{},时长为{}ms",new Date().toString(),message,ttlTime);

        MessagePostProcessor messagePostProcessor = (msg)->{
            //设置延迟时长
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        };
        rabbitTemplate.convertAndSend(TtlQueueConfig.X_EXCHANGE,
                "XC",
                String.format("消息来自ttl为10s的队列:%s", message),
                messagePostProcessor);
    }

    /**
     * 发送消息,基于插件
     * @param message
     * @param delayTime
     */
    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendDelayMsg(@PathVariable String message,@PathVariable Integer delayTime){
        log.info("当前时间:{},发送一条信息给一个delay队列:{},时长为{}ms",new Date().toString(),message,delayTime);

        MessagePostProcessor messagePostProcessor = (msg)->{
            //设置延迟时长
            msg.getMessageProperties().setDelay(delayTime);
            return msg;
        };
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,
                DelayedQueueConfig.DELAYED_ROUTING_KEY,
                String.format("延迟队列消息:%s", message),
                messagePostProcessor);
    }
}
