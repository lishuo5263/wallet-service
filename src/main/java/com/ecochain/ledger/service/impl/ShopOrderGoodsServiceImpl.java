package com.ecochain.ledger.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ecochain.ledger.dao.DaoSupport;
import com.ecochain.ledger.mapper.ShopGoodsMapper;
import com.ecochain.ledger.mapper.ShopOrderGoodsMapper;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.ShopOrderGoodsService;

/**
 * Created by LiShuo on 2016/10/28.
 */
@Component("shopOrderGoodsService")
public class ShopOrderGoodsServiceImpl implements ShopOrderGoodsService {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Resource
    private ShopGoodsMapper shopGoodsMapper;

    @Resource
    private ShopOrderGoodsMapper shopOrderGoodsMapper;

    @Override
    public boolean updateLogistics(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("com.qkl.wlsc.provider.dao.ShopOrderGoodsMapper.updateLogistics", pd)>0;
    }

    @Override
    public PageData getOrderGoods(PageData pd, String versionNo) throws Exception {
        return (PageData)dao.findForObject("com.qkl.wlsc.provider.dao.ShopOrderGoodsMapper.getOrderGoods", pd);
    }

    @Override
    public boolean updateOrderGoodsStatus(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("com.qkl.wlsc.provider.dao.ShopOrderGoodsMapper.updateOrderGoodsStatus", pd)>0;
    }

    @Override
    public Integer queryGoodsByCount(Integer userId, Integer goodsId) {
        return shopOrderGoodsMapper.queryGoodsByCount(userId,goodsId);
    }

    @Override
    public Map queryActiveInfo(String activityId) {
        return shopOrderGoodsMapper.queryActiveInfo(activityId);
    }

    @Override
    public PageData getOrderGoodsAndUserInfoById(String rec_id) throws Exception {
        return (PageData)dao.findForObject("com.qkl.wlsc.provider.dao.ShopOrderGoodsMapper.getOrderGoodsAndUserInfoById", rec_id);
    }

    @Override
    public String getOneGoodsNameByOrderNo(String shop_order_no) throws Exception {
        return (String)dao.findForObject("com.qkl.wlsc.provider.dao.ShopOrderGoodsMapper.getOneGoodsNameByOrderNo", shop_order_no);
    }

    @Override
    public boolean updateOrderGoodsStatusByHash(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("com.qkl.wlsc.provider.dao.ShopOrderGoodsMapper.updateOrderGoodsStatusByHash", pd)>0;
    }

}
