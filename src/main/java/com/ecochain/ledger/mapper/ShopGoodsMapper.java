package com.ecochain.ledger.mapper;


import com.ecochain.ledger.model.ShopGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by LiShuo on 2016/10/24.
 */
public interface ShopGoodsMapper {
    int deleteByPrimaryKey(Integer goodsId);

    int insert(ShopGoods record);

    int insertSelective(ShopGoods record);

    ShopGoods selectByPrimaryKey(Integer goodsId);

    ShopGoods selectByPrimaryKey2(Integer goodsId);

    int updateByPrimaryKeySelective(ShopGoods record);

    int updateByPrimaryKeyWithBLOBs(ShopGoods record);

    int updateByPrimaryKey(ShopGoods record);

    List<ShopGoods> showAll();

    List searchGood(String searchMsg);

    List  queryGoodsInfo(String catId);

    List SearchgoodsListParam(String catId);

    List queryGoodsDetailInfoById(String goodsId);

    List queryHotGoodsDetailInfoById(String goodsId);

    List queryGoodsDetailInfoByGoodsId(String goodsId);

    List showBySearchInfo(@Param("brandId") String brandId, @Param("catId") String catId);

    List goodsDetailGuessYourLike(String goodsId);

    List searchGoodsOtherInfo(String goodsId);

    List<Map<String,Object>> serchMyCart(Integer userId);

    List serchMyCartYourLike(Integer userId);

    List<Map<String, Object>> guessYourLike(String goodsId);

    Map<String, Object> serchMyCartGoodsPrice(Integer goodsId);

    String getGoodsStock(String goodsId);

    int addGoodStockByType(Integer goodsId);

    int delGoodStockByType(Integer goodsId);

    int queryGoodsStock(Integer goodsId);

    int getOrderType(String orderNo);

    List getShopHotGoods();

    boolean updateGoodStock(String goodsId, String orderNo);

    List searchTagInfo();

    List getBuyCountByOrderNo(String orderNo);

    boolean updateShopGoodsSales(Map map);

    boolean updateShopHotGoodsSales(Map map);

    Map getSkuGoodsInfo(String skuValue);

    int delSkuGoodStockByType(String skuValue);

    int addSkuGoodStockByType(String skuValue);

    List queryRMBGoodsDetailInfoByGoodsId(String goodsId);
}