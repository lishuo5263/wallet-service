package com.ecochain.ledger.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ecochain.ledger.mapper.ShopCartMapper;
import com.ecochain.ledger.mapper.ShopGoodsMapper;
import com.ecochain.ledger.model.ShopCart;
import com.ecochain.ledger.model.ShopGoods;
import com.ecochain.ledger.service.ShopCartService;

/**
 * Created by LiShuo on 2016/10/26.
 */
@Component("shopCartService")
public class ShopCartServiceImpl implements ShopCartService {

    @Autowired
    private ShopCartMapper shopCartMapper;

    @Autowired
    private ShopGoodsMapper shopGoodsMapper;

    @Override
    public int insertMyCart(ShopCart shopCart) {
        return this.shopCartMapper.insert(shopCart);
    }

    @Override
    public ShopCart queryCartGoods(String userId,String goodsId,String skuValue) {
        return this.shopCartMapper.queryCartGoods(userId,goodsId,skuValue);
    }

    @Override
    public ShopCart queryCartGoodsID(Integer userId, Integer goodsId) {
        return this.shopCartMapper.queryCartGoodsID(userId,goodsId);
    }

    @Override
    public List queryCartAllGoodsID(Map map) {
        return this.shopCartMapper.queryCartAllGoodsID(map);
    }

    @Override
    public Map<String, Object> serchMyCartGoodsPrice(Integer goodsId) {
        return this.shopGoodsMapper.serchMyCartGoodsPrice(goodsId);
    }

    @Override
    public int updateMyCart(ShopCart shopCart) {
        return this.shopCartMapper.updateByPrimaryKeySelective(shopCart);
    }

    @Override
    public int deleteMyCart(Integer id) {
        return this.shopCartMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void batchDelete(List list) {
        this.shopCartMapper.batchDelete(list);
    }

    @Override
    public List myCartToGenerateOrder(Map map) {
        return this.shopCartMapper.myCartToGenerateOrder(map);
    }

    @Override
    public List<ShopGoods> queryGoodsDetailInfoByGoodsId(String goodsId,String isPromote) {
        if("1".equals(isPromote)){
            return this.shopGoodsMapper.queryHotGoodsDetailInfoById(goodsId);
        }else if("0".equals(isPromote)){
            return this.shopGoodsMapper.queryGoodsDetailInfoByGoodsId(goodsId);
        }else{
            return null;
        }
    }
}
