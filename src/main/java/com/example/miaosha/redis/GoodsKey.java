package com.example.miaosha.redis;


public class GoodsKey extends BasePrefix{
    private GoodsKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    public static GoodsKey GetGoodsList=new GoodsKey(60,"gl");
    public static GoodsKey GetGoodsDetail=new GoodsKey(60,"gd");
}
