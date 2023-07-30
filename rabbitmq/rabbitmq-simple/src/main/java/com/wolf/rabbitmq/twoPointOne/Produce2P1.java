package com.wolf.rabbitmq.twoPointOne;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.wolf.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Produce2P1 {
    public static final String QUEUE_NAME = "twoPone";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();

        channel.queueDeclare(QUEUE_NAME,true,false,false,null);

        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()){
            String next = sc.next();
            for (int i = 0; i < 100; i++) {
                String message = next + i;
                channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            }
            System.out.println(String.format("发送消息:%s",next));
        }
    }
}
