package com.tingcream.rabbitmq.topic;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class TopicLogProducer {
	
	private static String EXCHANGE_NAME="topic_logs";
	
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
        

//        lazy.black.elephant    懒惰的黑色的象
//        lazy.white.sheep       懒惰的白色的羊
//        quick.black.horse      快速的黑色的马
//        quick.red.horse        快速的红色的马
        
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");  
        
        for (int i=0;i<10;i++){
            String message="你好,这是lazy.black.elephant级别消息 "+i;
             channel.basicPublish(EXCHANGE_NAME,"lazy.black.elephant",null,message.getBytes());
             System.out.println("TopicLogProducer Send: " + message );
        }
        for (int i=0;i<10;i++){
        	String message="你好,这是lazy.white.sheep级别消息 "+i;
        	channel.basicPublish(EXCHANGE_NAME,"lazy.white.sheep",null,message.getBytes());
        	System.out.println("TopicLogProducer Send: " + message );
        }
        for (int i=0;i<10;i++){
        	String message="你好,这是quick.black.horse级别消息 "+i;
        	channel.basicPublish(EXCHANGE_NAME,"quick.black.horse",null,message.getBytes());
        	System.out.println("TopicLogProducer Send: " + message );
        }
        for (int i=0;i<10;i++){
        	String message="你好,这是quick.red.horse级别消息 "+i;
        	channel.basicPublish(EXCHANGE_NAME,"quick.red.horse",null,message.getBytes());
        	System.out.println("TopicLogProducer Send: " + message );
        }
        channel.close();
        connection.close();
    }

}
