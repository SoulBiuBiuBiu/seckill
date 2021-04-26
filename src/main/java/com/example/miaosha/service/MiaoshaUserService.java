package com.example.miaosha.service;

import com.example.miaosha.dao.MiaoshaUserDao;
import com.example.miaosha.domain.MiaoshaUser;
import com.example.miaosha.exception.GlobalException;
import com.example.miaosha.redis.MiaoshaUserKey;
import com.example.miaosha.redis.RedisService;
import com.example.miaosha.result.CodeMsg;
import com.example.miaosha.util.MD5Util;
import com.example.miaosha.util.UUIDUtil;
import com.example.miaosha.vo.LoginVo;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN="token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    private MiaoshaUser getUserById(long id){
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getByID, String.valueOf(id), MiaoshaUser.class);
        if(user!=null) return user;
        user = miaoshaUserDao.getById(id);
        if(user!=null){
            redisService.set(MiaoshaUserKey.getByID,String.valueOf(id),user);
        }
        return user;
    }

    public boolean updatePassword(String token,long id,String pwdNew){
        MiaoshaUser user = getUserById(id);
        if(user==null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //修改哪个字段就设置哪个字段
        MiaoshaUser toBeupdate=new MiaoshaUser();
        toBeupdate.setId(id);
        toBeupdate.setPassword(MD5Util.formPassToDbPass(pwdNew,user.getSalt()));

        miaoshaUserDao.update(toBeupdate);
        redisService.delete(MiaoshaUserKey.getByID,String.valueOf(id));
        user.setPassword(toBeupdate.getPassword());
        redisService.set(MiaoshaUserKey.token,token,user);

        return true;
    }



    public boolean login(HttpServletResponse response,LoginVo loginVo) {
        if(loginVo==null) throw new GlobalException(CodeMsg.SERVER_ERROR);
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        MiaoshaUser user = getUserById(Long.parseLong(mobile));
        if(user==null)  throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        String dbPass = user.getPassword();
        String dbSalt = user.getSalt();
        String calcPass = MD5Util.formPassToDbPass(formPass, dbSalt);
        if(!calcPass.equals(dbPass))  throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        //生成cookie
        String token=UUIDUtil.uuid();
        addCookie(user,token,response);
        return true;
    }
    private void addCookie(MiaoshaUser user,String token,HttpServletResponse response){
        redisService.set(MiaoshaUserKey.token,token,user);
        Cookie cookie=new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public MiaoshaUser getByToken(String token,HttpServletResponse response) {
        if(StringUtils.isNullOrEmpty(token)) return null;
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);

        //延长有效期
        if(user!=null) addCookie(user,token,response);
        return user;
    }
}
