package com.wolf.rabbitmq.eightPointTwo;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wolf.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 被消费者拒绝
 */
public class Consumer01 {
    //普通交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机
    public static final String DEAD_EXCHANGE = "dead_exchange";

    //普通队列
    public static final String NORMAL_QUEUE = "normal_queue";
    //死信队列
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();
        //交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        //普通队列
        //设置参数
        Map<String,Object> arguments = new HashMap<>();
        //  死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //  死信长度限制
        //  死信的RoutingKey
        arguments.put("x-dead-letter-routing-key","lisi");
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);
        //死信队列
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        //bind 普通交换机到普通队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");

        System.out.println("等待接收消息");

        DeliverCallback deliverCallback = (consumerTag,message)->{
            String messageInfo = new String(message.getBody(), "UTF-8");
            if(messageInfo.equals("info5")){
                System.out.println(String.format("被拒绝消息:%s", messageInfo));
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else {
                System.out.println("Consumer01 接受的消息是:"+ messageInfo);
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
        };
        //开启手动应答
        boolean autoAck = false;
        channel.basicConsume(NORMAL_QUEUE,autoAck,deliverCallback,consumerTag -> {});
    }
}
