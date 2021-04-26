package com.example.miaosha.controller;

import com.example.miaosha.domain.MiaoshaUser;
import com.example.miaosha.redis.GoodsKey;
import com.example.miaosha.redis.RedisService;
import com.example.miaosha.result.Result;
import com.example.miaosha.service.GoodsService;
import com.example.miaosha.service.MiaoshaUserService;
import com.example.miaosha.vo.GoodsVo;
import com.example.miaosha.vo.LoginVo;
import com.mysql.cj.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    GoodsService goodsService;
    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
    public String toList(Model model, HttpServletRequest request, HttpServletResponse response, MiaoshaUser user){
        String html = redisService.get(GoodsKey.GetGoodsList, "", String.class);
        if(!StringUtils.isNullOrEmpty(html)) return html;
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsVos);
        model.addAttribute("user",user);
        //

        //手动渲染
        IWebContext webContext = new WebContext(request,response,request.getServletContext(),response.getLocale(),model.asMap());
        html=thymeleafViewResolver.getTemplateEngine().process("goods_list",webContext);
        if(!StringUtils.isNullOrEmpty(html)){
            redisService.set(GoodsKey.GetGoodsList,"",html);
        }
        return html;
    }

    @RequestMapping(value = "/detail/{goodsId}",produces = "text/html")
    @ResponseBody
    public String toDetail(Model model,HttpServletRequest request, HttpServletResponse response, MiaoshaUser user, @PathVariable("goodsId") long goodsId){
        //snowflack算法
        model.addAttribute("user",user);
        GoodsVo goods=goodsService.getGoodsVoById(goodsId);

        model.addAttribute("goods",goods);

        long startAt = goods.getStartDate().getTime();
        long endAt =goods.getEndDate().getTime();
        long now=System.currentTimeMillis();

        int miaoshaStatus=0;
        long remainSeconds=0;

        if(now<startAt){
            remainSeconds=(startAt-now)/1000;
        }else if(now>endAt){
            miaoshaStatus=2;
            remainSeconds=-1;
        }else{
            miaoshaStatus=1;
            remainSeconds=0;
        }
        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSeconds);

        return "";
    }


}
