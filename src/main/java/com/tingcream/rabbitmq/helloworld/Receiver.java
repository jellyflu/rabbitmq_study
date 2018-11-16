package com.tingcream.rabbitmq.helloworld;


import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Receiver {
	
	private final static String QUEUE_NAME = "helloWorld";

	  public static void main(String[] argv)
	      throws Exception {

		    ConnectionFactory factory = new ConnectionFactory();
			//主机  端口  vhost 用户名 密码
			factory.setHost("192.168.9.102");
			factory.setUsername("rabbitmq");
			factory.setPassword("rabbitmq123");
			factory.setPort(AMQP.PROTOCOL.PORT);
			factory.setVirtualHost("/");
			
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();

		//	channel.queueDeclare(queue, durable, exclusive, autoDelete, arguments)
		    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		     System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		    
		    Consumer consumer = new DefaultConsumer(channel) { 
		    	  @Override
		    	  public void handleDelivery(String consumerTag, Envelope envelope,
		    	                             AMQP.BasicProperties properties, byte[] body)
		    	      throws IOException {
		    	    String message = new String(body, "UTF-8");
		    	    System.out.println(" [x] Received '" + message + "'");
		    	  }
		    };
		    	
		    
		    //收取消息  自动ack  autoAck为true
		    //channel.basicConsume(queue, autoAck, callback)
		    channel.basicConsume(QUEUE_NAME, true, consumer);
		    
	      
	    }

}
