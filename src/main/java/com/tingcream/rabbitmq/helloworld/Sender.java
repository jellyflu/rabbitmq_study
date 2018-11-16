package com.tingcream.rabbitmq.helloworld;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 *  (AMQP DEFAULT)   
 * @author jelly
 *
 */
public class Sender {
	
	private final static String QUEUE_NAME = "helloWorld";
	
	public static void main(String[] args) throws  Exception {
		
		ConnectionFactory factory = new ConnectionFactory();
		//主机  端口  vhost 用户名 密码
		factory.setHost("192.168.9.102");
		factory.setUsername("rabbitmq");
		factory.setPassword("rabbitmq123");
		factory.setPort(AMQP.PROTOCOL.PORT);
		factory.setVirtualHost("/");
		
		
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
 
		/*queueDeclare 
		 * 第一个参数 queue: 队列名称
		 * 第二个参数 durable: 是否持久，  true则当rabbitmq broker重启后队列仍在
		 * 第三个参数 exclusive: 是否是独占队列（创建者可以使用的私有队列，断开后自动删除）
		 * 第四个参数 autoDelete ，当所有消费者连接断开时，是否自动删除队列  ,true是
		 * 第五个参数 arguments  ，可选参数map
		 */
		//channel.queueDeclare(queue, durable, exclusive, autoDelete, arguments)
		channel.queueDeclare(QUEUE_NAME, false, false,false, null);    
		String message = "Hello rabbitmq";
		
		//exchagne名称默认为 (AMQP DEFAULT)  direct
		//channel.basicPublish(exchange, routingKey, props, body);
		
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");
		
		//关闭连接
		channel.close();
		connection.close();
		
	}

}
