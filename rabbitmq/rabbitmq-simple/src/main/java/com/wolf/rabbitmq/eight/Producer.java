package com.wolf.rabbitmq.eight;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.wolf.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 过期时间引发的
 * 死信队列生产者
 */
public class Producer {
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();

        //设置消息的TTL参数 单位是ms
        AMQP.BasicProperties basicProperties =new AMQP.BasicProperties().builder()
                .expiration("10000")
                .build();
        for (int i = 0; i < 10; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",basicProperties,message.getBytes("UTF-8"));
        }
    }
}
