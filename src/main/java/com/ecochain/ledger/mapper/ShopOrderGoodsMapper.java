package com.ecochain.ledger.mapper;

import com.ecochain.ledger.model.ShopOrderGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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