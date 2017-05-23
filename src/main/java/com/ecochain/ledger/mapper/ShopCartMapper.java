package com.ecochain.ledger.mapper;

import com.ecochain.ledger.model.ShopCart;

import java.util.List;
import java.util.Map;

/**
 * Created by LiShuo on 2016/10/24.
 */
public interface ShopCartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopCart record);

    int insertSelective(ShopCart record);

    ShopCart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopCart record);

    int updateByPrimaryKey(ShopCart record);

    ShopCart queryCartGoods(String userId, String goodsId, String skuValue);

    ShopCart queryCartGoodsID(Integer userId, Integer goodsId);

    List queryCartAllGoodsID(Map map);

    List myCartToGenerateOrder(Map map);

    void batchDelete(List list);

    void batchDeleteMyCart(Map map);
}