package com.nqlz.springrabbitmq.entity;

import lombok.Data;

@Data
public class Order {
    private Integer id;
    private String name;
    private String content;
}
