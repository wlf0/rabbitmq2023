package com.wolf.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wolf.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Woker01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                try {
                    Channel channel = RabbitMqUtil.getChannel();

                    long id = Thread.currentThread().getId();
                    DeliverCallback deliverCallback = (consumerTag,  message)->{
                        System.out.println(String.format("%s接受到的消息%s",id,new String(message.getBody())));
                    };

                    CancelCallback cancelCallback = (consumerTag)->{
                        System.out.println(String.format("%s取消消息%s",id,consumerTag));
                    };
                    System.out.println(String.format("%s 准备接受消息",Thread.currentThread().getId()));
                    channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }
}
