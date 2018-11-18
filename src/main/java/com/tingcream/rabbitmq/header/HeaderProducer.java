package com.tingcream.rabbitmq.header;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class HeaderProducer {
	
	private static   String    EXCHANGE_NAME="my_headers_ex"; 
	
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
		    
	   // channel.exchangeDeclare(EXCHANGE_NAME, "headers");
	   //  channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.HEADERS);
	    
	    Map<String, Object> header = new HashMap<String, Object>();
	    header.put("name", "张三");
	    header.put("idcard","123321"); 
	    header.put("phone","13567655555");
        AMQP.BasicProperties.Builder properties = new AMQP.BasicProperties().builder().headers(header);

        String message = "Hello headers消息!";
        
       // channel.basicPublish(exchange, routingKey, props, body);
        channel.basicPublish(EXCHANGE_NAME, "", properties.build(), message.getBytes("UTF-8"));
         System.out.println("headerProducer发送消息:"+message);
        channel.close();
        connection.close();
 
	     
	  
  }
}
