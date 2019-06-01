package com.nqlz.springrabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan("com.nqlz.springrabbitmq.*")
public class SpringRabbitmqApplicationTests {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testAdmin(){
        rabbitAdmin.declareExchange(new DirectExchange("test.direct",false,false));
        rabbitAdmin.declareExchange(new TopicExchange("test.topic",false,false));
        rabbitAdmin.declareExchange(new FanoutExchange("test.fanout",false,false));


    }
}
