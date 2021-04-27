package com.example.miaosha.controller;

import com.example.miaosha.domain.MiaoshaUser;
import com.example.miaosha.domain.OrderInfo;
import com.example.miaosha.redis.GoodsKey;
import com.example.miaosha.redis.RedisService;
import com.example.miaosha.result.CodeMsg;
import com.example.miaosha.result.Result;
import com.example.miaosha.service.GoodsService;
import com.example.miaosha.service.OrderService;
import com.example.miaosha.vo.GoodsDetailVo;
import com.example.miaosha.vo.GoodsVo;
import com.example.miaosha.vo.OrderDetailVo;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;

    @RequestMapping(value = "/detail")
    @ResponseBody
    public Result<OrderDetailVo> detail(Model model, MiaoshaUser user, @RequestParam("orderId")long orderId){
        if(user==null) return Result.error(CodeMsg.SESSION_ERROR);//可以优化  采用拦截器
        OrderInfo orderInfo= orderService.getOrderById(orderId);
        if(orderInfo==null) return Result.error(CodeMsg.ORDER_NOT_EXIST);
        Long goodsId = orderInfo.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoById(goodsId);
        OrderDetailVo vo=new OrderDetailVo();
        vo.setOrder(orderInfo);
        vo.setGoods(goods);
        return Result.success(vo);
    }


}
