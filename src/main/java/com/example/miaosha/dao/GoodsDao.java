package com.example.miaosha.dao;

import com.example.miaosha.domain.Goods;
import com.example.miaosha.domain.MiaoshaGoods;
import com.example.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsDao {
    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    List<GoodsVo> listGoodsVo();
    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id=#{goodsId}")
    GoodsVo getGoodsVoById(@Param("goodsId") long goodsId);
    //and stock_count>0 解决库存为负
    //卖超解决 建立miaosha_order表的唯一索引（goods_id, order_id）  如果同一个用户触发了两次，第二次会回滚。
    @Update("update miaosha_goods set stock_count=stock_count-1 where goods_id=#{goodsId} and stock_count>0")
    int reduceStock(MiaoshaGoods g);
}
