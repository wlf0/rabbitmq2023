package com.wolf.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.101.184.48");
        factory.setUsername("admin");
        factory.setPassword("123");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();


        //接受消息的回调
        DeliverCallback deliverCallback = ( consumerTag,  message)->{
            System.out.println(new String(message.getBody()));
        };
        //取消消息的回调
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println("消息被中断");
        };

        /**
         * 1.消费的队列
         * 2.是否自动答复
         * 3.消费的回调
         * 4.取消消费的回调
         *
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

    }
}
