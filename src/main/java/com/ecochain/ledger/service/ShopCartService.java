package com.ecochain.ledger.service;


import java.util.List;
import java.util.Map;

import com.ecochain.ledger.model.ShopCart;

/**
 * Created by LiShuo on 2016/10/26.
 */
public interface ShopCartService {

    int insertMyCart(ShopCart shopCart);

    int updateMyCart(ShopCart shopCart);

    int deleteMyCart(Integer id);

    /**
     * 批量删除购物车
     * @param list
     */
    void batchDelete(List list);

    ShopCart queryCartGoods(String userId,String goodsId,String skuValue);

    Map<String, Object> serchMyCartGoodsPrice(Integer goodsId);

    ShopCart queryCartGoodsID(Integer userId, Integer goodsId);

    List queryCartAllGoodsID(Map map);

    List myCartToGenerateOrder(Map map);

    List queryGoodsDetailInfoByGoodsId(String goodsId,String isPromote);
}
