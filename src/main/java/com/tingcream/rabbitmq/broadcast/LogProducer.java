package com.tingcream.rabbitmq.broadcast;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class LogProducer {
	
	private static String EXCHANGE_NAME="logs";
	
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
        
        //    声明一个fanout 广播交换机，名称为logs  ，  声明的fanout队列     autoDelete为true, exclude 为true
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout"); //amq.gen-xxxxxxxxxx   AD EXCL      
       //  channel.exchangeDeclare(exchange, type, durable, autoDelete, arguments)
         
        for (int i=0;i<10;i++){
            String message="你好 World "+i;
             // channel.basicPublish(exchange, routingKey, props, body);
             //发布消息时指定routingKey为""
             channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());
             System.out.println("LogProducer Send ：" + message );
        }
        channel.close();
        connection.close();
    }

}
