package com.wolf.rabbitmq.six;

import com.rabbitmq.client.Channel;
import com.wolf.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class DirectLogs {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();
//        channel

        Scanner scanner = new Scanner(System.in);
        int i = 0;
        while (scanner.hasNext()){
            String message = scanner.next();
            i ++;
            if(i%3==0){
                System.out.println(String.format("i:%s，发送给routingKey:%s", i,"info"));
                channel.basicPublish(EXCHANGE_NAME,"info",null,message.getBytes("UTF-8"));
            } else if(i%3==1){
                System.out.println(String.format("i:%s，发送给routingKey:%s", i,"warning"));
                channel.basicPublish(EXCHANGE_NAME,"warning",null,message.getBytes("UTF-8"));
            } else if(i%3==2){
                System.out.println(String.format("i:%s，发送给routingKey:%s", i,"error"));
                channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes("UTF-8"));
            }
            System.out.println("生产者发出消息");
        }
    }
}
