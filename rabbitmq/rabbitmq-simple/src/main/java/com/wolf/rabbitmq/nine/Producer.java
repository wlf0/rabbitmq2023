package com.wolf.rabbitmq.nine;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Producer {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建一个连接工程
        ConnectionFactory factory = new ConnectionFactory();

        //工厂IP 连接rabbitMQ的实例
        factory.setHost("192.168.43.94");
        factory.setUsername("admin");
        factory.setPassword("123");

        //获取连接
        Connection connection = factory.newConnection();

        //获取逻辑连接：信道
        Channel channel = connection.createChannel();

        /**
         * 生成一个队列
         * 1.队列名称
         * 2.是否持久化消息（磁盘），默认保存在内存中
         * 3.该队列是否只供一个消费者消费，是否可以消息共享：
         *      true：可以共享，多个消费者一起消费
         *      false：只能一个消费者消费
         * 4.是否自动删除：
         *      true：自动删除
         *      false：不自动删除
         * 5.其它参数
         */
        Map<String, Object> arguments = new HashMap<>();
        //0 - 255 之间,设置0到10之前
        arguments.put("x-max-priority",10);
        channel.queueDeclare(QUEUE_NAME,true,false,false,arguments);



        for (int i = 0; i < 10; i++) {
            //消息
            String message = "hello world" + i;
            if(i == 5){
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("",QUEUE_NAME,properties,message.getBytes());
            }else {
                channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            }
            System.out.println("消息发送完毕");
        }

    }
}
