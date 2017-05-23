package com.ecochain.ledger.mapper;

import com.ecochain.ledger.model.ShopGoodsBrandDetail;

import java.util.List;

/**
 * Created by LiShuo on 2016/10/24.
 */
public interface ShopGoodsBrandDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopGoodsBrandDetail record);

    int insertSelective(ShopGoodsBrandDetail record);

    ShopGoodsBrandDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopGoodsBrandDetail record);

    int updateByPrimaryKey(ShopGoodsBrandDetail record);

    List showGoods(String catId);

    List showGoodsAttr(String categoryId);

    List searchGoodsOtherBrand(String goodsId);
}