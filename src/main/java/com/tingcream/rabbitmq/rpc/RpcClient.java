package com.tingcream.rabbitmq.rpc;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * rabbitmq  rpc 异步 client
 * @author jelly
 * 
 */
public class RpcClient {
	
	  private  static  String RPC_QUEUE_NAME = "rpc_queue";
	
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
			
			String replyQueueName = channel.queueDeclare().getQueue();//回复的队列名称
			String corrId=UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
			
			// 生成发送消息的属性
			AMQP.BasicProperties props = new AMQP.BasicProperties
			        .Builder()
			        .correlationId(corrId) // 唯一标志本次请求
			        .replyTo(replyQueueName) // 设置回调队列
			        .build();
			// 发送消息，发送到默认交换机
			String message ="hello RPC";
			//请求队列名称 
			channel.basicPublish("", RPC_QUEUE_NAME, props, message.getBytes("UTF-8"));
		 
		 

		     BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

		    String ctag = channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
		       @Override
		      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
		        if (properties.getCorrelationId().equals(corrId)) {
		            response.offer(new String(body, "UTF-8"));
		        }  
		      }
		    });

		    String result = response.take();
		    channel.basicCancel(ctag);
		  //  return result;
		    
		    System.out.println("返回结果： "+result);
			
			
	 
			 
		
	  }

}
