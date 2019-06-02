package com.nqlz.springrabbitmq.config;

import com.nqlz.springrabbitmq.convert.TextMessageConvert;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class queueAndExchange {
    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.addQueues(queue001(), queue002(), queue003(), queue_image(), queue_pdf());
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(5);
        container.setDefaultRequeueRejected(false);
        //签收机制
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setConsumerTagStrategy(queue -> queue + "_STACK_HH_" + UUID.randomUUID().toString());
        //加入监听
        /*container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            String msg = new String(message.getBody());
            System.out.println("------消费者："+msg);

        });*/
        //加入适配器
        //适配器方式. 默认是有自己的方法名字的：handleMessage
        // 可以自己指定一个方法的名字: consumeMessage
        // 也可以添加一个转换器: 从字节数组转换为String
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        /*adapter.setDefaultListenerMethod("consumeMessage");
        adapter.setMessageConverter(new TextMessageConvert());*/

        //2 适配器方式，队列名称和方法名称也可以一一配置
        Map<String, String> queueOrTagToMethodName=new HashMap<>();
        queueOrTagToMethodName.put("queue001", "method1");
        queueOrTagToMethodName.put("queue002", "method2");
        adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
        adapter.setMessageConverter(new TextMessageConvert());


        container.setMessageListener(adapter);

        return container;
    }

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     */
    @Bean
    public TopicExchange exchange001() {
        return new TopicExchange("topic001", true, false);
    }

    @Bean
    public Queue queue001() {
        return new Queue("queue001", true); //队列持久
    }

    @Bean
    public Binding binding001() {
        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.*");
    }

    @Bean
    public TopicExchange exchange002() {
        return new TopicExchange("topic002", true, false);
    }

    @Bean
    public Queue queue002() {
        return new Queue("queue002", true); //队列持久
    }

    @Bean
    public Binding binding002() {
        return BindingBuilder.bind(queue002()).to(exchange002()).with("rabbit.*");
    }

    @Bean
    public Queue queue003() {
        return new Queue("queue003", true); //队列持久
    }

    @Bean
    public Binding binding003() {
        return BindingBuilder.bind(queue003()).to(exchange001()).with("mq.*");
    }

    @Bean
    public Queue queue_image() {
        return new Queue("image_queue", true); //队列持久
    }

    @Bean
    public Queue queue_pdf() {
        return new Queue("pdf_queue", true); //队列持久
    }

}
