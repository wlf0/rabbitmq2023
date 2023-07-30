package com.wolf.rabbitmq.two;

import com.rabbitmq.client.Channel;
import com.wolf.rabbitmq.utils.RabbitMqUtil;
import org.apache.commons.io.Charsets;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Task01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();

        /**
         * 1.队列名
         * 2.是否持久
         * 3.消息共享(一个消息由多个消费者消费)
         *
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
            System.out.println(String.format("发送消息%s",message));
        }
    }
}
