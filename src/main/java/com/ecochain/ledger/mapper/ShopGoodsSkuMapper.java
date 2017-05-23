package com.ecochain.ledger.mapper;

import com.ecochain.ledger.model.ShopGoodsSku;

import java.util.List;

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