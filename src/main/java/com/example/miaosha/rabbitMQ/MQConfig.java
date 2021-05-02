package com.example.miaosha.rabbitMQ;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class MQConfig {

    public static final String QUEUE="queue";
    public static final String TOPIC_QUEUE1="topicQueue1";
    public static final String HEADER_QUEUE1="headerQueue1";
    public static final String TOPIC_QUEUE2="topicQueue2";
    public static final String FANOUT_QUEUE1="fanoutQueue1";
    public static final String FANOUT_QUEUE2="fanoutQueue2";
    public static final String TOPIC_EXCHANGE="topicExchange";
    public static final String FANOUT_EXCHANGE="fanoutExchange";
    public static final String HEADER_EXCHANGE="headerExchange";
    public static final String ROUTING_KEY1="topicKey1";
    public static final String ROUTING_KEY2="topic#";

    /**
     * Direct模式 交换机Exchang
     * 客户端->交换机（路由）->队列
     * @return
     */
    @Bean
    public Queue queue(){
        return new Queue(QUEUE,true);
    }

    /**
     * Topic模式 交换机Exchang
     * @return
     */
    @Bean
    public Queue topicQueue1(){
        return new Queue(TOPIC_QUEUE1,true);
    }

    @Bean
    public Queue topicQueue2(){
        return new Queue(TOPIC_QUEUE2,true);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(ROUTING_KEY1);
    }

    @Bean
    public Binding topicBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with(ROUTING_KEY2);
    }

    /**
     * Fanout模式 交换机Exchang  =广播模式
     * @return
     */
    @Bean
    public Queue fanoutQueue1(){
        return new Queue(FANOUT_QUEUE1,true);
    }

    @Bean
    public Queue fanoutQueue2(){
        return new Queue(FANOUT_QUEUE2,true);
    }


    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding fanoutBinding1(){
        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutBinding2(){
        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
    }

    /**
     * Header模式 交换机Exchang
     * @return
     */

    @Bean
    public Queue HeaderQueue1(){
        return new Queue(HEADER_QUEUE1,true);
    }
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADER_EXCHANGE);
    }

    @Bean
    public Binding headerBinding(){
        Map<String,Object> map=new HashMap<>();
        map.put("header1","value1");
        map.put("header2","value2");
        return BindingBuilder.bind(HeaderQueue1()).to(headersExchange()).whereAll(map).match();
    }





}
