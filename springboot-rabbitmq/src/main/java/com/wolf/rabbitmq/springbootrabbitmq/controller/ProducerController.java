package com.wolf.rabbitmq.springbootrabbitmq.controller;

import com.wolf.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/confirm")
@Slf4j
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @GetMapping("sendMessage/{message}")
    public void sendMessage(@PathVariable String message){
        //可路由
        CorrelationData correlationData = new CorrelationData("1");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY,
                message +  ConfirmConfig.CONFIRM_ROUTING_KEY,
                correlationData);

        //不可路由,routingkey是错误的
        CorrelationData correlationData2 = new CorrelationData("2");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY+2,
                message +  ConfirmConfig.CONFIRM_ROUTING_KEY+2,
                correlationData2);

        log.info("发送消息内容为:{}",message);
    }




}
