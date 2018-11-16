package com.tingcream.rabbitmq.workQueues;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class WorkA {
	
	private final static String QUEUE_NAME = "direct_task_queue";
	

    public static void main(String[] args) throws Exception {
    	
        ConnectionFactory factory = new ConnectionFactory();
          
		//主机  端口  vhost 用户名 密码
		factory.setHost("192.168.9.102");
		factory.setUsername("rabbitmq");
		factory.setPassword("rabbitmq123");
		factory.setPort(AMQP.PROTOCOL.PORT);
		factory.setVirtualHost("/");
		
		
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
      
    //  channel.queueDeclare(queue, durable, exclusive, autoDelete, arguments)
		boolean  durable=true ;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);//channel向服务器声明一个队列，设置durable为true，则当rabbitmq 服务器重启时，队列不会丢失
        System.out.println("WorkerA  Waiting for messages");
 
        //每次从队列获取的message的数量
         channel.basicQos(1);
     //   prefetchCount maximum number of messages that the server will deliver, 0 if unlimited

     
        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("WorkerA  Received ：" + message );
                try {
                     
                    doWork(message);
                }catch (Exception e){
                    channel.abort();
                }finally {
                    System.out.println("WorkerA Done");
                    channel.basicAck(envelope.getDeliveryTag(),false);//当消息处理完毕后 在finally中 回复一个ack  手动ack
                }
            }
        };
        boolean autoAck=false;
         
        //消息消费完成确认   不自动 ack  非自动ack
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
    private static void doWork(String task) {
    	try {
			Thread.sleep(5000);// 暂停2秒钟
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
    }

}
