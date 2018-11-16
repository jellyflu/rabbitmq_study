package com.tingcream.rabbitmq.topic;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class TopicLogReceiverA {
	private static    String EXCHANGE_NAME="topic_logs";
	
	public static void main(String[] args) throws Exception {
		
		ConnectionFactory factory = new ConnectionFactory();
        
		//主机  端口  vhost 用户名 密码
		factory.setHost("192.168.9.102");
		factory.setUsername("rabbitmq");
		factory.setPassword("rabbitmq123");
		factory.setPort(AMQP.PROTOCOL.PORT);
		factory.setVirtualHost("/");
	    Connection connection=factory.newConnection();
	    Channel channel=connection.createChannel();
	    
	    
	    channel.exchangeDeclare(EXCHANGE_NAME, "topic");
	    
	    //获取一个随机的队列名称   
	    String queueName = channel.queueDeclare().getQueue();
	    
	    
	    
	    
	    //  绑定routingKey "#" 表示对所有队列感兴趣 ，相当于fanout 
	    //  绑定routingKey 若不含"#"和"*",表示只对精确匹配的路由key队列感兴趣，则相对于routing 
	    
	    
	   // channel.queueBind(queue, exchange, routingKey)
	    channel.queueBind(queueName, EXCHANGE_NAME, "*.black.*"); 
	    System.out.println("TopicLogReceiverA Waiting for messages");
	    
	      Consumer consumer = new DefaultConsumer(channel) {
	        @Override
	        public void handleDelivery(String consumerTag, Envelope envelope,
	                                   AMQP.BasicProperties properties, byte[] body) throws IOException {
	          String message = new String(body, "UTF-8");
	          System.out.println( "TopicLogReceiverA接收到消息：" + message );   
	        }
	      };
	      channel.basicConsume(queueName, true, consumer);
	}

}
