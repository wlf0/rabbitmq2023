package com.wolf.rabbitmq.twoPointOne;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wolf.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Consumer2P1 {
    public static void main(String[] args) throws IOException, TimeoutException {
        for (int i = 0; i < 3; i++) {
            Channel channel = RabbitMqUtil.getChannel();
            new Thread(()->{
                try {
                    int prefetchCount = 1;
                    channel.basicQos(prefetchCount);

                    String s = channel.basicConsume(Produce2P1.QUEUE_NAME, false, (consumerTag,message) -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        long deliveryTag = message.getEnvelope().getDeliveryTag();
                        String info = new String(message.getBody());
                        System.out.println(String.format("拿到消息=%s,tag=%s",info,deliveryTag));
                        if(deliveryTag % 2 ==0){
                            System.out.println(String.format("接受消息%s", info));
                            channel.basicAck(deliveryTag,false);
                        }else {
                            System.out.println(String.format("拒绝消息%s", info));
                            channel.basicNack(deliveryTag,false,true);
                        }
                    }, (consumerTag) -> {

                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
