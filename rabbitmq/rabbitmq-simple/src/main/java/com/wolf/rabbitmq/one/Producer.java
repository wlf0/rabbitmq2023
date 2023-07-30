package com.wolf.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建一个连接工程
        ConnectionFactory factory = new ConnectionFactory();

        //工厂IP 连接rabbitMQ的实例
        factory.setHost("47.101.184.48");
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
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //消息
        String message = "hello world";

        /**
         * 发送消息
         * 1.发送到哪个交换机
         * 2.路由的key值是哪个，本次是队列的名称
         * 3.其他参数
         * 4.消息体
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送完毕");
    }
}
