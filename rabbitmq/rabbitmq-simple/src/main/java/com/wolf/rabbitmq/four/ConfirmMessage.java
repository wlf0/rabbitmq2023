package com.wolf.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.wolf.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * 发布确认
 * 1.单个确认
 * 2.批量确认
 * 3.异步确认
 *
 * 比较使用时间
 */
public class ConfirmMessage {
    public static void main(String[] args) throws InterruptedException, TimeoutException, IOException {
        //1.单个确认 23166ms
        //publishMessageIndividually();
        //2.批量确认 457ms
        //publishMessageBatch();
        //3.异步确认 423ms
        publishMessageAsync();
    }

    public static final int MESSAGE_COUNT = 1000;

    public static void publishMessageIndividually() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);

        channel.confirmSelect();
        long begin = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            //单个确认
            boolean hasConfirm = channel.waitForConfirms();
            if(hasConfirm){
                //System.out.println(String.format("message:%S send success",message));
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("单独确认耗时:%sms",end - begin));

    }

    public static void publishMessageBatch() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);

        channel.confirmSelect();
        long begin = System.currentTimeMillis();

        int batchSize = 100;
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());

            if(i%batchSize == 99){
                boolean hasConfirm = channel.waitForConfirms();
                if(hasConfirm){
                    System.out.println("确认发送");
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("批量确认耗时:%sms",end - begin));

    }

    public static void publishMessageAsync() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);

        channel.confirmSelect();

        /**
         * 线程安全有序的一个hash表，适用于高并发的情况
         * 1.轻松的将序号与消息关联
         * 2.轻松批量删除条目
         * 3.支持高并发
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<>();

        //消息发送成功回调
        ConfirmCallback ackCallback = ( deliveryTag,  multiple)->{
            //2.删除已经确认的消息  剩下的就是未确认的消息
            //headMap 从map头节点到参数指定的节点 这一段的map，修改子mpa对原map也产生影响
            ConcurrentNavigableMap<Long, String> confirmd = outstandingConfirms.headMap(deliveryTag);
            if(multiple){
                confirmd.clear();
            }else {
                outstandingConfirms.remove(deliveryTag);
            }

            System.out.println("已经确认消息"+deliveryTag);
        };
        //消息发送失败回调
        ConfirmCallback nackCallback =( deliveryTag,  multiple)->{
            String s = outstandingConfirms.get(deliveryTag);
            System.out.println(String.format("未确认消息标记:%s,内容:%s",deliveryTag,s));
        };
        //准备消息的监听器，监听哪些消息成功了，哪些消息失败了
        channel.addConfirmListener(ackCallback,nackCallback);

        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "" + i;
            channel.basicPublish("",queueName,null,message.getBytes());
            //1.记录所有要发送的消息
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);
            System.out.println(String.format("记录发送的消息%s,序号为%s",message,channel.getNextPublishSeqNo()));
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("异步确认耗时:%sms",end - begin));

    }
}
