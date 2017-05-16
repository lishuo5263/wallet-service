package com.ecochain.ledger.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ecochain.ledger.model.ShopOrderGoods;

public interface ShopOrderGoodsMapper {
    int deleteByPrimaryKey(Integer recId);

    int insert(@Param("shopOrderGoods") List<ShopOrderGoods> record);

    int insertSelective(ShopOrderGoods record);

    ShopOrderGoods selectByPrimaryKey(Integer recId);

    int updateByPrimaryKeySelective(ShopOrderGoods record);

    int updateByPrimaryKeyWithBLOBs(ShopOrderGoods record);

    int updateByPrimaryKey(ShopOrderGoods record);

    Map queryActiveInfo(String activityId);

    Integer queryGoodsByCount(Integer userId, Integer goodsId);
}