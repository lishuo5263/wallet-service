package com.ecochain.ledger.mapper;

import com.ecochain.ledger.model.ShopGoodsCategory;

import java.util.List;
import java.util.Map;

/**
 * Created by LiShuo on 2016/10/24.
 */
public interface ShopGoodsCategoryMapper {
    int deleteByPrimaryKey(Short catId);

    int insert(ShopGoodsCategory record);

    int insertSelective(ShopGoodsCategory record);

    ShopGoodsCategory selectByPrimaryKey(Short catId);

    int updateByPrimaryKeySelective(ShopGoodsCategory record);

    int updateByPrimaryKey(ShopGoodsCategory record);

    List<ShopGoodsCategory> showAll();

    List<Map<String, Object>> seleceUnionLevelOneName();

    List<Map<String, Object>> seleceLevelOneInfo();

    List<Map<String, Object>> seleceLevelTwoNameInfo();

    List<Map<String, Object>> searchGoodsType(String catId);

    List seleceCatId(Integer catId);

    int selectGoodsCount(Integer catId);

    List<Map<String,Object>> seleceUnionLevelOneNameByLevel(Integer isLevel);

    List searchGoodsSameType(String catId);

}