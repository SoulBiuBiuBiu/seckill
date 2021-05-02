package com.example.miaosha.rabbitMQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
public class MQRecevier {
    private static Logger log= LoggerFactory.getLogger(MQRecevier.class);
    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receive(String message){
        log.info("receive topic1: message:"+message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receive2(String message){
        log.info("receive topic2: message:"+message);
    }

    @RabbitListener(queues = MQConfig.HEADER_QUEUE1)
    public void receiveHeaderQueue(byte[] message){
        log.info("receive header: message:"+new String(message));
    }

    @RabbitListener(queues = MQConfig.FANOUT_QUEUE1)
    public void receive4(String message){
        log.info("receive fanout1: message:"+message);
    }

    @RabbitListener(queues = MQConfig.FANOUT_QUEUE2)
    public void receive5(String message){
        log.info("receive fanout2: message:"+message);
    }

}
