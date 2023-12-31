package com.wolf.rabbitmq.springbootrabbitmq.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 发布确认 高级内容
 */
@Configuration
public class ConfirmConfig {

    //交换机
    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";

    //备份交换机
    public static final String BACKUP_EXCHANGE_NAME = "backup_exchange";

    //队列
    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";

    //备份队列
    public static final String BACKUP_QUEUE_NAME = "backup_queue";

    //报警队列
    public static final String WARNING_QUEUE_NAME = "warning_queue";

    //routingKey
    public static final String CONFIRM_ROUTING_KEY = "key1";

    //交换机声明
    @Bean("confirmExchange")
    public DirectExchange confirmExchange(){
        //return new DirectExchange(CONFIRM_EXCHANGE_NAME);

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("alternate-exchange",BACKUP_EXCHANGE_NAME);
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).withArguments(arguments).build();
    }

    //queue
    @Bean("confirmQueue")
    public Queue confirmQueue(){
        return  QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    //bindingkey
    @Bean
    public Binding queueBindingExchange(@Qualifier("confirmQueue") Queue queue,
                                        @Qualifier("confirmExchange") DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with(CONFIRM_ROUTING_KEY);
    }

    @Bean("backupExchange")
    public FanoutExchange backupExchange(){
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    //queue
    @Bean("backupQueue")
    public Queue backupQueue(){
        return  QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    //queue
    @Bean("warningQueue")
    public Queue warningQueue(){
        return  QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    //bindingkey
    @Bean
    public Binding backupQueueBindingBackupExchange(@Qualifier("backupQueue") Queue backupQueue,
                                        @Qualifier("backupExchange") FanoutExchange backupExchange){
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    //bindingkey
    @Bean
    public Binding warningQueueBindingBackupExchange(@Qualifier("warningQueue") Queue warningQueue,
                                                    @Qualifier("backupExchange") FanoutExchange backupExchange){
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }


}
