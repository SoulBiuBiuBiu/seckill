package com.example.miaosha.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;
    /*
    获取对象
     */
    public <T> T get(KeyPrefix prefix ,String key,Class<T> clazz){
        Jedis jedis =null;
        try{
            jedis= jedisPool.getResource();
            //生成realkey
            String k=prefix.getPrefix()+ key;
            String s = jedis.get(k);
            T t=StringToBean(s,clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }
    /*
    设置对象
     */
    public <T> boolean set(KeyPrefix prefix ,String key,T value){
        Jedis jedis =null;
        try{
            jedis= jedisPool.getResource();
            String str=beanToString(value);
            String k = prefix.getPrefix() + key;
            int seconds = prefix.expireSeconds();
            if(seconds<=0){
                jedis.set(k,str);
            }else{
                jedis.setex(k,seconds,str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }
    /*
    判断是否存在
     */
    public <T> boolean exists(KeyPrefix prefix ,String key){
        Jedis jedis =null;
        try{
            jedis= jedisPool.getResource();
            String k = prefix.getPrefix() + key;
            return  jedis.exists(k);
        }finally {
            returnToPool(jedis);
        }
    }
    /*
    增加
     */
    public <T> Long incr(KeyPrefix prefix ,String key){
        Jedis jedis =null;
        try{
            jedis= jedisPool.getResource();
            String k = prefix.getPrefix() + key;
            return jedis.incr(k);
        }finally {
            returnToPool(jedis);
        }
    }
    /*
   删除
    */
    public <T> boolean delete(KeyPrefix prefix ,String key){
        Jedis jedis =null;
        try{
            jedis= jedisPool.getResource();
            String k = prefix.getPrefix() + key;
            Long del = jedis.del(k);
            return del>0;
        }finally {
            returnToPool(jedis);
        }
    }
    /*
    减少
     */
    public <T> Long decr(KeyPrefix prefix ,String key){
        Jedis jedis =null;
        try{
            jedis= jedisPool.getResource();
            String k = prefix.getPrefix() + key;
            return jedis.decr(k);
        }finally {
            returnToPool(jedis);
        }
    }

    private <T> String beanToString(T value) {
        if(value==null) return null;
        Class<?> clazz=value.getClass();
        if(clazz==int.class||clazz==Integer.class){
            return String.valueOf(value);
        }else if(clazz==String.class){
            return  (String) value;
        }else if(clazz==long.class||clazz==Long.class){
            return String.valueOf(value);
        }else{
            return JSON.toJSONString(value);
        }
    }

    private <T> T StringToBean(String s,Class<T> clazz) {
        if(s==null||s.length()==0||clazz==null) return null;
        if(clazz==int.class||clazz==Integer.class){
            return (T)Integer.valueOf(s);
        }else if(clazz==String.class){
            return  (T)s;
        }else if(clazz==long.class||clazz==Long.class){
            return (T)Long.valueOf(s);
        }else{
            return JSON.toJavaObject(JSON.parseObject(s),clazz);
        }
    }

    private void returnToPool(Jedis jedis){
        if(jedis!=null){
            jedis.close();
        }
    }

}
