package com.ecochain.ledger.mapper;

import java.util.List;

import com.ecochain.ledger.model.ShopGoodsSku;
/**
 * Created by LiShuo on 2016/10/24.
 */
public interface ShopGoodsSkuMapper {
    int deleteByPrimaryKey(Integer skuId);

    int insert(ShopGoodsSku record);

    int insertSelective(ShopGoodsSku record);

    ShopGoodsSku selectByPrimaryKey(Integer skuId);

    int updateByPrimaryKeySelective(ShopGoodsSku record);

    int updateByPrimaryKey(ShopGoodsSku record);

    List searchGoodsSku(String goodsId);
}