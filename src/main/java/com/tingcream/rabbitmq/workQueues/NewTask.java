package com.tingcream.rabbitmq.workQueues;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
/**
 * (AMQP DEFAULT)  
 * @author jelly
 *
 */
public class NewTask {
	
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
		
 
		boolean  durable=true ;
		channel.queueDeclare(QUEUE_NAME, durable, false,false, null);    
	    //String message = "Hello rabbitmq";
		//exchagne名称默认为 (AMQP DEFAULT)  direct
		//channel.basicPublish(exchange, routingKey, props, body);
	    //channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		//System.out.println(" [x] Sent '" + message + "'");
		
		for(int i=0;i<20;i++) {
			String message="hello direct task message E "+i;
			//发布消息 
			channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");
		}
		
		//关闭连接
		channel.close();
		connection.close();
	
  }
	
}
