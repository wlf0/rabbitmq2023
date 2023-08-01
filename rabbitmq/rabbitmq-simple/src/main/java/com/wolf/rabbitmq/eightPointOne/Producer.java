package com.wolf.rabbitmq.eightPointOne;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.wolf.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 达到队列最大长度
 * 死信队列生产者
 */
public class Producer {
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();

        for (int i = 0; i < 10; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,message.getBytes("UTF-8"));
        }
    }
}
