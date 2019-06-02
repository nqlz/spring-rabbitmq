package com.nqlz.springrabbitmq.config;

public class MessageDelegate {

    public void handleMessage(byte[] messageBody) {
        System.err.println("默认方法, 消息内容:" + new String(messageBody));
    }

    public void consumeMessage(byte[] messageBody) {
        System.err.println("字节数组方法, 消息内容:" + new String(messageBody));
    }

    public void consumeMessage(String messageBody) {
        System.err.println("字符串方法, 消息内容:" + messageBody);
    }
    public void method1(String messageBody) {
        System.err.println("method1方法, 消息内容:" + messageBody);
    }
    public void method2(String messageBody) {
        System.err.println("method2方法, 消息内容:" + messageBody);
    }
}
