package com.nqlz.springrabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.*")
public class SpringRabbitmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRabbitmqApplication.class, args);
    }

}
