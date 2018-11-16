package com.tingcream.rabbitmq.routing;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RoutingLogProducer {
	
	private static String EXCHANGE_NAME="routing_logs";
	
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
        
        //info warning  error
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");      
       //  channel.exchangeDeclare(exchange, type, durable, autoDelete, arguments)
         
        for (int i=0;i<10;i++){
            String message="你好,这是info级别消息 "+i;
             channel.basicPublish(EXCHANGE_NAME,"info",null,message.getBytes());
             System.out.println("RoutingLogProducer Send: " + message );
        }
        for (int i=0;i<10;i++){
        	String message="你好,这是warning级别消息 "+i;
        	channel.basicPublish(EXCHANGE_NAME,"warning",null,message.getBytes());
        	System.out.println("RoutingLogProducer Send: " + message );
        }
        for (int i=0;i<10;i++){
        	String message="你好,这是error级别消息 "+i;
        	channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes());
        	System.out.println("RoutingLogProducer Send: " + message );
        }
        channel.close();
        connection.close();
    }

}
