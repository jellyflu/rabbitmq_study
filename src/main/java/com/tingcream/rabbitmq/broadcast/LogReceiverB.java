package com.tingcream.rabbitmq.broadcast;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class LogReceiverB {
	private static    String EXCHANGE_NAME="logs";
	
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
	    
	    
	    channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
	    //获取一个随机的队列名称
	    String queueName = channel.queueDeclare().getQueue();
	    
	    //关联 exchange 和 queue ，因为是广播无需指定routekey，routingKey设置为空字符串
	   // channel.queueBind(queue, exchange, routingKey)
	     channel.queueBind(queueName, EXCHANGE_NAME, ""); 
	     System.out.println("LogReceiverB Waiting for messages");
	    
	      Consumer consumer = new DefaultConsumer(channel) {
	        @Override
	        public void handleDelivery(String consumerTag, Envelope envelope,
	                                   AMQP.BasicProperties properties, byte[] body) throws IOException {
	          String message = new String(body, "UTF-8");
	          System.out.println( "LogReceiverA接收到消息：" + message );   
	        }
	        
	      };
	      //true 自动回复ack
	      channel.basicConsume(queueName, true, consumer);
 
		
	}

}
