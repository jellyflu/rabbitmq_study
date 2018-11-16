package com.tingcream.rabbitmq.rpc;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * rabbitmq  rpc 异步 server
 * @author jelly
 *
 */
public class RpcServer {
	
	  private  static  String RPC_QUEUE_NAME = "rpc_queue";
	
	  public static void main(String[] args) throws Exception {
			
			ConnectionFactory factory = new ConnectionFactory();
			//主机  端口  vhost 用户名 密码
			factory.setHost("192.168.9.102");
			factory.setUsername("rabbitmq");
			factory.setPassword("rabbitmq123");
			factory.setPort(AMQP.PROTOCOL.PORT);
			factory.setVirtualHost("/");
			
			Connection connection =null;
			try {
				    connection = factory.newConnection();
					Channel channel = connection.createChannel();
					// 声明一个rpc_queue队列
		            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
		            
		            // 设置同时最多只能获取一个消息
		            channel.basicQos(1);
		            
					// 定义消息的回调处理类
					Consumer consumer = new DefaultConsumer(channel) {
					    @Override
					    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
					        // 生成返回的结果，关键是设置correlationId值
					        AMQP.BasicProperties replyProps = new AMQP.BasicProperties
					                .Builder()
					                .correlationId(properties.getCorrelationId())
					                .build();
					        // 生成返回
					        String response = generateResponse(body);
					        // 回复消息，通知已经收到请求
					        channel.basicPublish( "", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
					        // 对消息进行应答
					        channel.basicAck(envelope.getDeliveryTag(), false);
					        // 唤醒正在消费者所有的线程
					        synchronized(this) {
					            this.notify();
					        }
					    }
					};
					 
					
					 // 消费消息
		            channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
		            
		            // 在收到消息前，本线程进入等待状态
		            while (true) {
		                synchronized(consumer) {
		                    try {
		                        consumer.wait();
		                    } catch (InterruptedException e) {
		                        e.printStackTrace();
		                    }
		                }
		            }
			} catch (Exception e) {
	            e.printStackTrace();
	        }
	        finally {
	            try {
	                connection.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
			 
	  }
	  
	    /**
	     * 暂停一段时间，再返回结果
	     * @param body
	     * @return
	     */
	    private static String generateResponse(byte[] body) {
	        System.out.println(" [RpcServer] receive requests: " + new String(body));
	        try {
	            Thread.sleep(1000 *3);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        return "返回数据" + new String(body) + "-" + System.currentTimeMillis();
	    }

}
