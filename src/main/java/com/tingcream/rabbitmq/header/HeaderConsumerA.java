package com.tingcream.rabbitmq.header;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class HeaderConsumerA {
	
   private static   String    EXCHANGE_NAME="my_headers_ex"; 
   public static void main(String[] args)  throws Exception {
	  ConnectionFactory factory = new ConnectionFactory();
		//主机  端口  vhost 用户名 密码
		factory.setHost("192.168.9.102");
		factory.setUsername("rabbitmq");
		factory.setPassword("rabbitmq123");
		factory.setPort(AMQP.PROTOCOL.PORT);
		factory.setVirtualHost("/");
		
		Connection connection=factory.newConnection();
	    Channel channel=connection.createChannel();
		    
	  //  channel.exchangeDeclare(EXCHANGE_NAME, "headers");
	    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.HEADERS);
	    
	    
	    String queueName = channel.queueDeclare().getQueue();
	    Map<String, Object> header = new HashMap<String, Object>();
	    header.put("x-match", "all");  //x-match: all表所有key-value全部匹配才匹配成功 ，any表只需要匹配任意一个key-value 即匹配成功。
	    header.put("name", "张三");
	    header.put("idcard","123321"); 
	    
	    channel.queueBind(queueName, EXCHANGE_NAME, "", header);
	    
	    Consumer consumer = new DefaultConsumer(channel){
          @Override
          public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
              String message = new String(body, "UTF-8");
              System.out.println(message);
          }
      };
	    
      channel.basicConsume(queueName, true, consumer);
     
 
	
	  
  }
  
  
  
  
}
