package com.ecochain.ledger.mapper;

import com.ecochain.ledger.model.ShopGoodsBrand;

/**
 * Created by LiShuo on 2016/10/24.
 */
public interface ShopGoodsBrandMapper {
    int deleteByPrimaryKey(Short brandId);

    int insert(ShopGoodsBrand record);

    int insertSelective(ShopGoodsBrand record);

    ShopGoodsBrand selectByPrimaryKey(Short brandId);

    int updateByPrimaryKeySelective(ShopGoodsBrand record);

    int updateByPrimaryKeyWithBLOBs(ShopGoodsBrand record);

    int updateByPrimaryKey(ShopGoodsBrand record);

}