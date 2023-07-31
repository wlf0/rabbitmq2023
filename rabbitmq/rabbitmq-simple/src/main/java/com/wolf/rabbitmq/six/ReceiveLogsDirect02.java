package com.wolf.rabbitmq.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wolf.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsDirect02 {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //声明队列
        String queueName = "disk";
        channel.queueDeclare(queueName,false,false,false,null);

        //建立连接bind info 和 warning
        channel.queueBind(queueName,EXCHANGE_NAME,"error");

        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println(String.format("ReceiveLogsDirect02 -> 接受消息:%s" ,new String(message.getBody(),"UTF-8")));
        };
        channel.basicConsume(queueName,true,deliverCallback,consumerTag->{});

    }
}
