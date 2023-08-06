package com.wolf.rabbitmq.springbootrabbitmq.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 交换机确认收到消息回调
 * 1.发消息 交换机接收到了 会 回调
 *      1.1 correlationData 保存回调消息的ID及相关消息
 *      1.2 交换机收到消息 true
 *      1.3 cause null
 * 2.发消息 交互机接受失败了 会 回调
 *      2.1 correlationDate 保存回调消息的ID相关消息
 *      2.2 交换机收到消息 ack = true
 *      2.3 cause 失败的原因
 */
@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {

    @Autowired
    RabbitTemplate rabbitTemplate;

    //注入到RabbitTemplate
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = "";
        if (correlationData!=null) {
            id = correlationData.getId();
        }
        if(ack){
            log.info("交互机已经收到了消息,id为{}",id);
        }else {
            log.info("交换机还未收到消息,id为{},原因为:{}",id,cause);
        }
    }

    //消息不可达目的地时返回给生产者
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("消息{},被交换机{} 退回，退回的原因为{},路由key:{}",
                new String(message.getBody()),
                exchange,replyText,
                routingKey);
    }


}
