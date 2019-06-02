package com.nqlz.springrabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringRabbitmqApplicationTests {

    @Autowired
    private RabbitAdmin rabbitAdmin;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testAdmin() {
        rabbitAdmin.declareExchange(new DirectExchange("test.direct", false, false));
        rabbitAdmin.declareExchange(new TopicExchange("test.topic", false, false));
        rabbitAdmin.declareExchange(new FanoutExchange("test.fanout", false, false));

        rabbitAdmin.declareQueue(new Queue("test.direct.queue", false));
        rabbitAdmin.declareQueue(new Queue("test.topic  .queue", false));
        rabbitAdmin.declareQueue(new Queue("test.fanout.queue", false));

        rabbitAdmin.declareBinding(new Binding("test.direct.queue"
                , Binding.DestinationType.QUEUE, "test.direct", "direct", null));

        rabbitAdmin.declareBinding(BindingBuilder
                .bind(new Queue("test.topic  .queue", false))
                .to(new TopicExchange("test.topic", false, false))
                .with("user.#")
        );
    }

    @Test
    public void testSendMessage(){
        //创建消息
        MessageProperties messageProperties=new MessageProperties();
        messageProperties.getHeaders().put("desc", "消息描述");
        messageProperties.getHeaders().put("type", "消息类型");
        Message message = new Message("rabbitMQ,你好好".getBytes(), messageProperties);

        rabbitTemplate.convertAndSend("topic001","spring.amqp",message,
                new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        System.out.println("---------添加额外设置--------");
                        message.getMessageProperties().getHeaders().put("prop", "额外的配置");
                        return message;
                    }
                }
        );

    }

    @Test
    public void testSendMessage2(){
        rabbitTemplate.convertAndSend("topic002","rabbit.ddk","你好，mq");
    }

    @Test
    public void testSendMessage4Convert(){
        //创建消息
        MessageProperties messageProperties=new MessageProperties();
        messageProperties.getHeaders().put("desc", "消息描述");
        messageProperties.setContentType("text/plain");
        Message message = new Message("rabbitMQ,你好好,convertMessage".getBytes(), messageProperties);
        rabbitTemplate.convertAndSend("topic001","spring.add",message);
        rabbitTemplate.convertAndSend("topic002","rabbit.add",message);
    }
}
