package com.wolf.rabbitmq.seven;

import com.rabbitmq.client.Channel;
import com.wolf.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class EmitLogTopic {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();
        /**
         * Q1的binding：
         *      *.orange.*
         *
         * Q2的binding:
         *      *.*.rabbit
         *      lazy.#
         */
        Map<String,String> bindingMap = new HashMap<>();
        bindingMap.put("quick.orange.rabbit","被队列Q1Q2接收到");
        bindingMap.put("quick.orange.fox","被队列Q1接收到");
        bindingMap.put("lazy.brown.fox","被队列Q2接收到 ");
        bindingMap.put("lazy.pink.rabbit","虽然满足队列Q2的两个绑定但是只会被接收一次");
        bindingMap.put("quick.orange.male.rabbit","四个单词不匹配任何绑定会被丢弃");

        for (Map.Entry<String, String> stringEntry : bindingMap.entrySet()) {
            String routingKey = stringEntry.getKey();
            String message = stringEntry.getValue();
            channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes("UTF-8"));
            System.out.println(String.format("生产者发送消息 %s", message));
        }
    }
}
