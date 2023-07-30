package com.wolf.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqUtil {

    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("47.101.184.48");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        return channel;
    }

}
