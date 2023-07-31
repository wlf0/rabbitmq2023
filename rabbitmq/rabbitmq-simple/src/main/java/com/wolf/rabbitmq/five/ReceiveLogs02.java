package com.wolf.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wolf.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogs02 {

    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        /**
         * 声明一个临时队列
         * 队列名称随机，断开连接时自动删除队列
         */
        String tempQueue = channel.queueDeclare().getQueue();

        /**
         * 绑定交换机
         */
        channel.queueBind(tempQueue,EXCHANGE_NAME,"");
        System.out.println("等待接受消息");

        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println(String.format("ReceiveLogs02 -> 接受消息:%s" ,new String(message.getBody(),"UTF-8")));
        };
        channel.basicConsume(tempQueue,true,deliverCallback,consumerTag -> {});
    }
}
