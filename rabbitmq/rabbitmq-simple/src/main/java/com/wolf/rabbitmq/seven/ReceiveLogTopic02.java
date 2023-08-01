package com.wolf.rabbitmq.seven;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wolf.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * topic
 */
public class ReceiveLogTopic02 {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();
        //交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //队列
        String queueName = "Q2";
        channel.queueDeclare(queueName,false,false,false,null);
        // bind
        channel.queueBind(queueName,EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind(queueName,EXCHANGE_NAME,"lazy.#");
        System.out.println("等待接受消息...");

        //消费者
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println(String.format("接收到消息%s",new String(message.getBody(),"UTF-8") ));
        };
        channel.basicConsume(queueName,true,deliverCallback,(consumerTag -> {}));
    }
}
