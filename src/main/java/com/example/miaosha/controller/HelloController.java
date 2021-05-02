package com.example.miaosha.controller;

import com.example.miaosha.domain.User;
import com.example.miaosha.rabbitMQ.MQSender;
import com.example.miaosha.redis.RedisService;
import com.example.miaosha.redis.UserKey;
import com.example.miaosha.result.CodeMsg;
import com.example.miaosha.result.Result;
import com.example.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;
    @Autowired
    MQSender mqSender;

    @RequestMapping("/mq")
    @ResponseBody
    public Result<Boolean> mq(){
        mqSender.send("hello,imooc!");
        return Result.success(true);
    }

    @RequestMapping("/mq/topic")
    @ResponseBody
    public Result<Boolean> sendtopic(){
        mqSender.sendTopic("hello,imooc!");
        return Result.success(true);
    }

    @RequestMapping("/mq/fanout")
    @ResponseBody
    public Result<Boolean> sendFanout(){
        mqSender.sendFanout("hello,fanout!");
        return Result.success(true);
    }

    @RequestMapping("/mq/header")
    @ResponseBody
    public Result<Boolean> header(){
        mqSender.sendHeader("hello,imooc!");
        return Result.success(true);
    }




    @RequestMapping("/")
    @ResponseBody
    public Result<String> home(){
        return Result.success("hello!");
    }

    @RequestMapping("/error1")
    @ResponseBody
    public Result<String> helloError(){
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbget(){
        User user = userService.getById(1);
        return Result.success(user);
    }
    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx(){
        userService.tx();
        return Result.success(true);
    }
    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","Mr.Wang");
        return "hello";
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet(){
        User user = redisService.get(UserKey.getById,""+1, User.class);
        return Result.success(user);
    }

    /*@RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){
        User user=new User(1,"1111");
        Boolean b = redisService.set(UserKey.getById,""+1, user);
        return Result.success(b);
    }*/
}
